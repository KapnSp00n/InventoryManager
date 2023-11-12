package application;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class SusController implements Initializable {
	@FXML
	private AnchorPane pany;
	@FXML
	TableView<Order> orderTable;
	@FXML
	private TableColumn<Order, String> manuCol;
	@FXML
	private TableColumn<Order,LocalDate> dateCol;
	@FXML
	private TableColumn<Order, BigDecimal> total, tax, totalWTax;
	@FXML
	private MenuItem newSell, newBuy, changeOrder, deleteOrder, lItems;
	@FXML 
	private Button buyOrderButton, sellOrderButton, sumButton;
	static ObservableList<Order> buyOrdersList;
	static ObservableList<Order> sellOrdersList;
	static ObservableList<Item> itemsList;
	static Client leClient;
	static SusController x;
	OrderType orderListType;
	public void onClick(ActionEvent e) {
		if (e.getSource() == newSell ||e.getSource() == newBuy) {
			try {
				Stage stage = new Stage();
				FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/main.fxml"));
				Parent root = loader.load();
				Scene scene = new Scene(root);
				scene.getStylesheets().add(getClass().getResource("/view/application.css").toExternalForm());
				stage.setScene(scene);
				stage.setTitle("Order Processor");
				Controller leControl = (Controller)loader.getController();
				if (e.getSource()==newSell) {
					leControl.newOrderType = OrderType.SELL;
				} else {
					leControl.newOrderType = OrderType.BUY;
				}
				stage.show();
			} catch (Exception eee) {
				eee.printStackTrace();
			}
		} else if (e.getSource() == changeOrder) {
			Order selectedOrder = orderTable.getSelectionModel().getSelectedItem();
			if (selectedOrder != null) {
				try {
					Stage stage = new Stage();
					FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/main.fxml"));
					Parent root = loader.load();
					((Controller)loader.getController()).setCurrentOrder(selectedOrder);
					Scene scene = new Scene(root);
					scene.getStylesheets().add(getClass().getResource("/view/application.css").toExternalForm());
					stage.setScene(scene);
					stage.setTitle("Order Processor2");
					stage.show();
				} catch (Exception eee) {
					eee.printStackTrace();
				}
			}
		} else if (e.getSource() == deleteOrder) {
			Order selectedOrder = orderTable.getSelectionModel().getSelectedItem();
			orderTable.getItems().remove(selectedOrder);
		} else if (e.getSource()==lItems) {
			try {
				Stage stage = new Stage();
				FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/ItemList.fxml"));
				Parent root;
				root = loader.load();
				Scene scene = new Scene(root);
				stage.setScene(scene);
				stage.setTitle("List of Items");
				stage.show();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
	}
	public void onButtonClick(ActionEvent e) {
		if (e.getSource() == buyOrderButton) {
			orderTable.setItems(buyOrdersList);
			manuCol.setText("Manufacturer");
			orderListType = OrderType.BUY;
		} else if (e.getSource() == sellOrderButton) {
			orderTable.setItems(sellOrdersList);
			manuCol.setText("Buyer");
			orderListType = OrderType.SELL;
		} else if (e.getSource() == sumButton) {
			try {
				Stage stage = new Stage();
				FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/summary.fxml"));
				Parent root = loader.load();
				Scene scene = new Scene(root);
				stage.setScene(scene);
				stage.setTitle("Summary");
				stage.show();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
	}
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		manuCol.setCellValueFactory(new PropertyValueFactory<Order, String>("producer"));
		dateCol.setCellValueFactory(new PropertyValueFactory<Order, LocalDate>("date"));
		total.setCellValueFactory(new PropertyValueFactory<Order, BigDecimal>("total"));
		tax.setCellValueFactory(new PropertyValueFactory<Order, BigDecimal>("tax"));
		totalWTax.setCellValueFactory(new PropertyValueFactory<Order, BigDecimal>("totalWTax"));
		
		if (leClient.getOrders()!=null && leClient.getSellOrders()!=null) {
			buyOrdersList=FXCollections.observableList(leClient.getOrders());
			sellOrdersList = FXCollections.observableList(leClient.getSellOrders());
			orderTable.setItems(buyOrdersList);
			orderListType = OrderType.BUY;
		}
		
		if (leClient==null) {
			SavedInfo x = addItemController.restoreManager("savedInfo.ser");
			itemsList = FXCollections.observableList(x.getItems());
		} else {
			itemsList = FXCollections.observableList(leClient.getItems());
		}
	}
}
