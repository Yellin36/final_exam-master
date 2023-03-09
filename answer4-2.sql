create table `Reservations`
(
    `reserv_id`  BIGINT NOT NULL AUTO_INCREMENT,
    `user_id`    BIGINT NOT NULL,
    `start_date` DATE   NOT NULL,
    `end_date`   DATE   NOT NULL,
    `hotel_id`   BIGINT NOT NULL,
    `room_id`    BIGINT NOT NULL,
    PRIMARY KEY (`reserv_id`),
    CONSTRAINT `fk_hotel_name` FOREIGN KEY (`hotel_id`) REFERENCES `Hotels` (`hotel_id`),
    CONSTRAINT `fk_room_name` FOREIGN KEY (`room_id`) REFERENCES `Rooms` (`room_id`)
) ENGINE = InnoDB;