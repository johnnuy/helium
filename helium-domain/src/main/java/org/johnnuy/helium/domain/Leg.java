package org.johnnuy.helium.domain;

import java.time.Duration;
import java.time.LocalDateTime;

public class Leg {
	
	private boolean deadhead;
	private String flightNumber;

	private String departureAirport;
	private LocalDateTime departureTime;
	
	private String arrivalAirport;
	private LocalDateTime arrivalTime;
	
	private Duration block;
	private Duration timeOnGround;
	private Duration credit;
	
	private String aircraft;
	
	
	
}
