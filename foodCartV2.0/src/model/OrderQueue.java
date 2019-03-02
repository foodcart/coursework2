package model;

import java.util.concurrent.ConcurrentSkipListMap;


public class OrderQueue {
	private ConcurrentSkipListMap<Integer, OrderEntry> orderQueue;

	OrderQueue(){
		orderQueue = new ConcurrentSkipListMap<Integer, OrderEntry>();
	}
	public void add(){
		
	}
}
