package application;

import java.io.Serializable;
import java.util.ArrayList;

public class SavedInfo implements Serializable{
	private static final long serialVersionUID = 1L;
	private ArrayList<Item> items;
	public SavedInfo () {
		items = new ArrayList<Item>();
	}
	public ArrayList<Item> getItems(){
		return items;
	}
	public void addThing(Item x) {
		items.add(x);
	}
	
}
