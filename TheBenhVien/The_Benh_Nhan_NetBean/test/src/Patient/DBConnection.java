/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Patient;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Base64;


public class DBConnection {

    private final static String url = "jdbc:mysql://localhost:3306/javacard?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";
    private final static String user = "root";
    private final static String password = "";

    public static Connection connect() {
        Connection conn = null;
        try {
            // Tải driver MySQL
            Class.forName("com.mysql.cj.jdbc.Driver");
            // Kết nối đến cơ sở dữ liệu
            conn = DriverManager.getConnection(url, user, password);
            System.out.println("Ket noi thanh cong!");
        } catch (ClassNotFoundException e) {
            System.err.println("Loi: Khong tim thay driver MySQL: " + e.getMessage());
        } catch (SQLException e) {
            System.err.println("Loi co so du lieu " + e.getMessage());
        }
        return conn;
    }


    public static String getPublicKey(String id) {
        String str_key = null;
        String sql = "SELECT public_key FROM users WHERE id = ?";
        try (Connection conn = DBConnection.connect(); PreparedStatement ps = conn.prepareStatement(sql)) {

            if (conn == null || conn.isClosed()) {
                System.err.println("Không thể kết nối cơ sở dữ liệu!");
                return null;
            }

            System.out.println("Truy vấn khóa công khai với ID: " + id); // Log ID
            ps.setString(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    str_key = rs.getString("public_key");
                    System.out.println("Khóa công khai tìm thấy: " + str_key);
                } else {
                    System.err.println("Không tìm thấy khóa công khai cho ID: " + id);
                }
            }
        } catch (SQLException e) {
            System.err.println("Lỗi truy vấn cơ sở dữ liệu: " + e.getMessage());
        }

        if (str_key == null) {
            System.err.println("Khóa công khai trả về null. Vui lòng kiểm tra ID: " + id);
        }
        return str_key;
    }
    public static String getPhoneFromDatabase(String userId) {
    String phone = null; // Biến lưu số điện thoại
    String sql = "SELECT phone FROM users WHERE id = ?"; // Câu lệnh SQL truy vấn số điện thoại
    try (Connection conn = DBConnection.connect(); PreparedStatement ps = conn.prepareStatement(sql)) {

        if (conn == null || conn.isClosed()) {
            System.err.println("Không thể kết nối cơ sở dữ liệu!");
            return null;
        }

        ps.setString(1, userId); // Gắn giá trị userId vào câu truy vấn
        try (ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                phone = rs.getString("phone"); // Lấy số điện thoại từ cột "phone"
                System.out.println("Số điện thoại tìm thấy: " + phone);
            } else {
                System.err.println("Không tìm thấy số điện thoại cho ID: " + userId);
            }
        }
    } catch (SQLException e) {
        System.err.println("Lỗi truy vấn cơ sở dữ liệu: " + e.getMessage());
    }

    if (phone == null) {
        System.err.println("Số điện thoại trả về null. Vui lòng kiểm tra ID: " + userId);
    }
    return phone;
}
public static boolean isPhoneExist(String phone) {
    String sql = "SELECT COUNT(*) FROM users WHERE phone = ?";
    try (Connection conn = DBConnection.connect(); PreparedStatement ps = conn.prepareStatement(sql)) {
        if (conn == null || conn.isClosed()) {
            System.err.println("Không thể kết nối cơ sở dữ liệu!");
            return false;
        }

        ps.setString(1, phone); // Gắn giá trị số điện thoại vào câu truy vấn

        try (ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                int count = rs.getInt(1); // Đếm số bản ghi có số điện thoại trùng
                return count > 0; // Nếu số bản ghi > 0, có số điện thoại trùng
            }
        }
    } catch (SQLException e) {
        System.err.println("Lỗi truy vấn cơ sở dữ liệu: " + e.getMessage());
    }
    return false; // Không tìm thấy số điện thoại
}

public static boolean updateUserInfo(String id, String name, String address, String dob, String insurance, String phone) {
    Connection conn = null;
    PreparedStatement pstmt = null;
    String sql = "UPDATE Users SET name = ?, address = ?, dob = ?, insurance_number = ?, phone = ? WHERE id = ?";

    try {
        conn = DBConnection.connect(); // Hàm lấy kết nối tới DB
        pstmt = conn.prepareStatement(sql);

        // Gán giá trị cho các tham số
        pstmt.setString(1, name);
        pstmt.setString(2, address);
        pstmt.setString(3, dob);
        pstmt.setString(4, insurance);
        pstmt.setString(5, phone);
        pstmt.setString(6, id);

        // Thực thi câu lệnh
        int rowsUpdated = pstmt.executeUpdate();

        return rowsUpdated > 0; // Trả về true nếu có ít nhất một dòng được cập nhật
    } catch (SQLException e) {
        e.printStackTrace();
        return false; // Trả về false nếu có lỗi xảy ra
    } finally {
        // Đóng kết nối
        try {
            if (pstmt != null) pstmt.close();
            if (conn != null) conn.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
}

}
