package application;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.AnchorPane;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.TableRow;

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
	public void openOrder(Order selectedOrder) {
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
	}
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		manuCol.setCellValueFactory(new PropertyValueFactory<Order, String>("producer"));
		dateCol.setCellValueFactory(new PropertyValueFactory<Order, LocalDate>("date"));
		total.setCellValueFactory(new PropertyValueFactory<Order, BigDecimal>("total"));
		tax.setCellValueFactory(new PropertyValueFactory<Order, BigDecimal>("tax"));
		totalWTax.setCellValueFactory(new PropertyValueFactory<Order, BigDecimal>("totalWTax"));

		//Creates ContextMenu for orderTable
    	ContextMenu menu = new ContextMenu();
    	MenuItem mitem1 = new MenuItem("Edit");
    	MenuItem mitem2 = new MenuItem("Duplicate");
    	MenuItem mitem3 = new MenuItem("Delete Order");
    	menu.getItems().add(mitem1);
    	menu.getItems().add(mitem2);
    	menu.getItems().add(mitem3);
    	
    	//Defines what each menu item will do
    	menu.setOnAction((ActionEvent event) ->{
    		Order selectedOrder = orderTable.getSelectionModel().getSelectedItem();
    		if (event.getTarget()==mitem1) {
    			openOrder(selectedOrder);
    		} else if (event.getTarget()==mitem2) {
    			orderTable.getItems().add(new Order (selectedOrder));
    		} else if (event.getTarget()==mitem3){
    			orderTable.getItems().remove(selectedOrder);
    		}
    	});
    	
    	//Add ContextMenu to orderTable
    	orderTable.setContextMenu(menu);
    	
		orderTable.setOnMouseClicked(e->{
			if (e.getButton().equals(MouseButton.PRIMARY)&&e.getClickCount() == 2) {
				Order selectedOrder = orderTable.getSelectionModel().getSelectedItem();
	            openOrder(selectedOrder);
	        } 
		});
		
//		orderTable.setRowFactory( tv -> {
//		    TableRow<Order> row = new TableRow<Order>();
//		    row.setOnMouseClicked(event -> {
//		        if (event.getClickCount() == 2 && (! row.isEmpty()) ) {
//		            System.out.println("Hello");
//		        }
//		    });
//		    row.setContextMenu(menu);
//		    return row ;
//		});
		
		
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
