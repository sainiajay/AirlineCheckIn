package org.system;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Allocator {
    private static final String SEAT_BOOKING_QUERY = "SELECT * FROM SeatBooking";
//    private final Connection dbConnection;
    private final AllocationStrategy strategy;
    private final DataSource dataSource;

    public Allocator(DataSource dataSource, AllocationStrategy strategy) {
        this.dataSource = dataSource;
        this.strategy = strategy;
    }

    public void allocateFor(String userId) {
        strategy.allocate(dataSource, userId);
    }

    public void printSeatMap() {
        try (Connection dbConnection = dataSource.getConnection();
             PreparedStatement seatBookingStmt = dbConnection.prepareStatement(SEAT_BOOKING_QUERY)) {
            ResultSet results = seatBookingStmt.executeQuery();
            while(results.next()) {
                int seatId = results.getInt("seatId");
                String customerId = results.getString ("customerId");
                System.out.print(String.format("%s ", customerId));
                if(seatId % 20 == 0) {
                    System.out.println();
                    if(seatId % 60 == 0) {
                        System.out.println();
                    }
                }
            }
        }
        catch (SQLException exception) {
            System.err.println(exception);
        }
    }
}
