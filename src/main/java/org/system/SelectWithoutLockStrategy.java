package org.system;

import javax.sql.DataSource;
import java.sql.*;

public class SelectWithoutLockStrategy implements AllocationStrategy {
    private static final String selectQuery =
            "SELECT seatId FROM SeatBooking WHERE customerId = 'XXX' limit 1";

    private static final String updateQuery =
            "UPDATE SeatBooking SET customerId = ? WHERE seatId = ?";

    @Override
    public void allocate(DataSource dataSource, String customerId) {
        try (Connection dbConnection = dataSource.getConnection()) {
            dbConnection.setAutoCommit(false);
            PreparedStatement queryStmt = dbConnection.prepareStatement(selectQuery);
            ResultSet result = queryStmt.executeQuery();
            if(result.next()) {
                int seatId = result.getInt("seatId");
                PreparedStatement updateStmt = dbConnection.prepareStatement(updateQuery);
                updateStmt.setString(1, customerId);
                updateStmt.setInt(2, seatId);
                updateStmt.execute();
                dbConnection.commit();
            }
            else {
                System.out.println("No seat found for " + customerId);
            }
        }
        catch (SQLException exception) {
            System.err.println(exception);
        }
    }
}
