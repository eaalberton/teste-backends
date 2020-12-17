package entities;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import enums.EnumAction;

public class Proponent {

	private String id;
	
	private String name;
	
	private Integer age;
	
	private BigDecimal monthlyIncome;
	
	private boolean main;
	
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

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getAge() {
		return age;
	}

	public void setAge(Integer age) {
		this.age = age;
	}

	public BigDecimal getMonthlyIncome() {
		return monthlyIncome;
	}

	public void setMonthlyIncome(BigDecimal monthlyIncome) {
		this.monthlyIncome = monthlyIncome;
	}

	public boolean isMain() {
		return main;
	}

	public void setMain(boolean main) {
		this.main = main;
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
