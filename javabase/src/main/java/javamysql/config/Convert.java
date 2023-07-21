package javamysql.config;

import javamysql.data_classes.Column;
import javamysql.data_classes.SqlData;
import javamysql.data_classes.Url;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class Convert {

    public static Url asUrl(String databaseUrl) {
        String[] splittedUrl = databaseUrl.split("/");
        String[] hostnameAndPort = splittedUrl[2].split(":");
        String[] usernameAndPassword = splittedUrl[3].split(":");
        return new Url(hostnameAndPort[0], Integer.parseInt(hostnameAndPort[1]), usernameAndPassword[0],
                usernameAndPassword[1], splittedUrl[4]);
    }

    public static String asSqlType(Object columnValue, Object columnType) {
        return columnType.equals(Integer.class) ? columnValue.toString() : "'%s'".formatted(columnValue);
    }

    public static HashMap<String, Column> getColumns(ResultSet resultSet) throws SQLException {
        HashMap<String, Column> columns = new HashMap<>();
        while (resultSet.next()) {
            columns.put(resultSet.getString("Field"), new Column(resultSet.getString("Field"), resultSet.getString("Type").equals("text") ? String.class : Integer.class));
        }
        return columns;
    }

    public static List<SqlData> asSqlData(ResultSet resultSet) throws SQLException {
        ResultSetMetaData resultSetMetaData = resultSet.getMetaData();
        ArrayList<SqlData> sqlDataList = new java.util.ArrayList<>();

        while (resultSet.next()) {
            for (int i = 1; i < resultSetMetaData.getColumnCount() + 1; i++) {
                sqlDataList.add(new SqlData(resultSetMetaData.getColumnName(i), resultSetMetaData.getColumnTypeName(i).equals("TEXT") ? resultSet.getString(i) : resultSet.getInt(i)));
            }
        }
        return sqlDataList;
    }
}
