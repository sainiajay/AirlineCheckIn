DROP TABLE IF EXISTS SeatBooking;

CREATE TABLE `SeatBooking` (
    `seatId` int NOT NULL AUTO_INCREMENT PRIMARY KEY,
    `customerId` varchar(3) NOT NULL DEFAULT 'XXX'
) ENGINE=InnoDB DEFAULT CHARSET=UTF8;

DROP PROCEDURE IF EXISTS reset_bookings;

DELIMITER ;;
CREATE PROCEDURE reset_bookings()
BEGIN

  DECLARE seatId INT DEFAULT 120;
  WHILE seatId > 0 DO
    INSERT SeatBooking(customerId) VALUES (DEFAULT);
    SET seatId = seatId - 1;
  END WHILE;
END;;
DELIMITER ;

CALL reset_bookings();
