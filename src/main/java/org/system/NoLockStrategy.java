package org.system;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class NoLockStrategy implements AllocationStrategy {
    private static final String query = "UPDATE SeatBooking SET customerId = ? WHERE customerId = 'XXX' limit 1";

    @Override
    public void allocate(DataSource dataSource, String customerId) {
        try (Connection dbConnection = dataSource.getConnection()) {
            dbConnection.setAutoCommit(false);

            PreparedStatement preparedStmt = dbConnection.prepareStatement(query);
            preparedStmt.setString (1, customerId);
            preparedStmt.executeUpdate();
            dbConnection.commit();
        }
        catch (SQLException exception) {
            System.err.println(exception);
        }
    }
}
