/**
 * 数据库连接器类
 * 
 * 该类负责管理与MySQL数据库的连接，提供统一的数据库访问接口。
 * 主要功能：
 * 1. 配置数据库连接参数（URL、用户名、密码）
 * 2. 加载MySQL JDBC驱动程序
 * 3. 提供静态方法获取数据库连接
 * 4. 处理数据库连接异常
 * 
 * 设计模式：
 * - 单例模式：使用静态方法提供连接
 * - 工厂模式：统一创建数据库连接
 * 
 * 安全考虑：
 * - 数据库密码应该通过配置文件或环境变量管理
 * - 生产环境中应使用连接池提高性能
 * 
 * @author 系统开发者
 * @version 1.0
 * @since 2024
 */

// 导入必要的Java SQL包
import java.sql.Connection;    // 导入数据库连接接口
import java.sql.DriverManager; // 导入驱动管理器类
import java.sql.SQLException;  // 导入SQL异常类

/**
 * 数据库连接器类
 * 提供静态方法用于获取MySQL数据库连接
 */
class DBConnector {
    
    // ==================== 数据库连接配置常量 ====================
    
    /**
     * 数据库连接URL
     * 格式：jdbc:mysql://主机地址:端口号/数据库名?参数1=值1&参数2=值2
     * 
     * 参数说明：
     * - localhost:3306 - 数据库服务器地址和端口
     * - job_management - 数据库名称
     * - useSSL=false - 禁用SSL连接（开发环境）
     * - serverTimezone=UTC - 设置服务器时区为UTC
     */
    private static final String URL = "jdbc:mysql://localhost:3306/job_management?useSSL=false&serverTimezone=UTC";
    
    /**
     * 数据库用户名
     * 用于连接MySQL数据库的用户名
     */
    private static final String USER = "root";
    
    /**
     * 数据库密码
     * 用于连接MySQL数据库的密码
     * 
     * 注意：生产环境中应该：
     * 1. 使用更复杂的密码
     * 2. 将密码存储在配置文件中
     * 3. 使用环境变量管理敏感信息
     */
    private static final String PASS = "123456";

    // ==================== 静态初始化块 ====================
    
    /**
     * 静态初始化块
     * 
     * 在类加载时自动执行，用于：
     * 1. 加载MySQL JDBC驱动程序
     * 2. 验证驱动程序是否可用
     * 3. 处理驱动程序加载异常
     * 
     * 执行时机：当类第一次被使用时（如调用getConnection方法时）
     */
    static {
        try {
            // 显式加载MySQL JDBC驱动程序
            // 使用Class.forName()方法动态加载驱动程序类
            // 驱动程序类名：com.mysql.cj.jdbc.Driver
            Class.forName("com.mysql.cj.jdbc.Driver");
            
            // 如果驱动程序加载成功，会输出调试信息（可选）
            System.out.println("MySQL JDBC驱动程序加载成功！");
            
        } catch (ClassNotFoundException e) {
            // 捕获类未找到异常（驱动程序不存在）
            e.printStackTrace();  // 打印异常堆栈信息到控制台
            
            // 输出用户友好的错误信息
            System.err.println("MySQL JDBC驱动程序未找到!");
            System.err.println("请确保：");
            System.err.println("1. mysql-connector-j-9.3.0.jar文件在classpath中");
            System.err.println("2. 驱动程序版本与MySQL服务器版本兼容");
            System.err.println("3. 项目依赖配置正确");
            
            // 程序无法继续运行，强制退出
            System.exit(1);
        }
    }

    // ==================== 公共方法 ====================
    
    /**
     * 获取数据库连接
     * 
     * 该方法使用配置的URL、用户名和密码创建到MySQL数据库的连接。
     * 
     * 方法特点：
     * - 静态方法：可以直接通过类名调用，无需创建实例
     * - 返回Connection对象：用于执行SQL语句
     * - 抛出SQLException：调用者需要处理数据库异常
     * 
     * 使用示例：
     * <pre>
     * try (Connection conn = DBConnector.getConnection()) {
     *     // 使用连接执行SQL操作
     *     Statement stmt = conn.createStatement();
     *     ResultSet rs = stmt.executeQuery("SELECT * FROM JobCategory");
     *     // 处理结果集...
     * } catch (SQLException e) {
     *     // 处理数据库异常
     *     e.printStackTrace();
     * }
     * </pre>
     * 
     * 注意事项：
     * 1. 调用者负责关闭连接（使用try-with-resources语句）
     * 2. 该方法每次调用都会创建新的连接
     * 3. 生产环境建议使用连接池提高性能
     * 
     * @return 数据库连接对象
     * @throws SQLException 如果数据库连接失败
     */
    public static Connection getConnection() throws SQLException {
        // 使用DriverManager创建数据库连接
        // 传入配置的URL、用户名和密码
        return DriverManager.getConnection(URL, USER, PASS);
    }
}
