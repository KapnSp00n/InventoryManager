package application;

import java.io.*;
import java.math.BigDecimal;
import java.util.Optional;
import java.util.function.UnaryOperator;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.scene.control.Alert;
import javafx.scene.control.TextFormatter;
import javafx.util.converter.DoubleStringConverter;

public class addItemController {
	@FXML
	TextField name, price;
	Item currItem;
	int currItemPos;

	public void prepListener() {
		if (currItem !=null) {
			name.setText(currItem.getName());
			price.setText(currItem.getPrice() + "");
		}
		price.textProperty().addListener((a,b,c)->{
			if (c.matches(b)) {
				return;
			} else {
				System.out.println("Hey");
			}
		});
	}
	public void prepFilter() {
		UnaryOperator<TextFormatter.Change> doubleFilter = change -> {
            String newText = change.getControlNewText();
            if (newText.matches("(([1-9][0-9]*)|0)?(\\.[0-9]*)?")) { 
                return change;
            } 
            return null;
        };
		TextFormatter<Double> x = new TextFormatter<Double>(new DoubleStringConverter(), null, doubleFilter);
		price.setTextFormatter(x);
	}

	public void onClick(ActionEvent event) {
		String itemName = name.getText();
		if (itemName.equals("") || price.getText().equals("")) {
			//Put reminder for user to type in price and item name
			Alert x = new Alert (Alert.AlertType.WARNING);
			x.setContentText("Please fill out all required information before saving");
			x.showAndWait();
			return;
		}
		BigDecimal itemPrice = new BigDecimal(price.getText());
		if (currItem == null) {
			try {
				Item toAdd = new Item(itemName, itemPrice);
				SusController.itemsList.add(toAdd);
				if (Controller.leClient != null) {

				} else {
					String fileName = "savedInfo.ser";
					File file = new File(fileName);
					ObjectOutputStream output;
					SavedInfo prev = restoreManager(fileName);
					prev.addThing(toAdd);
					output = new ObjectOutputStream(new FileOutputStream(file));
					output.writeObject(prev);
					output.close();
				}
			} catch (FileNotFoundException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		} else {
			Alert x = new Alert(Alert.AlertType.CONFIRMATION);
			x.setContentText("Apply the changes to orders before this change?");
			Optional<ButtonType> hey = x.showAndWait();
			if (hey.isPresent()) {
				if (hey.get() == ButtonType.OK) {
					currItem.setName(itemName);
					currItem.setPrice(itemPrice);
					SusController.itemsList.set(currItemPos, currItem);
				} else if (hey.get() == ButtonType.CANCEL) {
					SusController.itemsList.remove(currItem);
					SusController.itemsList.add(new Item(itemName, itemPrice));
				}
			}
		}
		Stage addWindow = (Stage) ((Node) event.getSource()).getScene().getWindow();
		addWindow.close();
	}

	public static SavedInfo restoreManager(String fileName) {
		File file = new File(fileName);
		if (file.exists()) {
			ObjectInputStream input;
			try {
				input = new ObjectInputStream(new FileInputStream(file));
				SavedInfo info = (SavedInfo) input.readObject();
				input.close();
				return info;
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
		}
		return new SavedInfo();
	}
}
