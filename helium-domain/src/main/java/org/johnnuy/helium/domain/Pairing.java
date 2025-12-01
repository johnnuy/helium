package org.johnnuy.helium.domain;

import java.time.Duration;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class Pairing {

	private Integer tripNumber;
	private String pairingId;
	private CrewCompliment crewCompliment;
	
	private Duration timeAwayFromBase;
	private Duration creditTime;
	private Double perdiem;
	
	public Integer getTripNumber() {
		return tripNumber;
	}
	
	public Pairing setTripNumber(Integer tripNumber) {
		this.tripNumber = tripNumber;
		return this;
	}
	
	public String getPairingId() {
		return pairingId;
	}
	
	public Pairing setPairingId(String pairingId) {
		this.pairingId = pairingId;
		return this;
	}
	
	public CrewCompliment getCrewCompliment() {
		return crewCompliment;
	}
	
	public Pairing setCrewCompliment(CrewCompliment crewCompliment) {
		this.crewCompliment = crewCompliment;
		return this;
	}
	
	public Duration getTimeAwayFromBase() {
		return timeAwayFromBase;
	}
	
	public Pairing setTimeAwayFromBase(Duration timeAwayFromBase) {
		this.timeAwayFromBase = timeAwayFromBase;
		return this;
	}
	
	public Duration getCreditTime() {
		return creditTime;
	}
	
	public Pairing setCreditTime(Duration creditTime) {
		this.creditTime = creditTime;
		return this;
	}
	
	public Double getPerdiem() {
		return perdiem;
	}
	
	public Pairing setPerdiem(Double perdiem) {
		this.perdiem = perdiem;
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
		return ToStringBuilder.reflectionToString(ToStringStyle.JSON_STYLE);
	}
}
