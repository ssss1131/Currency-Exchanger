package com.ssss.dao;

import com.ssss.model.Currency;
import com.ssss.util.ConnectionManager;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static java.sql.Statement.RETURN_GENERATED_KEYS;

public class CurrencyDao implements Dao<Currency> {
    private static final CurrencyDao INSTANCE = new CurrencyDao();

    private CurrencyDao() {
    }

    public static CurrencyDao getInstance() {
        return INSTANCE;
    }

    private static final String FIND_ALL_SQL = """
                    SELECT ID,
                           Code,
                           FullName,
                           Sign
                    FROM Currencies;
            """;
    private static final String SAVE_SQL = """
            INSERT INTO Currencies(Code,FullName,Sign)
            VALUES (?,?,?);
            """;

    @Override
    public List<Currency> findAll() {
        try (Connection connection = ConnectionManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(FIND_ALL_SQL)) {
            ResultSet resultSet = statement.executeQuery();
            List<Currency> currencies = new ArrayList<>();

            while (resultSet.next()) {
                Currency currency = buildCurrency(resultSet);
                currencies.add(currency);
            }
            return currencies;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public Currency save(Currency currency) {
        try (Connection connection = ConnectionManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(SAVE_SQL, RETURN_GENERATED_KEYS)) {
            statement.setString(1, currency.getCode());
            statement.setString(2, currency.getFullName());
            statement.setString(3, currency.getSign());
            statement.executeUpdate();
            ResultSet generatedKeys = statement.getGeneratedKeys();
            generatedKeys.next();
            currency.setId(generatedKeys.getInt(1));
            return currency;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private Currency buildCurrency(ResultSet resultSet) throws SQLException {
        return Currency.builder()
                .id(resultSet.getInt("ID"))
                .code(resultSet.getString("Code"))
                .fullName(resultSet.getString("FullName"))
                .sign(resultSet.getString("Sign"))
                .build();
    }

}
