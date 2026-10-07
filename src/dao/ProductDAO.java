package dao;

import model.Product;
import util.DBContext;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProductDAO {

    // Hàm thêm sản phẩm mới vào Database, trả về ID của sản phẩm vừa tạo
    public int insertProduct(Product product) {
        String sql = "INSERT INTO products (user_id, category_id, title, description, price, condition_status, location, status) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        int generatedId = 0;
        
        try (Connection conn = DBContext.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
             
            ps.setInt(1, product.getUserId());
            ps.setInt(2, product.getCategoryId());
            ps.setString(3, product.getTitle());
            ps.setString(4, product.getDescription());
            ps.setDouble(5, product.getPrice());
            ps.setString(6, product.getConditionStatus());
            ps.setString(7, product.getLocation());
            ps.setString(8, product.getStatus()); // 'PENDING'
            
            ps.executeUpdate();
            
            // Lấy ID tự tăng vừa được tạo ra
            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                generatedId = rs.getInt(1);
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return generatedId;
    }
}
