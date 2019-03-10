package model;

public enum OrderStatus {
	NEW("New"),
	PREPARING("Preparing"),
	READY("Ready"),
	PICKEDUP("Delivered"),
	COMPLETED("Completed");
	
	public final String status;
	OrderStatus(String status){
		this.status = status;
	}
}
