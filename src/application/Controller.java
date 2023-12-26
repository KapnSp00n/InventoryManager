package application;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URL;
import java.text.NumberFormat;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.ChoiceBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.InputEvent;
import javafx.scene.input.KeyEvent;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.scene.Node;
public class Controller implements Initializable{
	@FXML
	ChoiceBox<Item> list;
	@FXML
	TableView<ItemInTable> table;
	@FXML
	TableColumn<ItemInTable, Item> itemBox;
	@FXML
	TableColumn<ItemInTable, TextField> price, num, tax;
	@FXML
	TableColumn<ItemInTable, Double> total, totalTax;
	@FXML
	Text gTotal,gTotalTax;
	@FXML
	Button butt, buttRem;
	@FXML
	TextField name;
	@FXML 
	DatePicker date;
	static ObservableList<Item> items;
	static Client leClient;
	private BigDecimal grandTax,grand;
	Order currentOrder;
	OrderType newOrderType;
	public void onClick(ActionEvent e) {
		if (e.getSource()==butt) {
			Stage s = new Stage();
			Parent root;
			try {
				FXMLLoader loader = new FXMLLoader (getClass().getResource("/view/addItem.fxml"));
				root = loader.load();
				addItemController itemPage = (addItemController) loader.getController();
				itemPage.prepFilter();
				itemPage.prepListener();
				Scene scene = new Scene(root);
				s.setScene(scene);
				s.show();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		} else if (e.getSource()==buttRem) {
			if (list.getValue()==null) {
				System.out.println("Please select an item from the box to remove");
			} else {
				items.remove(list.getValue());
				list.setValue(null);
			}
		}
	}
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		leClient = SusController.leClient;
		items=SusController.itemsList;
		list.setItems(items);
		
		//TableView Setup
		table.setEditable(true);
		itemBox.setEditable(true);
		itemBox.setCellFactory(ChoiceBoxTableCell.forTableColumn(items));
		itemBox.setOnEditCommit(e->{
			ItemInTable curr = table.getSelectionModel().getSelectedItem();
			curr.setTheItem(e.getNewValue());
			curr.prepNumAndTax(table);
			curr.setPrice(curr.getTheItem().getPrice());
			if (e.getTablePosition().getRow()==table.getItems().size()-1) {
				table.getItems().add(new ItemInTable());
			}
			table.refresh();
		});
		itemBox.setCellValueFactory(new PropertyValueFactory<ItemInTable,Item>("theItem"));
		price.setCellValueFactory(new PropertyValueFactory<ItemInTable,TextField>("price"));
		num.setCellValueFactory(new PropertyValueFactory<ItemInTable,TextField>("num"));
		total.setCellValueFactory(new PropertyValueFactory<ItemInTable,Double>("total"));
		tax.setCellValueFactory(new PropertyValueFactory<ItemInTable, TextField>("tax"));
		totalTax.setCellValueFactory(new PropertyValueFactory<ItemInTable, Double>("totalTax"));
		if (currentOrder==null) {
			ObservableList<ItemInTable> tableItems = FXCollections.observableArrayList(
					new ItemInTable()
					);
			table.setItems(tableItems);
		}
		
		//Recalculate grand whenever keys pressed are numbers
		table.addEventHandler(KeyEvent.ANY, e->{
			if (e.getText().matches("[0-9]")) {
				calcGrand();
				System.out.println("Hello");
			}
		});
	}
	
	//Listener for observable list so grandTotal can change
	void calcGrand() {
		grand = BigDecimal.valueOf(0);
		grandTax = BigDecimal.valueOf(0);
		for (ItemInTable currItem : table.getItems()) {
			if (currItem.getTotal() != null) {
				grand = grand.add(currItem.getTotal());
			}
			if (currItem.getTotalTax() != null) {
				grandTax = grandTax.add(currItem.getTotalTax());
			}
		}
		gTotal.setText(NumberFormat.getCurrencyInstance().format(grand));
		gTotalTax.setText(NumberFormat.getCurrencyInstance().format(grandTax));
	}
	
	/*	Another way to do it but require keeping track of row number and giving each
	 *  ItemInTable a ChoiceBox which is not efficient at all
	 */
//	private void setCell(int numCell) {
//		ChoiceBox<Item> currBox = itemBox.getCellData(numCell);
//		currBox.setOnAction(e->{
//			ObservableList<ItemInTable> tabItems = table.getItems();
//			if (currBox.isShowing()) {
//				Item curr = currBox.getValue();
//				ItemInTable itTab = tabItems.get(numCell);
//				itTab.resetStoredStuff();
//				itTab.prepNumAndTax(numCell,tabItems);
//				if (curr!=null) {
//					itTab.setPrice(curr.getPrice());
//				}
//				tabItems.set(numCell, itTab);
//				if (numCell==tabItems.size()-1) {
//					ChoiceBox<Item> ggg = new ChoiceBox<Item>(items);
//					ggg.setVisible(false);
//					tabItems.add(new ItemInTable(ggg));
//					setCell(numCell+1);
//				}
//			} else {
//				currBox.setValue(tabItems.get(numCell).getItem());
//			}
//		});
//	}
	
	public void onSaveClick(ActionEvent e) {
		if (currentOrder==null) {
			Order toBeAdded = new Order (name.getText(),date.getValue(),
					table.getItems(),grand, grandTax.subtract(grand), grandTax, newOrderType);
			if (newOrderType == OrderType.SELL) {
				SusController.sellOrdersList.add(toBeAdded);
			} else {
				SusController.buyOrdersList.add(toBeAdded);
			}
		} else {
			currentOrder.setProducer(name.getText());
			currentOrder.setDate(date.getValue());
			currentOrder.setTotal(grand);
			currentOrder.setTotalWTax(grandTax);
			currentOrder.setTax(grandTax.subtract(grand));
			OrderType currentType = currentOrder.getOrderType();
			if (currentType==SusController.x.orderListType) {
				if (currentType == OrderType.SELL) {
					for (int i = 0; i< SusController.sellOrdersList.size();i++) {
						if (SusController.sellOrdersList.get(i)==currentOrder) {
							SusController.sellOrdersList.set(i, currentOrder);
						}
					}
				} else {
					for (int i = 0; i< SusController.buyOrdersList.size();i++) {
						if (SusController.buyOrdersList.get(i)==currentOrder) {
							SusController.buyOrdersList.set(i, currentOrder);
						}
					}
				}
			}
		}
		Node huh = ((Node)e.getSource());
		((Stage)huh.getScene().getWindow()).close();
	}
	
	public void onRemoveClick(ActionEvent e) {
		ItemInTable toRemove = table.getSelectionModel().getSelectedItem();
		if (toRemove==null) {
			System.out.println("Select an item to remove");
		} else {
			table.getItems().remove(toRemove);
		}
	}
	public void setCurrentOrder(Order curr) {
		currentOrder = curr;
		name.setText(currentOrder.getProducer());
		date.setValue(currentOrder.getDate());
		grand = currentOrder.getTotal();
		grandTax = currentOrder.getTotalWTax();
		ObservableList<ItemInTable> currList = currentOrder.getOrderItems();
		for (int i =0; i<currList.size();i++) {
			currList.get(i).prep(table);
		}
		table.setItems(currList);
		gTotal.setText(NumberFormat.getCurrencyInstance().format(grand));
		gTotalTax.setText(NumberFormat.getCurrencyInstance().format(grandTax));

	}
}
