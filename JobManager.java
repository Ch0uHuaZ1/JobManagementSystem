// JobManager.java
import java.sql.*;

public class JobManager {

    // 添加职业
    public static void addJob(int categoryId, String employer, int required, String notes) {
        String sql = "INSERT INTO Job (category_id, employer, required_count, notes) VALUES (?,?,?,?)";
        try (Connection conn = DBConnector.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, categoryId);
            pstmt.setString(2, employer);
            pstmt.setInt(3, required);
            pstmt.setString(4, notes);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // 求职者匹配
    public static void matchJobSeeker(int jobId, int seekerId) {
        String sql = "INSERT INTO JobMatching (job_id, seeker_id) VALUES (?,?)";
        try (Connection conn = DBConnector.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, jobId);
            pstmt.setInt(2, seekerId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // 获取职业统计（调用存储过程）
    public static void getJobStatistics() {
        String sql = "CALL GetJobStats()";
        try (Connection conn = DBConnector.getConnection();
             CallableStatement cstmt = conn.prepareCall(sql);
             ResultSet rs = cstmt.executeQuery()) {

            System.out.println("职业统计：");
            while (rs.next()) {
                System.out.printf(
                        "ID:%d 类型:%s 需求:%d 已聘:%d\n",
                        rs.getInt("job_id"),
                        rs.getString("category_name"),
                        rs.getInt("required_count"),
                        rs.getInt("hired_count")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}