package app.persistence;

import app.entities.Bottom;
import app.entities.Order;

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
    public Order saveOrder(int userId, LocalDate date, int orderId, int discountId) throws SQLException {
        String sql = "INSERT INTO orders (user_id, order_id, date, discount_id) VALUES (?, ?, ?) RETURNING order_id";
        Order order = null;
        try (Connection connection = ConnectionPool.getInstance().getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ps.setInt(2, orderId);
            ps.setInt(3, discountId);
            ResultSet rs = ps.executeQuery();
            if(rs.next()){
                order =new Order(userId,orderId,date,discountId);
            }
        }
        return order;
    }
}

