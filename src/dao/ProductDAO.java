package dao;

import model.Product;
import util.DBContext;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProductDAO {

    // Hàm thêm sản phẩm mới vào Database, trả về ID của sản phẩm vừa tạo
    public int insertProduct(Product product) {
        String sql = "INSERT INTO products (user_id, category_id, title, description, price, condition_status, location, status, delivery_method) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
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
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    generatedId = rs.getInt(1);
                }
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return generatedId;
    }
    // Hàm lấy danh sách tất cả sản phẩm mới nhất (kèm ảnh bìa) để hiển thị lên trang chủ
    public List<Product> getRecentProducts() {
        String sql = "SELECT p.*, pi.image_url FROM products p " +
                     "LEFT JOIN product_images pi ON p.id = pi.product_id AND pi.is_primary = 1 " +
                     "ORDER BY p.id DESC"; // Lấy tin mới nhất
        return queryProducts(sql, null);
    }

    // Tìm trên tiêu đề và mô tả để từ khóa có thể khớp với nội dung tin đăng.
    public List<Product> searchProducts(String keyword) {
        return searchProducts(keyword, 0);
    }

    public List<Product> searchProducts(String keyword, int limit) {
        String sql = "SELECT p.*, pi.image_url FROM products p " +
                     "LEFT JOIN product_images pi ON p.id = pi.product_id AND pi.is_primary = 1 " +
                     "WHERE p.title LIKE ? OR p.description LIKE ? " +
                     "ORDER BY p.id DESC" + (limit > 0 ? " LIMIT ?" : "");
        String pattern = "%" + keyword.trim() + "%";
        return queryProducts(sql, pattern, limit);
    }

    public Product getProductById(int id) {
        String sql = "SELECT * FROM products WHERE id = ?";
        try (Connection conn = DBContext.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Product product = new Product();
                    product.setId(rs.getInt("id"));
                    product.setUserId(rs.getInt("user_id"));
                    product.setCategoryId(rs.getInt("category_id"));
                    product.setTitle(rs.getString("title"));
                    product.setDescription(rs.getString("description"));
                    product.setPrice(rs.getDouble("price"));
                    product.setConditionStatus(rs.getString("condition_status"));
                    product.setLocation(rs.getString("location"));
                    product.setStatus(rs.getString("status"));
                    product.setCreatedAt(rs.getTimestamp("created_at"));
                    return product;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    private List<Product> queryProducts(String sql, String searchPattern) {
        return queryProducts(sql, searchPattern, 0);
    }

    private List<Product> queryProducts(String sql, String searchPattern, int limit) {
        List<Product> list = new ArrayList<>();
        try (Connection conn = DBContext.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            if (searchPattern != null) {
                ps.setString(1, searchPattern);
                ps.setString(2, searchPattern);
                if (limit > 0) {
                    ps.setInt(3, limit);
                }
            }
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Product p = new Product();
                    p.setId(rs.getInt("id"));
                    p.setUserId(rs.getInt("user_id"));
                    p.setCategoryId(rs.getInt("category_id"));
                    p.setTitle(rs.getString("title"));
                    p.setDescription(rs.getString("description"));
                    p.setPrice(rs.getDouble("price"));
                    p.setConditionStatus(rs.getString("condition_status"));
                    p.setLocation(rs.getString("location"));
                    p.setStatus(rs.getString("status"));
                    p.setCreatedAt(rs.getTimestamp("created_at"));
                    p.setPrimaryImage(rs.getString("image_url"));
                    list.add(p);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
}
