package com.flight;

import org.apache.logging.log4j.LogManager;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.apache.logging.log4j.Logger;

@SpringBootApplication
public class FlightReservationSystemApplication {
	private static final Logger logger = LogManager.getLogger(FlightReservationSystemApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(FlightReservationSystemApplication.class, args);

		logger.info("Application Started Hurray!");
	}

}
