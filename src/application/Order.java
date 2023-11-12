package application;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;

import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Order implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -2858342986967327082L;
	private String producer;
	private LocalDate date;
	private BigDecimal total, tax, totalWTax;
	private ArrayList<ItemInTable> items;
	private OrderType type;
	public Order(String producer, LocalDate date, ObservableList<ItemInTable> items,
			BigDecimal total, BigDecimal tax, BigDecimal totalWTax, OrderType type) {
		this.date = date;
		this.producer = producer;
		this.items = new ArrayList<ItemInTable>(items);
		this.setTotal(total);
		this.setTax(tax);
		this.setTotalWTax(totalWTax);
		setOrderType(type);
	}
	public Order (Order copyMe) {
		this.date = copyMe.date;
		this.producer=copyMe.producer;
		this.items = new ArrayList<ItemInTable>();
	}
	public String getProducer() {
		return producer;
	}
	public ObservableList<ItemInTable> getOrderItems(){
		return FXCollections.observableList(items);
	}
	public void setOrderItems(ObservableList<ItemInTable> items){
	}
 	public void setProducer(String producer) {
		this.producer = producer;
	}
	public LocalDate getDate() {
		return date;
	}
	private void setOrderType(OrderType type) {
		this.type = type;
	}
	public OrderType getOrderType() {
		return type;
	}
	public void setDate(LocalDate date) {
		this.date = date;
	}
	public BigDecimal getTotal() {
		return total;
	}
	public void setTotal(BigDecimal total) {
		this.total = total;
	}
	public BigDecimal getTax() {
		return tax;
	}
	public void setTax(BigDecimal tax) {
		this.tax = tax;
	}
	public BigDecimal getTotalWTax() {
		return totalWTax;
	}
	public void setTotalWTax(BigDecimal totalWTax) {
		this.totalWTax = totalWTax;
	}
}
