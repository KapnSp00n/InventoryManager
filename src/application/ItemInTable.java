package application;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.function.UnaryOperator;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.util.converter.DoubleStringConverter;
import javafx.util.converter.IntegerStringConverter;

public class ItemInTable implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -1649273385016940349L;
	private transient TextField price;
	private transient TextField num;
	private BigDecimal total;
	private transient TextField tax;
	private BigDecimal totalTax;

	private Item theItem;
	private BigDecimal price0;
	private int num0;
	private BigDecimal tax0;

	public ItemInTable() {
	}
	public ItemInTable (ItemInTable x) {
		theItem = x.theItem;
		total = x.total;
		totalTax = x.totalTax;
		price0 = x.price0;
		num0 = x.num0;
		tax0 = x.tax0;
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

	void prep(TableView<ItemInTable> items2) {
		if (theItem != null) {
			prepNumAndTax(items2);
			setPrice(price0);
			num.setText("" + num0);
			tax.setText("" + (tax0==null?0:tax0));
			items2.refresh();
		}
	}

	public void resetStoredStuff() {
		price0 = null;
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
	public void prepNumAndTax(TableView<ItemInTable> items2) {
		price = new TextField();
		price.setTextFormatter(createFormatter());
		price.textProperty().addListener((observable, oldValue, newValue) -> {
			price0 = getP();
			String numInput = num.getText();
			String taxIn = tax.getText();
			if (num!=null&&!numInput.equals("")) {
				total = price0.multiply(new BigDecimal (numInput));
				totalTax = total;
			}
			if (!taxIn.equals("")&&total!=null) {
				totalTax = total
						.add(total.multiply((new BigDecimal(taxIn)).divide(new BigDecimal(100))));
			}
			items2.refresh();
		});
		num = new TextField();
		num.setTextFormatter(createIntFormatter());
		num.textProperty().addListener((observable, oldValue, newValue) -> {
			num0 = getN();
			total = price0.multiply(new BigDecimal(num0));
			if (tax0!=null) {
				totalTax = total
						.add(total.multiply(tax0.divide(new BigDecimal(100))));
			} else {
				totalTax = total;
			}
			items2.refresh();
		});
		tax = new TextField();
		tax.setTextFormatter(createFormatter());
		tax.textProperty().addListener((observable, oldValue, newValue) -> {
			tax0 = getT();
			if (total != null) {
				totalTax = total
						.add(total.multiply(tax0.divide(new BigDecimal(100))));
			}
			items2.refresh();
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

	public Item getTheItem() {
		return theItem;
	}
	public void setTheItem(Item theItem) {
		this.theItem = theItem;
	}
}
