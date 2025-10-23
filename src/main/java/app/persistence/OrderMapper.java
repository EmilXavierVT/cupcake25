package app.persistence;

import app.entities.Order;
import app.entities.DiscountCode;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

public class OrderMapper {
    public void createOrder(int userId, int orderId, int discountId) {

    }
    public int getAvailableOrderid(ConnectionPool connectionPool) throws SQLException {

        String sql = "SELECT nextval('orders_id_seq')";

        int orderId = 0;
       ConnectionPool.getInstance();
        try (Connection connection = connectionPool.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                orderId = rs.getInt(1);
            }
            }
        return orderId;
    }
    public void saveOrder(int userId, LocalDate date, int orderId, int discountId) throws SQLException {
        String sql = "INSERT INTO orders (id, user_id, date, applied_discount) VALUES (?, ?, ?, ?) RETURNING id";
        Order order = null;
        try (Connection connection = ConnectionPool.getInstance().getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, orderId);
            ps.setInt(2, userId);
            ps.setDate(3, java.sql.Date.valueOf(date));
            ps.setInt(4, discountId);
            ResultSet rs = ps.executeQuery();
            if(rs.next()){
                rs.getInt("id");
                order =new Order(userId,orderId,date,discountId);
            }
        }
    }

    public DiscountCode findDiscountPercentage(String discountCode, ConnectionPool connectionPool) throws SQLException {
        DiscountCode returnDiscountCode = null;
        String sql ="SELECT discount_id, discount_code,discount FROM discount_code WHERE discount_code = ?";
        try (Connection connection = connectionPool.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql))
        {
            ps.setString(1,discountCode);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                int discountId = rs.getInt("discount_id");
                String discountCodeString = rs.getString("discount_code");
                int discountRate = rs.getInt("discount");
                returnDiscountCode = new DiscountCode(discountId,discountCodeString,discountRate);
            }
        }
        return returnDiscountCode;
    }
}

