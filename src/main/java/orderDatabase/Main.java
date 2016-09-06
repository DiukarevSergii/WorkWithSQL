package orderDatabase;

import java.sql.*;
import java.util.ResourceBundle;
import java.util.Scanner;

public class Main {

    static Connection connection;

    public static void main(String[] args) {
        ResourceBundle res = ResourceBundle.getBundle("db");
        try {
            try (Scanner sc = new Scanner(System.in)) {
                connection = DriverManager.getConnection(
                        res.getString("db.order.url"), res.getString("db.user"), res.getString("db.password"));
                initDB();

                Statement statement = connection.createStatement();
                statement.execute("DROP TABLE IF EXISTS Orders");
                statement.execute("CREATE TABLE Orders " +
                        "(id INT NOT NULL AUTO_INCREMENT PRIMARY KEY, " +
                        "client VARCHAR (10) NOT NULL," +
                        "product VARCHAR (10) NOT NULL," +
                        "quantity INT (3)," +
                        "price INT (4))");

                while (true) {
                    System.out.println("Choose: ");
                    System.out.println("1: Make an order");
                    System.out.println("2: Add client");
                    System.out.println("or else for EXIT");
                    System.out.print("->");

                    String point = sc.nextLine();
                    if (!point.isEmpty()) {
                        int i = Integer.parseInt(point);

                        switch (i) {
                            case 1:
                                makeOrder(sc);
                                break;
                            case 2:
                                addClient(sc);
                                break;
                            default:
                                return;
                        }
                    }
                }
            }
        } catch (SQLException e1) {
            e1.printStackTrace();
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private static void addClient(Scanner sc) throws SQLException {
        System.out.println("Add client:");
        System.out.println("Enter name of client:");
        String newName = sc.nextLine();

        try (PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO Clients " +
                "(name) VALUES (?)")) {
            preparedStatement.setString(1, newName);
            preparedStatement.executeUpdate();
        }

        try (PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM Clients");
             ResultSet rs = preparedStatement.executeQuery()) {

            resultSet(rs);
        }
    }


    private static void makeOrder(Scanner sc) throws SQLException {
        System.out.println("\nMake an order:");
        System.out.println("Enter id of the name client:");
        int id = Integer.parseInt(sc.nextLine());

        PreparedStatement psClients = connection.prepareStatement("INSERT INTO Orders" +
                "(client, product, quantity, price) VALUES (?,?,?,?)");
        PreparedStatement ps = connection.prepareStatement("SELECT * FROM Clients");
        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            if (rs.getInt("id") == id) {
                psClients.setString(1, rs.getString("name"));
            }
        }

        ps = connection.prepareStatement("SELECT * FROM Goods");
        rs = ps.executeQuery();

        System.out.println("Enter id of the product:");
        id = Integer.parseInt(sc.nextLine());
        System.out.println("Enter id of the product:");
        int quantity = Integer.parseInt(sc.nextLine());
        while (rs.next()) {
            if (rs.getInt("id") == id) {
                psClients.setString(2, rs.getString("product"));
                psClients.setInt(3, quantity);
                psClients.setInt(4, rs.getInt("price") * quantity);
            }
        }

        psClients.executeUpdate();

        PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM Orders");
        rs = preparedStatement.executeQuery();
        resultSet(rs);
    }


    private static void initDB() throws SQLException {
        try (Statement statement = connection.createStatement()) {
            statement.execute("DROP TABLE IF EXISTS Goods");
            statement.execute("CREATE TABLE Goods " +
                    "(id INT NOT NULL AUTO_INCREMENT PRIMARY KEY, " +
                    "product VARCHAR (10) NOT NULL," +
                    "quantity INT (3)," +
                    "price INT(5))");

            try (PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO Goods " +
                    "(product, quantity, price) VALUES (?,?,?)")) {
                preparedStatement.setString(1, "Chair");
                preparedStatement.setInt(2, 20);
                preparedStatement.setInt(3, 120);
                preparedStatement.executeUpdate();

                preparedStatement.setString(1, "Table");
                preparedStatement.setInt(2, 5);
                preparedStatement.setInt(3, 560);
                preparedStatement.executeUpdate();

                preparedStatement.setString(1, "Window");
                preparedStatement.setInt(2, 10);
                preparedStatement.setInt(3, 780);
                preparedStatement.executeUpdate();
            }

            statement.execute("DROP TABLE IF EXISTS Clients");
            statement.execute("CREATE TABLE Clients " +
                    "(id INT NOT NULL AUTO_INCREMENT PRIMARY KEY, " +
                    "name VARCHAR (10) NOT NULL)");

            try (PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO Clients " +
                    "(name) VALUES (?)")) {
                preparedStatement.setString(1, "Victor");
                preparedStatement.executeUpdate();

                preparedStatement.setString(1, "Serg");
                preparedStatement.executeUpdate();

                preparedStatement.setString(1, "Tim");
                preparedStatement.executeUpdate();
            }

            try (PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM Goods");
                 ResultSet rs = preparedStatement.executeQuery()) {

                resultSet(rs);
            }
            System.out.println();
            try (PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM Clients");
                 ResultSet rs = preparedStatement.executeQuery()) {

                resultSet(rs);
            }
        }
    }

    private static void resultSet(ResultSet rs) throws SQLException {
        ResultSetMetaData metaData = rs.getMetaData();
        for (int i = 1; i <= metaData.getColumnCount(); i++) {
            System.out.print(metaData.getColumnName(i) + "\t\t");
        }
        System.out.println();

        while (rs.next()) {
            for (int i = 1; i <= metaData.getColumnCount(); i++) {
                System.out.print(rs.getString(i) + "\t\t");
            }
            System.out.println();
        }
    }
}

