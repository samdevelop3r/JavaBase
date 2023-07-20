package javamysql.data_classes;

import javamysql.config.Convert;
import javamysql.connectors.MySqlConnector;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;

public class Table {
    private final String tableName;
    private final HashMap<String, Column> columnHashMap;
    private final MySqlConnector mySqlConnector;

    public Table(String tableName, HashMap<String, Column> columnHashMap, MySqlConnector mySqlConnector) {
        this.tableName = tableName;
        this.columnHashMap = columnHashMap;
        this.mySqlConnector = mySqlConnector;
    }

    public void addColumn(String columnName, Object columnType) throws SQLException {
        this.mySqlConnector.inputQuery("ALTER TABLE %s ADD %s %s".formatted(this.tableName, columnName,
                columnType.equals(Integer.class) ? " INTEGER" : " TEXT"));
        this.columnHashMap.put(columnName, new Column(columnName, columnType));
    }

    public void dropColumn(String columnName) throws SQLException {
        this.mySqlConnector.inputQuery("ALTER TABLE %s DROP COLUMN %s".formatted(this.tableName, columnName));
        this.columnHashMap.remove(columnName);
    }

    public void insertValues(List<SqlData> insertValues) throws SQLException {
        StringBuilder queryBuilder = new StringBuilder()
                .append("INSERT INTO %s(".formatted(this.tableName));
        for (SqlData sqlData:
             insertValues) {
            queryBuilder.append(sqlData.columnName().concat(", "));
        }
        queryBuilder.delete(queryBuilder.length() - 2, queryBuilder.length());
        queryBuilder.append(") VALUES(");
        for (SqlData sqlData:
             insertValues) {
            queryBuilder.append(Convert.asSqlType(sqlData.columnValue(), this.columnHashMap.get(sqlData.columnName()).columnType()).concat(", "));
        }
        queryBuilder.delete(queryBuilder.length() - 2, queryBuilder.length());
        queryBuilder.append(")");
        this.mySqlConnector.inputQuery(queryBuilder.toString());
    }

    public void deleteValue(SqlData locateValue) throws SQLException {
        this.mySqlConnector.inputQuery("DELETE FROM %s WHERE %s = %s".formatted(this.tableName, locateValue.columnName(),
                this.columnHashMap.get(locateValue.columnName()).columnType()));
    }

    public ResultSet selectValue(String selectedValues, Object orderBy, Object locateValue) throws SQLException {
        StringBuilder queryBuilder = new StringBuilder()
                .append("SELECT %s FROM %s".formatted(selectedValues, this.tableName));
        if(locateValue != null) {
            SqlData value = (SqlData) locateValue;
            queryBuilder.append(" WHERE %s = %s".formatted(value.columnName(), Convert.asSqlType(value.columnValue(), this.columnHashMap.get(value.columnName()).columnType())));
        }
        if(orderBy != null) {
            queryBuilder.append(" ORDER BY %s DESC".formatted(orderBy.toString()));
        }
        return (ResultSet) this.mySqlConnector.inputQuery(queryBuilder.toString());
    }

    public void updateValue(List<SqlData> updatingValues, SqlData locateValue) throws SQLException {
        StringBuilder queryBuilder = new StringBuilder()
                .append("UPDATE %s SET ".formatted(this.tableName));
        for (SqlData updatingValue:
             updatingValues) {
            queryBuilder.append("%s = %s, ".formatted(updatingValue.columnName(), Convert.asSqlType(updatingValue.columnValue(), this.columnHashMap.get(updatingValue.columnName()).columnType())));
        }
        queryBuilder.delete(queryBuilder.length() - 2, queryBuilder.length());
        queryBuilder.append(" WHERE %s = %s".formatted(locateValue.columnName(), Convert.asSqlType(locateValue.columnValue(), this.columnHashMap.get(locateValue.columnName()).columnType())));
        this.mySqlConnector.inputQuery(queryBuilder.toString());
    }
}
