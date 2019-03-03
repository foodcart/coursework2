package model;

import java.util.Observable;
import java.util.logging.Level;

import controller.Manager;

public class OrderEntry extends Observable{
	private Order[] orders;
	private String  status;
	private String  processedByThread;
	private Integer numberofItems;
	private boolean isReady;
	
	public OrderEntry(Order[] orders ){
		this.orders = orders;
		status = OrderStatus.NEW.status;
		this.numberofItems = 0;
		for(int i = 0; i < orders.length; i++){
			numberofItems+= orders[i].getQuantity();
		}
		isReady = false;
	}
	
	
	public synchronized void setClaim(String threadName){
		this.processedByThread = threadName;
		this.status = OrderStatus.PREPARING.status;
		//System.out.println("Customer orders Claimed from OrderQueue, Customer: " + getCustomerID() + " Thread: " + threadName);
	}
	
	public synchronized void setReady(){
		this.status = OrderStatus.READY.status;
		isReady = true;
		System.out.println("Status changed to Ready in OrderQueue for Customer: " + getCustomerID());
		
	}
	
	public synchronized void setPickedUp(){
		this.status = OrderStatus.PICKEDUP.status;
	}
	
	public void setComplete(){
		this.status = OrderStatus.COMPLETED.status;
		System.out.println("Status changed to Completed in OrderQueue for Customer: " + getCustomerID());
	}
	
	public String getStatus(){
		return this.status;
	}
	
	public Integer getCustomerID(){
		return this.orders[orders.length-1].getCustomer();
	}
	
	public Integer getItemCount(){
		return this.numberofItems;
	}
	
	public String getProcessingThread(){
		return this.processedByThread;
	}
	
	public synchronized void isReady(){
		while(!isReady){
			try {
				Manager.getLogger().log(Level.FINE, "I am put to WAIT State");
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		setChanged();
		notifyObservers();
		
	} 
}

