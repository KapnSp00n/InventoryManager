package application;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

public class ItemListController implements Initializable{
	@FXML
	TableView<Item> itemTable;
	@FXML
	TableColumn<Item, String> nameCol;
	@FXML
	TableColumn<Item, BigDecimal> priceCol;
	@FXML 
	Button removeButt, addButt;
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		nameCol.setCellValueFactory(new PropertyValueFactory<Item,String>("name"));
		priceCol.setCellValueFactory(new PropertyValueFactory<Item,BigDecimal>("price"));
		itemTable.setItems(SusController.itemsList);
	}
	public void changeItem (ActionEvent e) {
		if (e.getSource() == addButt) {
			try {
				Stage s = new Stage();
				FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/addItem.fxml"));
				Parent root = loader.load();
				Scene scene = new Scene(root);
				addItemController hihi = (addItemController)loader.getController();
				hihi.callMePwease();
				hihi.prep();
				s.setScene(scene);
				s.show();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		} else {
			Item currSelected = itemTable.getSelectionModel().getSelectedItem();
			if (currSelected == null) {
				return;
			}
			if (e.getSource()==removeButt) {
				SusController.itemsList.remove(currSelected);	
			} else {
				Stage s = new Stage();
				FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/addItem.fxml"));
				try {
					Parent root = loader.load();
					Scene scene = new Scene(root);
					addItemController hihi = (addItemController)loader.getController();
					hihi.currItem=currSelected;
					hihi.currItemPos=itemTable.getSelectionModel().getSelectedIndex();
					hihi.callMePwease();
					hihi.prep();
					s.setScene(scene);
					s.show();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		}
	}
}
