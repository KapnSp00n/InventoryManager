package application;

import java.util.HashMap;
import java.util.Scanner;
import java.util.TreeMap;
import java.io.*;
import java.text.NumberFormat;

public class MyThread implements Runnable {
	private String fileName;
	private TreeMap<String, Integer> totalItems;
	private HashMap<String, Double> itemsPrice;
	private TreeMap<Integer, String> orderReport;

	public MyThread(String fileName, TreeMap<Integer, String> orderReport, 
			TreeMap<String, Integer> totalItems,
			HashMap<String, Double> itemsPrice) {
		this.fileName = fileName;
		this.totalItems = totalItems;
		this.itemsPrice = itemsPrice;
		this.orderReport = orderReport;
	}

	@Override
	public void run() {
		TreeMap<String, Integer> totItemsPerOrder = new TreeMap<String, Integer>();
		StringBuilder answer = new StringBuilder("");
		try {
			Scanner sc = new Scanner(new BufferedReader(new FileReader(fileName)));
			sc.next();
			int id = sc.nextInt();
			answer.append("----- Order details for client with Id: " + id + 
					" -----");
			OrdersProcessor.fileDataToTreeMap(sc, totItemsPerOrder);
			sc.close();
			double orderTotal = 0;
			for (String item : totItemsPerOrder.keySet()) {
				int numItems = totItemsPerOrder.get(item);
				double price = itemsPrice.get(item);
				orderTotal += numItems * price;
				answer.append("\nItem's name: " + item + ", Cost per item: "
						+ NumberFormat.getCurrencyInstance().format(price) 
						+ ", Quantity: " + numItems + ", Cost: "
						+ NumberFormat.getCurrencyInstance().format(price * 
								numItems));
				synchronized (totalItems) {
					if (!totalItems.containsKey(item)) {
						totalItems.put(item, numItems);
					} else {
						totalItems.replace(item, totalItems.get(item) + numItems);
					}
				}
			}
			answer.append("\nOrder Total: " + NumberFormat.getCurrencyInstance().
					format(orderTotal) + "\n");
			synchronized (orderReport) {
				orderReport.put(id, answer.toString());
			}
			System.out.println("Reading order for client with id: " + id);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
}
