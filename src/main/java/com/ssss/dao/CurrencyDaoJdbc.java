package com.ssss.dao;

import com.ssss.exception.AlreadyExistsException;
import com.ssss.exception.DatabaseUnavailableException;
import com.ssss.exception.NotExistException;
import com.ssss.model.Currency;
import com.ssss.util.ConnectionManager;
import org.sqlite.SQLiteErrorCode;
import org.sqlite.SQLiteException;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static java.sql.Statement.RETURN_GENERATED_KEYS;

public class CurrencyDaoJdbc implements CurrencyDao {

    private static final CurrencyDaoJdbc INSTANCE = new CurrencyDaoJdbc();

    private CurrencyDaoJdbc() {
    }

    public static CurrencyDao getInstance() {
        return INSTANCE;
    }

    private static final String FIND_ALL_SQL = """
                    SELECT ID,
                           Code,
                           FullName,
                           Sign
                    FROM Currencies
            """;
    private static final String SAVE_SQL = """
            INSERT INTO Currencies(Code,FullName,Sign)
            VALUES (?,?,?);
            """;
    private static final String FIND_BY_CODE_SQL = FIND_ALL_SQL + "WHERE Code = ?";

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
            throw new DatabaseUnavailableException("The database is currently unavailable. Please try again later.");
        }

    }

    @Override
    public Currency save(Currency currency) {
        try (Connection connection = ConnectionManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(SAVE_SQL, RETURN_GENERATED_KEYS)) {
            statement.setString(1, currency.getCode().toUpperCase());
            statement.setString(2, currency.getName());
            statement.setString(3, currency.getSign());
            statement.executeUpdate();
            ResultSet generatedKeys = statement.getGeneratedKeys();
            generatedKeys.next();
            currency.setId(generatedKeys.getInt(1));
            return currency;
        } catch (SQLException e) {
            if (e instanceof SQLiteException) {
                SQLiteException exception = (SQLiteException) e;
                if (exception.getResultCode().code == SQLiteErrorCode.SQLITE_CONSTRAINT_UNIQUE.code) {
                    throw new AlreadyExistsException("Currency with code " + currency.getCode().toUpperCase() + " already exists");
                }
            }

            throw new DatabaseUnavailableException("The database is currently unavailable. Please try again later.");
        }
    }

    public Currency findByCode(String code) {
        try(Connection connection = ConnectionManager.getConnection();
        PreparedStatement statement = connection.prepareStatement(FIND_BY_CODE_SQL)) {
            statement.setString(1, code);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return buildCurrency(resultSet);
            }
            throw new NotExistException("Currency not found");
        } catch (SQLException e) {
            throw new DatabaseUnavailableException("The database is currently unavailable. Please try again later.");
        }
    }

    private Currency buildCurrency(ResultSet resultSet) throws SQLException {
        return Currency.builder()
                .id(resultSet.getInt("ID"))
                .code(resultSet.getString("Code"))
                .name(resultSet.getString("FullName"))
                .sign(resultSet.getString("Sign"))
                .build();
    }

}
