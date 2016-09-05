package apartmentsDatabase;

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
                        res.getString("db.url"), res.getString("db.user"), res.getString("db.password"));

                initDB();
                do {
                    System.out.println("\nSelect using the: ");
                    System.out.println("1: district");
                    System.out.println("2: address");
                    System.out.println("3: square");
                    System.out.println("4: number of rooms");
                    System.out.println("5: price");
                    System.out.print("-> ");

                    String s = sc.nextLine();
                    switch (s) {
                        case "1":
                            selectDistrict(sc);
                            break;
                        case "2":
                            selectAddress(sc);
                            break;
                        case "3":
                            selectSquare(sc);
                            break;
                        case "4":
                            selectRooms(sc);
                            break;
                        case "5":
                            selectPrice(sc);
                            break;
                        default:
                            return;
                    }
                } while (true);

            } finally {
                if (connection != null) {
                    connection.close();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void selectDistrict(Scanner scanner) throws SQLException {
        System.out.println("Enter name of district:");
        String nameOfDistrict = scanner.nextLine();
        try (PreparedStatement preparedStatement = connection.prepareStatement(
                "SELECT * FROM Apartments WHERE district =" + "'" + nameOfDistrict + "'");
             ResultSet rs = preparedStatement.executeQuery()) {
            resultSet(rs);
        }
    }

    private static void selectAddress(Scanner scanner) throws SQLException{
        System.out.println("Enter address:");
        String address = scanner.nextLine();
        try (PreparedStatement preparedStatement = connection.prepareStatement(
                "SELECT * FROM Apartments WHERE address =" + "'" + address + "'");
             ResultSet rs = preparedStatement.executeQuery()) {
            resultSet(rs);
        }
    }

    private static void selectSquare(Scanner scanner) throws SQLException{
        System.out.println("Enter square:");
        int square = Integer.parseInt(scanner.nextLine());
        try (PreparedStatement preparedStatement = connection.prepareStatement(
                "SELECT * FROM Apartments WHERE square =" + "'" + square + "'");
             ResultSet rs = preparedStatement.executeQuery()) {
            resultSet(rs);
        }
    }

    private static void selectRooms(Scanner scanner) throws SQLException{
        System.out.println("Enter number of rooms:");
        int number = Integer.parseInt(scanner.nextLine());
        System.out.println("number of rooms:" + number);
        try (PreparedStatement preparedStatement = connection.prepareStatement(
                "SELECT * FROM Apartments WHERE rooms =" + "'" + number + "'");
             ResultSet rs = preparedStatement.executeQuery()) {
            resultSet(rs);
        }
    }


    private static void selectPrice(Scanner scanner) throws SQLException{
        System.out.println("Enter price per apartment:");
        int price = Integer.parseInt(scanner.nextLine());
        try (PreparedStatement preparedStatement = connection.prepareStatement(
                "SELECT * FROM Apartments WHERE price =" + "'" + price + "'");
             ResultSet rs = preparedStatement.executeQuery()) {
            resultSet(rs);
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

    private static void initDB()  throws SQLException{
        try (Statement statement = connection.createStatement()) {
            statement.execute("DROP TABLE IF EXISTS Apartments");
            statement.execute("CREATE TABLE Apartments " +
                    "(id INT NOT NULL AUTO_INCREMENT PRIMARY KEY, " +
                    "district VARCHAR (10) NOT NULL," +
                    "address VARCHAR (20) NOT NULL," +
                    "square INT(3)," +
                    "rooms INT (1)," +
                    "price INT(10))");
//            }
            try (PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO Apartments " +
                    "(district, address, square, rooms, price) VALUES (?,?,?,?,?)")) {
                preparedStatement.setString(1, "Pechersk");
                preparedStatement.setString(2, "Ivana K.");
                preparedStatement.setInt(3, 26);
                preparedStatement.setInt(4, 2);
                preparedStatement.setInt(5, 200_000);
                preparedStatement.executeUpdate();

                preparedStatement.setString(1, "Podol");
                preparedStatement.setString(2, "Sagaidachnogo");
                preparedStatement.setInt(3, 36);
                preparedStatement.setInt(4, 3);
                preparedStatement.setInt(5, 250_000);
                preparedStatement.executeUpdate();
            }
            try (PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM Apartments");
                 ResultSet rs = preparedStatement.executeQuery()) {

                resultSet(rs);
            }
        }
    }
}
