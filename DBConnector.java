// DBConnector.java
import java.sql.*;

public class DBConnector {
    private static final String URL = "jdbc:mysql://localhost:3306/job_management?useSSL=false&serverTimezone=UTC";
    private static final String USER = "root";
    private static final String PASS = "123456"; // 替换为你的数据库密码

    static {
        try {
            // 显式加载MySQL JDBC驱动程序
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            System.err.println("MySQL JDBC驱动程序未找到!");
            System.exit(1);
        }
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASS);
    }
}