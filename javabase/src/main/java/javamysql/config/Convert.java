package javamysql.config;

import javamysql.data_classes.Url;


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
}
