package entities;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import enums.EnumAction;

public class Warranty {
	
	private String id;
	
	private BigDecimal value;
	
	private String province;
	
	private String proposalId;
	
	private String eventId;
	
	private String eventSchema;
	
	private EnumAction eventAction;
	
	private LocalDateTime eventTimeStamp;
	
	private boolean valid = true;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public BigDecimal getValue() {
		return value;
	}

	public void setValue(BigDecimal value) {
		this.value = value;
	}

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public String getProposalId() {
		return proposalId;
	}

	public void setProposalId(String proposalId) {
		this.proposalId = proposalId;
	}

	public String getEventId() {
		return eventId;
	}

	public void setEventId(String eventId) {
		this.eventId = eventId;
	}

	public String getEventSchema() {
		return eventSchema;
	}

	public void setEventSchema(String eventSchema) {
		this.eventSchema = eventSchema;
	}

	public EnumAction getEventAction() {
		return eventAction;
	}

	public void setEventAction(EnumAction eventAction) {
		this.eventAction = eventAction;
	}

	public LocalDateTime getEventTimeStamp() {
		return eventTimeStamp;
	}

	public void setEventTimeStamp(LocalDateTime eventTimeStamp) {
		this.eventTimeStamp = eventTimeStamp;
	}

	public boolean isValid() {
		return valid;
	}

	public void setValid(boolean valid) {
		this.valid = valid;
	} 
	
}
