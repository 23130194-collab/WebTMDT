package controller;

import dao.ProductDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.Product;

import java.io.IOException;
import java.util.List;

@WebServlet(name = "SearchController", urlPatterns = {"/search", "/search/suggestions"})
public class SearchController extends HttpServlet {

    private final ProductDAO productDAO = new ProductDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        String keyword = request.getParameter("q");
        keyword = keyword == null ? "" : keyword.trim();

        if ("/search/suggestions".equals(request.getServletPath())) {
            writeSuggestions(keyword, response);
            return;
        }

        List<Product> products = keyword.isEmpty()
                ? productDAO.getRecentProducts()
                : productDAO.searchProducts(keyword);
        request.setAttribute("products", products);
        request.setAttribute("searchKeyword", keyword);
        request.getRequestDispatcher("/index.jsp").forward(request, response);
    }

    private void writeSuggestions(String keyword, HttpServletResponse response) throws IOException {
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json; charset=UTF-8");
        if (keyword.length() < 2) {
            response.getWriter().write("[]");
            return;
        }

        List<Product> products = productDAO.searchProducts(keyword, 5);
        StringBuilder json = new StringBuilder("[");
        for (int i = 0; i < products.size(); i++) {
            Product product = products.get(i);
            if (i > 0) json.append(',');
            json.append("{\"id\":").append(product.getId())
                    .append(",\"title\":\"").append(escapeJson(product.getTitle()))
                    .append("\",\"price\":").append(product.getPrice())
                    .append(",\"imageUrl\":\"").append(escapeJson(product.getPrimaryImage()))
                    .append("\"}");
        }
        json.append(']');
        response.getWriter().write(json.toString());
    }

    private String escapeJson(String value) {
        if (value == null) return "";
        StringBuilder escaped = new StringBuilder(value.length());
        for (int i = 0; i < value.length(); i++) {
            char ch = value.charAt(i);
            switch (ch) {
                case '"': escaped.append("\\\""); break;
                case '\\': escaped.append("\\\\"); break;
                case '\b': escaped.append("\\b"); break;
                case '\f': escaped.append("\\f"); break;
                case '\n': escaped.append("\\n"); break;
                case '\r': escaped.append("\\r"); break;
                case '\t': escaped.append("\\t"); break;
                default:
                    if (ch < 0x20) {
                        escaped.append(String.format("\\u%04x", (int) ch));
                    } else {
                        escaped.append(ch);
                    }
            }
        }
        return escaped.toString();
    }
}
