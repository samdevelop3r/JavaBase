package javamysql;

import javamysql.config.Convert;
import javamysql.connectors.MySqlConnector;
import javamysql.data_classes.Column;
import javamysql.data_classes.Table;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;

public class MySql {
    private final MySqlConnector mySqlConnector;

    public MySql(String databaseUrl) throws SQLException, ClassNotFoundException {
        this.mySqlConnector = new MySqlConnector(Convert.asUrl(databaseUrl));
    }

    public Table createTable(String tableName, List<Column> columnList) throws SQLException {
        HashMap<String, Column> columnHashMap = new HashMap<>();
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("CREATE TABLE IF NOT EXISTS %s(".formatted(tableName));
        for (Column column:
             columnList) {
            queryBuilder.append("%s %s, ".formatted(column.columnName(), column.columnType().equals(Integer.class) ? " INTEGER" : " TEXT"));
            columnHashMap.put(column.columnName(), column);
        }
        queryBuilder.delete(queryBuilder.length() - 2, queryBuilder.length());
        queryBuilder.append(")");
        Table table = new Table(tableName, columnHashMap, this.mySqlConnector);
        this.mySqlConnector.inputQuery(queryBuilder.toString());
        return table;
    }

    public Table getTable(String tableName) throws SQLException {
        Object resultSet = mySqlConnector.inputQuery("DESCRIBE %s".formatted(tableName));
        ResultSet newResultSet = (ResultSet) resultSet;
        return new Table(tableName, Convert.getColumns(newResultSet), this.mySqlConnector);
    }

}
