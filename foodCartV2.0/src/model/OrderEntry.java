package model;

public class OrderEntry {
	private Order[] orders;
	private String  status;
	private String  processedByThread;
	private Integer numberofItems;
	
	public OrderEntry(Order[] orders ){
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
		System.out.println("Customer orders Claimed from OrderQueue, Customer: " + this.orders[0].getCustomer() + " Thread: " + threadName);
	}
	
	public void setReady(){
		this.status = OrderStatus.READY.status;
		System.out.println("Status changed to Ready in OrderQueue for Customer: " + this.orders[0].getCustomer());
		
	}
	
	public void setComplete(){
		this.status = OrderStatus.COMPLETED.status;
		System.out.println("Status changed to Completed in OrderQueue for Customer: " + this.orders[0].getCustomer());
	}
	
	public String getStatus(){
		return this.status;
	}
		
}

