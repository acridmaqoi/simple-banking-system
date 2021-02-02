package banking;

import java.sql.*;
import java.util.List;

public class DataSource {

    private static Connection connection;

    public static void connect(String name) {
        String url = "jdbc:sqlite:" + name;
        String sql = "CREATE TABLE IF NOT EXISTS card (" +
                "id INTEGER," +
                "number TEXT," +
                "pin TEXT," +
                "balance INTEGER DEFAULT 0)";

        try {
            Connection conn = DriverManager.getConnection(url);
            conn.createStatement().execute(sql);
            connection = conn;
        } catch (SQLException e) {
            System.out.println("Error " + e.getMessage());
        }
    }

    public static void loadData(List<Account> accounts) {
        String sql = "SELECT " +
                "id, balance, number, pin " +
                "FROM card";

        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                accounts.add(new Account(rs.getLong("id"),
                        rs.getInt("balance"),
                        rs.getString("number"),
                        rs.getString("pin")));
            }

        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public static void saveData(List<Account> accounts) {
        String sql = "INSERT INTO card(id, balance, number, pin)" +
                "VALUES(?,?,?,?)";
        /*String checkSql = "SELECT number FROM card WHERE number = ?";*/

        // reset db
        try (PreparedStatement pstmt = connection.prepareStatement("DELETE FROM card")) {
            pstmt.execute();
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }

        for (Account account : accounts) {

            /*// check if acc is already in db
            try (PreparedStatement pstmt = connection.prepareStatement(checkSql)) {
                pstmt.setString(1, account.getCard().getNumber());
                if (pstmt.executeQuery().next()) {
                    continue;
                }
            } catch (SQLException e) {
                System.out.println("Error: " + e.getMessage());
            }*/

            // reinsert updated records
            try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
                pstmt.setLong(1, account.getId());
                pstmt.setDouble(2, account.getBalance());
                pstmt.setString(3, account.getCard().getNumber());
                pstmt.setString(4, account.getCard().getPin());
                pstmt.execute();
            } catch (SQLException e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }
}
