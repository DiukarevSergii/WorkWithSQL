import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.sql.*;
import java.util.ResourceBundle;

import static org.junit.Assert.*;

public class Tests {
    private static ResourceBundle res = ResourceBundle.getBundle("db");
    private Connection conn;

    @Before
    public void prepare() {
        try {
            conn = DriverManager.getConnection(
                    res.getString("db.url"), res.getString("db.user"), res.getString("db.password"));
        } catch (SQLException e) {
            assertNull(e);
        }
    }

    @After
    public void finish() {
        try {
            conn.close();
        } catch (SQLException e) {
            ;
        }
    }

//    @Test
//        public void testCount() throws SQLException {
//            try (Statement st = conn.createStatement()) {
//                try (ResultSet rs = st.executeQuery("SELECT COUNT(*) FROM test")) {
//                    assertTrue(rs.next());
//                    assertEquals(rs.getInt(1), 100);
//                }
//            }
//        }
//
//        @Test
//        public void checkData() throws SQLException {
//            try (Statement st = conn.createStatement()) {
//                try (ResultSet rs = st.executeQuery("SELECT * FROM test")) {
//                    int i = 0;
//                    while (rs.next()) {
//                        assertEquals(rs.getString(1), "SomeText" + i);
//                        assertEquals(rs.getInt(2), i);
//                        i++;
//                    }
//                }
//            }
//    }
}
