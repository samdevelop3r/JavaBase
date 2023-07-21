# JavaBase
Library for automatization of working with mysql database on java

# Examples for working

Creating a table


```java
public static void main(String[] args) throws SQLException, ClassNotFoundException {
        MySql mySql = new MySql("mysql://localhost:3306/root:root/database");
        List<Column> columnList = List.of(new Column("username", String.class),
                                          new Column("user_id", Integer.class));

        mySql.createTable("users", columnList);
    }
```

Insert a column to table

```java
public static void main(String[] args) throws SQLException, ClassNotFoundException {
        MySql mySql = new MySql("mysql://localhost:3306/root:root/database");
        mySql.getTable("users")
                .insertValues(List.of(new SqlData("username", "sam"), 
                        new SqlData("user_id", 1)));
    }
```

Update a values from tables

```java
public static void main(String[] args) throws SQLException, ClassNotFoundException {
        MySql mySql = new MySql("mysql://localhost:3306/root:root/database");
        mySql.getTable("users")
                .updateValue(List.of(new SqlData("username", "proxymallet")),
                        new SqlData("user_id", 1));
        // second argument is identifier of value's location in database. equals WHERE in SQL syntax
    }

```

Add column

```java
public static void main(String[] args) throws SQLException, ClassNotFoundException {
        MySql mySql = new MySql("mysql://localhost:3306/root:root/database");
        mySql.getTable("users")
                .addColumn("balance", Integer.class);
    }
```

Delete column

```java
public static void main(String[] args) throws SQLException, ClassNotFoundException {
        MySql mySql = new MySql("mysql://localhost:3306/root:root/database");
        mySql.getTable("users")
                .dropColumn("balance");
    }
```

Deleting a values from columns

```java
public static void main(String[] args) throws SQLException, ClassNotFoundException {
        MySql mySql = new MySql("mysql://localhost:3306/root:root/database");
        mySql.getTable("users")
                .deleteValue(new SqlData("username", "proxymallet"));
        // this argument is identifier of value's location in database. equals WHERE in SQL syntax
    }
```

Selecting a values from columns

```java
public static void main(String[] args) throws SQLException, ClassNotFoundException {
        MySql mySql = new MySql("mysql://localhost:3306/root:root/database");
        List<SqlData> sqlDataList = mySql.getTable("users")
                .selectValue("*", "balance", new SqlData("username", "proxymallet"));
        // second argument equals ORDER BY in SQL syntax and third argument equals WHERE again
    }
```
