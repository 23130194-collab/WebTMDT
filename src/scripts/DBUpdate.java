package scripts;
import util.DBContext;
import java.sql.Connection;
import java.sql.Statement;

public class DBUpdate {
    public static void main(String[] args) {
        try (Connection conn = DBContext.getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.executeUpdate("ALTER TABLE products ADD COLUMN delivery_method INT DEFAULT 1");
            System.out.println("Success");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}