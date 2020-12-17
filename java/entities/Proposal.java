package entities;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import enums.EnumAction;

public class Proposal {
	
	private String id;
	
	private BigDecimal loanValue;
	
	private Integer numberOfMonthLyInstallments;

	private String eventId;
	
	private String eventSchema;
	
	private EnumAction eventAction;
	
	private LocalDateTime eventTimeStamp;
	
	private Map<String, Warranty> mapId_warranty = new HashMap<String, Warranty>();
	
	private Map<String, Proponent> mapId_proponent = new HashMap<String, Proponent>();
	
	private boolean valid = true;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public BigDecimal getLoanValue() {
		return loanValue;
	}

	public void setLoanValue(BigDecimal loanValue) {
		this.loanValue = loanValue;
	}

	public Integer getNumberOfMonthLyInstallments() {
		return numberOfMonthLyInstallments;
	}

	public void setNumberOfMonthLyInstallments(Integer numberOfMonthLyInstallments) {
		this.numberOfMonthLyInstallments = numberOfMonthLyInstallments;
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

	public Map<String, Warranty> getMapId_warranty() {
		return mapId_warranty;
	}

	public void setMapId_warranty(Map<String, Warranty> mapId_warranty) {
		this.mapId_warranty = mapId_warranty;
	}

	public Map<String, Proponent> getMapId_proponent() {
		return mapId_proponent;
	}

	public void setMapId_proponent(Map<String, Proponent> mapId_proponent) {
		this.mapId_proponent = mapId_proponent;
	}

	public boolean isValid() {
		return valid;
	}

	public void setValid(boolean valid) {
		this.valid = valid;
	}
	
}
