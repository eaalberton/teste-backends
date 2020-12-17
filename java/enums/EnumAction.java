package enums;

public enum EnumAction {
	
	CREATED("created"),
	UPDATED("updated"),
	DELETED("deleted"),
	ADDED("added"),
	REMOVED("removed")
	;
	
	private String actionName;
	
	private EnumAction(String actionName) {
		this.actionName = actionName;
	}

	public String getActionName() {
		return actionName;
	}

}
