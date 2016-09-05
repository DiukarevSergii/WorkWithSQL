package apartmentsDatabase;

import java.sql.*;

public class Main {
    static final String DB_CONNECTION = "jdbc:mysql://localhost:3306/ApartmentsDB";
    static final String DB_USER = "root";
    static final String DB_PASSWORD = "666999";

    public static void main(String[] args) {
        try (Connection connection = DriverManager.getConnection(DB_CONNECTION, DB_USER, DB_PASSWORD)) {
            try (Statement statement = connection.createStatement()) {
                statement.execute("DROP TABLE IF EXISTS Apartments");
                statement.execute("CREATE TABLE Apartments " +
                        "(id INT NOT NULL AUTO_INCREMENT PRIMARY KEY, " +
                        "district VARCHAR (10) NOT NULL," +
                        "adress VARCHAR (20) NOT NULL," +
                        "square INT(3)," +
                        "rooms INT (1)," +
                        "price INT(10))");
            }
            try (PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO Apartments " +
                    "(district, adress, square, rooms, price) VALUES (?,?,?,?,?)")) {
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

                ResultSetMetaData metaData = rs.getMetaData();
                for (int i = 1; i <= metaData.getColumnCount(); i++) {
                    System.out.print(metaData.getColumnName(i) + "\t\t");
                }
                System.out.println();

                while (rs.next()) {
                    for (int i = 1; i <= metaData.getColumnCount(); i++) {
                        System.out.print(rs.getString(i) +"\t\t");
                }
                    System.out.println();
                }

            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
