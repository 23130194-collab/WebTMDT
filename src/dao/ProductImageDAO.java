package dao;

import model.ProductImage;
import util.DBContext;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

public class ProductImageDAO {

    // Hàm thêm danh sách hình ảnh vào Database
    public void insertImages(List<ProductImage> images) {
        String sql = "INSERT INTO product_images (product_id, image_url, is_primary) VALUES (?, ?, ?)";
        
        try (Connection conn = DBContext.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
             
            for (ProductImage img : images) {
                ps.setInt(1, img.getProductId());
                ps.setString(2, img.getImageUrl());
                ps.setBoolean(3, img.isPrimary());
                ps.addBatch();
            }
            
            ps.executeBatch();
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
