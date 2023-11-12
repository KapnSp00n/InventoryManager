package application;

import java.io.*;
import java.text.NumberFormat;
import java.util.HashMap;
import java.util.Scanner;
import java.util.TreeMap;

public class OrdersProcessor {
	public static void storePrice(HashMap<String, Double> itemPrice, 
			String dataFile) {
		try {
			Scanner scan = new Scanner(new BufferedReader(new FileReader
					(dataFile)));
			while (scan.hasNextLine()) {
				itemPrice.put(scan.next(), scan.nextDouble());
			}
			scan.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	public static void fileDataToTreeMap (Scanner scan, TreeMap<String, Integer> 
			totItemsPerOrder) {
		while (scan.hasNextLine()) {
			String item = scan.next();
			if (!totItemsPerOrder.containsKey(item)) {
				totItemsPerOrder.put(item, 1);
			} else {
				totItemsPerOrder.replace(item, totItemsPerOrder.get(item) + 1);
			}
			scan.nextLine();
		}
	}

	public static void main(String[] args) {
		TreeMap<String, Integer> totalItems = new TreeMap<String, Integer>();
		HashMap<String, Double> itemsPrice = new HashMap<String, Double>();
		// String report of each order, key is ID
		TreeMap<Integer, String> orderReport = new TreeMap<Integer, String>();

		// Input
		Scanner inputScan = new Scanner(System.in);
		System.out.println("Enter item's data file name: ");
		String dataFile = inputScan.next();
		System.out.println("Enter 'y' for multiple threads, any other character "
				+ "otherwise: ");
		String useThread = inputScan.next();
		System.out.println("Enter number of orders to process: ");
		int numOrder = inputScan.nextInt();
		System.out.println("Enter order's base filename: ");
		String baseFile = inputScan.next();
		System.out.println("Enter result's filename: ");
		String resultFile = inputScan.next();
		inputScan.close();

		// Start
		long startTime = System.currentTimeMillis();
		StringBuilder ans = new StringBuilder("");
		storePrice(itemsPrice, dataFile);
		if (useThread.equals("y")) {
			// Multithread
			Thread[] threads = new Thread[numOrder];
			for (int i = 0; i < threads.length; i++) {
				threads[i] = new Thread(new MyThread((baseFile + (i + 1) + ".txt"), 
						orderReport, totalItems, itemsPrice));
				threads[i].start();
			}
			for (int i = 0; i < threads.length; i++) {
				try {
					threads[i].join();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		} else {
			// Single Thread
			for (int i = 1; i <= numOrder; i++) {
				StringBuilder orderDetails = new StringBuilder("");
				TreeMap<String, Integer> totItemsPerOrder = new TreeMap<String, 
						Integer>();
				try {
					Scanner scan = new Scanner(new BufferedReader(new FileReader
							(baseFile + i + ".txt")));
					
					scan.next();
					int id = scan.nextInt();
					orderDetails.append("----- Order details for client with Id: "
							+ id + " -----");
					/*Call static method (method used by both here (single thread)
					  and in MyThread for multiple threads since very similar*/
					fileDataToTreeMap(scan, totItemsPerOrder);
					scan.close();
					double orderTotal = 0;
					for (String item : totItemsPerOrder.keySet()) {
						int numItems = totItemsPerOrder.get(item);
						double price = itemsPrice.get(item);
						orderTotal += numItems * price;
						orderDetails.append("\nItem's name: " + item + 
								", Cost per item: " + NumberFormat.
								getCurrencyInstance().format(price) + 
								", Quantity: " + numItems + ", " + "Cost: " + 
								NumberFormat.getCurrencyInstance().format(price 
								* numItems));
						if (!totalItems.containsKey(item)) {
							totalItems.put(item, numItems);
						} else {
							totalItems.replace(item, totalItems.get(item) + 
									numItems);
						}
					}
					orderDetails.append("\nOrder Total: " + NumberFormat.
							getCurrencyInstance().format(orderTotal) + "\n");
					orderReport.put(id, orderDetails.toString());
					System.out.println("Reading order for client with id: " + id);
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				}
			}
		}

		// Appending all the details per order based on ID number in order
		for (int id : orderReport.keySet()) {
			ans.append(orderReport.get(id));
		}

		// Appending summary to StringBuilder ans
		ans.append("***** Summary of all orders *****");
		double grandTotal = 0;
		for (String item : totalItems.keySet()) {
			int numItems = totalItems.get(item);
			double price = itemsPrice.get(item);
			grandTotal += numItems * price;
			ans.append("\nSummary - Item's name: " + item + ", Cost per item: "
					+ NumberFormat.getCurrencyInstance().format(price) 
					+ ", Number sold: " + numItems + ", Item's Total: " 
					+ NumberFormat.getCurrencyInstance().format(price * numItems));
		}
		ans.append("\nSummary Grand Total: " + NumberFormat.getCurrencyInstance().
				format(grandTotal) + "\n");

		// Wrtiting to File
		try {
			BufferedWriter bufferedWriter = new BufferedWriter(
					new FileWriter(resultFile, false));
			bufferedWriter.write(ans.toString());

			bufferedWriter.flush();
			bufferedWriter.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		long endTime = System.currentTimeMillis();
		System.out.println("Processing time (msec): " + (endTime - startTime));
	}
}