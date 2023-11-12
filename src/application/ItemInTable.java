package application;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.function.UnaryOperator;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.util.converter.DoubleStringConverter;
import javafx.util.converter.IntegerStringConverter;

public class ItemInTable implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -1649273385016940349L;
	private transient ChoiceBox<Item> items;
	private transient TextField price;
	private transient TextField num;
	private BigDecimal total;
	private transient TextField tax;
	private BigDecimal totalTax;

	private Item theItem;
	private BigDecimal price00;
	private int num0;
	private BigDecimal tax0;

	public ItemInTable(ChoiceBox<Item> items) {
		this.items = items;
	}

	public ChoiceBox<Item> getItems() {
		return items;
	}

	public TextField getPrice() {
		return price;
	}

	private BigDecimal getP() {
		if (price.getText().equals("")) {
			return new BigDecimal(0);
		}
		return new BigDecimal(price.getText());
	}

	private int getN() {
		if (num.getText().equals("")) {
			return 0;
		}
		return Integer.parseInt(num.getText());
	}

	private BigDecimal getT() {
		if (tax.getText().equals("")) {
			return new BigDecimal(0);
		}
		return new BigDecimal(tax.getText());
	}

	void prep(int numRow, ObservableList<ItemInTable> items2) {
		items = new ChoiceBox<Item>(FXCollections.observableList(SusController.leClient.getItems()));
		if (numRow == items2.size() - 1) {
			items.setVisible(false);
		}
		if (theItem != null) {
			items.getSelectionModel().select(theItem);
			prepNumAndTax(numRow, items2);
			setPrice(price00);
			num.setText("" + num0);
			tax.setText("" + (tax0==null?0:tax0));
			items2.set(numRow, this);
		}
	}

	public void resetStoredStuff() {
		price00 = null;
		num0 = 0;
		tax0 = null;
		total = null;
		totalTax = null;
	}

	private TextFormatter<Double> createFormatter() {
		UnaryOperator<TextFormatter.Change> doubleFilter = change -> {
			String newText = change.getControlNewText();
			if (newText.matches("(([1-9][0-9]*)|0)?(\\.[0-9]*)?")) {
				return change;
			}
			return null;
		};
		TextFormatter<Double> x = new TextFormatter<Double>(new DoubleStringConverter(), null, doubleFilter);
		return x;
	}

	private TextFormatter<Integer> createIntFormatter() {
		UnaryOperator<TextFormatter.Change> intFilter = change -> {
			String newText = change.getControlNewText();
			if (newText.matches("\\d*")) {
				return change;
			}
			return null;
		};
		TextFormatter<Integer> x = new TextFormatter<Integer>(new IntegerStringConverter(), null, intFilter);
		return x;
	}

	// Preparing the TextField for tax and num input
	public void prepNumAndTax(int numRow, ObservableList<ItemInTable> items2) {
		price = new TextField();
		theItem = items.getValue();
		price.setTextFormatter(createFormatter());
		price.textProperty().addListener((observable, oldValue, newValue) -> {
			price00 = getP();
			String numInput = num.getText();
			String taxIn = tax.getText();
			if (num!=null&&!numInput.equals("")) {
				total = price00.multiply(new BigDecimal (numInput));
				totalTax = total;
			}
			if (!taxIn.equals("")&&total!=null) {
				totalTax = total
						.add(total.multiply((new BigDecimal(taxIn)).divide(new BigDecimal(100))));
			}
			items2.set(numRow, this);
		});
		num = new TextField();
		num.setTextFormatter(createIntFormatter());
		num.textProperty().addListener((observable, oldValue, newValue) -> {
			num0 = getN();
			total = price00.multiply(new BigDecimal(num0));
			if (tax0!=null) {
				totalTax = total
						.add(total.multiply(tax0.divide(new BigDecimal(100))));
			} else {
				totalTax = total;
			}
			items2.set(numRow, this);
		});
		tax = new TextField();
		tax.setTextFormatter(createFormatter());
		tax.textProperty().addListener((observable, oldValue, newValue) -> {
			tax0 = getT();
			if (total != null) {
				totalTax = total
						.add(total.multiply(tax0.divide(new BigDecimal(100))));
			}
			items2.set(numRow, this);
		});
	}

	public TextField getNum() {
		return num;
	}

	public TextField getTax() {
		return tax;
	}

	public void setPrice(BigDecimal price) {
		this.price.setText(price.toString());
	}

	public BigDecimal getTotal() {
		return total;
	}

	public BigDecimal getTotalTax() {
		return totalTax;
	}
	
	public BigDecimal getTaxx() {
		return tax0;
	}

	public Item getItem() {
		return theItem;
	}
}
