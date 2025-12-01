package org.johnnuy.helium.domain;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

public class CrewCompliment {

	private Integer captains;
	private Integer officers;
	private Integer flightAttendants;
	private Integer other;
	
	public Integer getCaptains() {
		return captains;
	}
	
	public CrewCompliment setCaptains(Integer captains) {
		this.captains = captains;
		return this;
	}
	
	public Integer getOfficers() {
		return officers;
	}
	
	public CrewCompliment setOfficers(Integer officers) {
		this.officers = officers;
		return this;
	}
	
	public Integer getFlightAttendants() {
		return flightAttendants;
	}
	
	public CrewCompliment setFlightAttendants(Integer flightAttendants) {
		this.flightAttendants = flightAttendants;
		return this;
	}
	
	public Integer getOther() {
		return other;
	}
	
	public CrewCompliment setOther(Integer other) {
		this.other = other;
		return this;
	}
	
	@Override
	public int hashCode() {
		return HashCodeBuilder.reflectionHashCode(this, false);
	}
	
	@Override
	public boolean equals(Object obj) {
		return EqualsBuilder.reflectionEquals(this, obj, false);
	}
	
	@Override
	public String toString() {
		return "[" + captains + "," + officers + "," + flightAttendants + "," + other + "]";
	}
	
}
