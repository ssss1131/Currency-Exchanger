package com.ssss.dao;

import com.ssss.model.Currency;
import com.ssss.model.ExchangeRate;
import com.ssss.util.ConnectionManager;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ExchangeRateDaoJdbc implements Dao<ExchangeRate> {

    private static final ExchangeRateDaoJdbc INSTANCE = new ExchangeRateDaoJdbc();

    private static final String FIND_ALL_SQL = """
                  SELECT exr.ID AS ExchangeRateId,
                        
                          base.ID AS BaseCurrencyId,
                   base.Code AS BaseCurrencyCode,
                   base.FullName AS BaseCurrencyName,
                   base.Sign AS BaseCurrencySign,
                        
                   target.ID AS TargetCurrencyId,
                   target.Code AS TargetCurrencyCode,
                   target.FullName AS TargetCurrencyName,
                   target.Sign AS TargetCurrencySign,
                        
                   exr.Rate AS ExchangeRate
                   FROM ExchangeRates exr
            JOIN Currencies base ON exr.BaseCurrencyId = base.ID
            JOIN Currencies target ON exr.TargetCurrencyId = target.ID
            """;

    private static final String FIND_BY_CODES = FIND_ALL_SQL + """
            WHERE base.code=? AND target.code=?;
            """;

    private static final String SAVE_SQL = """
                        INSERT INTO ExchangeRates(
                                        BaseCurrencyId,
                                        TargetCurrencyId,
                                        Rate
                                    ) VALUES (?, ?, ?);
            """;

    private static final String UPDATE_SQL = """
                                    UPDATE ExchangeRates
                                    SET Rate =?
                                    WHERE BaseCurrencyId=(SELECT ID FROM Currencies WHERE Code=?)
                                    AND TargetCurrencyId=(SELECT ID FROM Currencies WHERE Code=?);
            """;


    private ExchangeRateDaoJdbc() {
    }

    public static ExchangeRateDaoJdbc getInstance() {
        return INSTANCE;
    }


    @Override
    public List<ExchangeRate> findAll() {
        try (Connection connection = ConnectionManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(FIND_ALL_SQL)) {
            ResultSet resultSet = statement.executeQuery();
            List<ExchangeRate> exchangeRates = new ArrayList<>();
            while (resultSet.next()) {
                ExchangeRate exchangeRate = buildExchangeRate(resultSet);
                exchangeRates.add(exchangeRate);
            }
            return exchangeRates;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<ExchangeRate> save(ExchangeRate entity) {
        try (Connection connection = ConnectionManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(SAVE_SQL)) {
            statement.setInt(1, entity.getBaseCurrency().getId());
            statement.setInt(2, entity.getTargetCurrency().getId());
            statement.setBigDecimal(3, entity.getRate());
            statement.executeUpdate();
            return findByCodes(entity.getBaseCurrency().getCode(), entity.getTargetCurrency().getCode());

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Optional<ExchangeRate> findByCodes(String baseCode, String targetCode) {
        try (Connection connection = ConnectionManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(FIND_BY_CODES)) {
            statement.setString(1, baseCode);
            statement.setString(2, targetCode);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return Optional.of(buildExchangeRate(resultSet));
            } else {
                return Optional.empty();
            }


        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Optional<ExchangeRate> updateByCodes(String baseCode, String targetCode, BigDecimal newRate) {
        try(Connection connection = ConnectionManager.getConnection();
        PreparedStatement statement = connection.prepareStatement(UPDATE_SQL)) {
            statement.setBigDecimal(1, newRate);
            statement.setString(2, baseCode);
            statement.setString(3, targetCode);
            statement.executeUpdate();
            return findByCodes(baseCode, targetCode);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private ExchangeRate buildExchangeRate(ResultSet rs) throws SQLException {
        return ExchangeRate.builder()
                .id(rs.getInt("ExchangeRateId"))
                .baseCurrency(Currency.builder()
                        .id(rs.getInt("BaseCurrencyId"))
                        .code(rs.getString("BaseCurrencyCode"))
                        .name(rs.getString("BaseCurrencyName"))
                        .sign(rs.getString("BaseCurrencySign"))
                        .build())
                .targetCurrency(Currency.builder()
                        .id(rs.getInt("TargetCurrencyId"))
                        .code(rs.getString("TargetCurrencyCode"))
                        .name(rs.getString("TargetCurrencyName"))
                        .sign(rs.getString("TargetCurrencySign"))
                        .build())
                .rate(rs.getBigDecimal("ExchangeRate"))
                .build();
    }
}
