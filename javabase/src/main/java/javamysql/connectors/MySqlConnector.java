package javamysql.connectors;

import javamysql.config.Convert;
import javamysql.data_classes.Url;
import java.sql.*;

public class MySqlConnector {
    private final Connection connection;

    public MySqlConnector(Url url) throws SQLException, ClassNotFoundException {
        Class.forName("com.mysql.cj.jdbc.Driver");
        this.connection = DriverManager.getConnection(url.asJDBC(), url.username(), url.password());
    }

    public Object inputQuery(String query) throws SQLException {
        Statement statement = this.connection.prepareStatement(query);
        ResultSet resultSet;
        if(query.contains("DELETE") | query.contains("INSERT") | query.contains("UPDATE") | query.contains("CREATE")) {
            statement.execute(query);
            statement.close();
            return null;
        } else {
            statement.execute(query);
            resultSet = statement.getResultSet();
            if(query.contains("SELECT")) {
                return Convert.asSqlData(resultSet);
            }
            return resultSet;
        }
    }
}
