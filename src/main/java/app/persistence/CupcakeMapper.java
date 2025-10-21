package app.persistence;

import app.entities.Bottom;
import app.entities.Icing;
import app.entities.UserDefinedCupcake;
import app.exceptions.DatabaseException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CupcakeMapper {
    private ConnectionPool connectionPool;

    private final String GET_ALL_BOTTOMS = "SELECT * FROM the_bottoms";
    private final String GET_ALL_ICINGS = "SELECT * FROM icing";



    public List<Bottom> getAllBottoms(ConnectionPool connectionPool) throws DatabaseException {
        List<Bottom> bottomList = new ArrayList<>();

        try (Connection connection = connectionPool.getConnection();
            PreparedStatement ps = connection.prepareStatement(GET_ALL_BOTTOMS)) {

                ResultSet rs = ps.executeQuery();
                while (rs.next()) {
                    int bottomId = rs.getInt("bottom_id");
                    String bottomName = rs.getString("bottom_name");
                    float bottomPrice = rs.getFloat("bottom_price");
                    bottomList.add(new Bottom(bottomId, bottomName, bottomPrice));
                }

        } catch (SQLException e) {
            throw new DatabaseException("something database", e.getMessage());
        }
        return bottomList;
    }

    public List<Icing> getAllIcings() throws SQLException {
        List<Icing> icingList = new ArrayList<>();
        try (Connection connection = connectionPool.getConnection()) {
            try (PreparedStatement ps = connection.prepareStatement(GET_ALL_ICINGS)) {
                ResultSet rs = ps.executeQuery();
                while (rs.next()) {
                    int icingId = rs.getInt("icing_id");
                    String icingName = rs.getString("icing_name");
                    float icingPrice = rs.getFloat("icing_price");
                    icingList.add(new Icing(icingId, icingName, icingPrice));
                }
            }
        }
        return icingList;
    }
    public Bottom getBottomById(int id) throws SQLException {
        Bottom bottom = null;
        try (Connection connection = connectionPool.getConnection()) {
            String sql = "SELECT * FROM the_bottoms WHERE bottom_id = ?";
            try (PreparedStatement ps = connection.prepareStatement(sql)) {
                ps.setInt(1, id);
                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    String bottomName = rs.getString("bottom_name");
                    float bottomPrice = rs.getFloat("bottom_price");
                    bottom = new Bottom(id, bottomName, bottomPrice);
                }
            }
        }
        return bottom;
    }

    public Icing getIcingById(int id) throws SQLException {
        Icing icing = null;
        try (Connection connection = connectionPool.getConnection()) {
            String sql = "SELECT * FROM icing WHERE icing_id = ?";
            try (PreparedStatement ps = connection.prepareStatement(sql)) {
                ps.setInt(1, id);
                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    String icingName = rs.getString("icing_name");
                    float icingPrice = rs.getFloat("icing_price");
                    icing = new Icing(id, icingName, icingPrice);
                }
            }
        }
        return icing;
    }
    public UserDefinedCupcake createUserDefinedCupcake(int bottom, int icing) throws SQLException {
        UserDefinedCupcake userDefinedCupcake = null;
        try (Connection connection = connectionPool.getConnection()) {
            String sql = "INSERT INTO user_defined_cupcake (bottom_id, icing_id) VALUES (?, ?) RETURNING id";
            try (PreparedStatement ps = connection.prepareStatement(sql)) {
                ps.setInt(1, bottom);
                ps.setInt(2, icing);
                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    int id = rs.getInt("id");
                    userDefinedCupcake = new UserDefinedCupcake(getBottomById(bottom), getIcingById(icing));
                }
            }
        }
        return userDefinedCupcake;
    }
}
