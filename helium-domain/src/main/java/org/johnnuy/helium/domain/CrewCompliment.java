package org.johnnuy.helium.domain;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.springframework.util.Assert;

public class CrewCompliment {

	private Integer captains;
	private Integer officers;
	private Integer flightAttendants;
	private Integer other;
	
	public CrewCompliment() {}
	
	public CrewCompliment(String value) {
		Assert.notNull(value, "Crew compliment cannot be null");		
		Assert.isTrue(value.matches("\\[[0-9],[0-9],[0-9],[0-9]\\]"), "Not a valid crew compliment string");
		String[] components = StringUtils.split(
				StringUtils.remove(
						StringUtils.remove(value, "["), 
						"]"), 
				",");
		captains = Integer.parseInt(components[0]);
		officers = Integer.parseInt(components[1]);
		flightAttendants = Integer.parseInt(components[2]);
		other = Integer.parseInt(components[3]);
	}
	
	
	
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
