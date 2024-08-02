package com.ssss.dao;

import com.ssss.model.Currency;
import com.ssss.model.ExchangeRate;
import com.ssss.util.ConnectionManager;

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
        return null;
    }

    public Optional<ExchangeRate> findByCode(String baseCode, String targetCode) {
        try (Connection connection = ConnectionManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(FIND_BY_CODES)) {
            statement.setString(1, baseCode);
            statement.setString(2, targetCode);
            ResultSet resultSet = statement.executeQuery();
            // TODO maybe exception if resultset null bcs no such base-target codes
            resultSet.next();
            return Optional.of(buildExchangeRate(resultSet));


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
