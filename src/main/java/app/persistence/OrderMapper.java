package app.persistence;

import app.entities.Bottom;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

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
}

