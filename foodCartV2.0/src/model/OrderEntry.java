package model;

public class OrderEntry {
	private Integer customerID;
	private Order[] orders;
	private String  status;
	private String  processedByThread;
	private Integer numberofItems;
	
	public OrderEntry(Integer customerID, Order[] orders ){
		this.customerID = customerID;
		this.orders = orders;
		status = OrderStatus.NEW.status;
		this.numberofItems = 0;
		for(int i = 0; i < orders.length; i++){
			numberofItems+= orders[i].getQuantity();
		}
	}
	public void setClaim(String threadName){
		this.processedByThread = threadName;
		this.status = OrderStatus.PREPARING.status;
	}
	
	public void setReady(){
		this.status = OrderStatus.READY.status;
	}
	
	public void setComplete(){
		this.status = OrderStatus.COMPLETED.status;
	}
	
	
		
}

