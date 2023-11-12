package application;

import java.io.Serializable;
import java.util.ArrayList;

public class Client implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 983889814512600641L;
	private String username, password;
	private ArrayList<Order> buyOrders;
	private ArrayList<Order> sellOrders;
	private ArrayList<Item> items;
	public Client(String username, String password) {
		this.username=username;
		this.password=password;
		items = new ArrayList<Item>();
		buyOrders = new ArrayList<Order>();
		sellOrders = new ArrayList<Order>();
	}
	public ArrayList<Order> getOrders(){
		return buyOrders;
	}
	public ArrayList<Order> getSellOrders () {
		return sellOrders;
	}
	public ArrayList<Item> getItems(){
		return items;
	}
	public String getName() {
		 return username;
	}
}
