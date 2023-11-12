package application;

import java.math.BigDecimal;
import java.net.URL;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.DatePicker;
import javafx.scene.text.Text;

public class SummaryController implements Initializable{
	@FXML
	Text totRev, revTax, totExp, expTax, profit;
	@FXML
	DatePicker fromDate, toDate;
	
	public void onDateAction (ActionEvent e) {
		LocalDate fromD = fromDate.getValue();
		LocalDate toD = toDate.getValue();
		BigDecimal totRevenue=BigDecimal.valueOf(0),revenueTax=BigDecimal.valueOf(0),
				totExpense=BigDecimal.valueOf(0),expenseTax =BigDecimal.valueOf(0);
		if (fromD!=null && toD!=null) {
			for (Order x : SusController.leClient.getSellOrders()) {
				LocalDate orderDate = x.getDate();
				if (orderDate.isAfter(fromD)&&orderDate.isBefore(toD)) {
					totRevenue=totRevenue.add(x.getTotal());
					revenueTax=revenueTax.add(x.getTax());
				}
			}
			for (Order x : SusController.leClient.getOrders()) {
				LocalDate orderDate = x.getDate();
				if (orderDate.isAfter(fromD)&&orderDate.isBefore(toD)) {
					totExpense=totExpense.add(x.getTotal());
					expenseTax=expenseTax.add(x.getTax());
				}
			}
			totRev.setText(NumberFormat.getCurrencyInstance().format(totRevenue));
			revTax.setText(NumberFormat.getCurrencyInstance().format(revenueTax));
			totExp.setText(NumberFormat.getCurrencyInstance().format(totExpense));
			expTax.setText(NumberFormat.getCurrencyInstance().format(expenseTax));
			profit.setText(NumberFormat.getCurrencyInstance().format(totRevenue.subtract(totExpense)));
		}
	}
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		
		
	}
	
	
}
