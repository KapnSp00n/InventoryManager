package application;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class LogInController {
	@FXML
	private TextField username, regName;
	@FXML
	private PasswordField thePass, regPass, regPass2;
	@FXML
	private Text fillerText, fillerText2, passReq;
	@FXML
	private Button logButton, regButton;
	@FXML 
	Hyperlink goBack;
	private static Parent logRoot;

	public void logClick(ActionEvent e) {
		Node clicked = (Node) e.getSource();
		Scene currScene = clicked.getScene();
		if (clicked == regButton) {
			//Save login root to logRoot to return after registering
			if (logRoot == null) {
				logRoot = currScene.getRoot();
			}
			try {
				Parent regRoot = FXMLLoader.load(getClass().getResource("/view/reg.fxml"));
				currScene.setRoot(regRoot);
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		} else if (clicked == logButton) {
			try {
				String name = username.getText();
				String passWord = thePass.getText();
				Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/mydb", "root",
						"Notapassword!");
				Statement stmt = con.createStatement();
				ResultSet rs = stmt.executeQuery("CALL logInTime(\"" + name + "\",\"" + passWord + "\");");
				if (rs.next()) {
					byte[] data = rs.getBytes(1);
					if (data != null) {
						ByteArrayInputStream bais = new ByteArrayInputStream(data);
						ObjectInputStream ois;
						
						ois = new ObjectInputStream(bais);
						Client theClient = (Client) ois.readObject();
						SusController.leClient = theClient;
						Stage stage = new Stage();
						FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/lemain.fxml"));
						Parent root = loader.load();
						Scene scene = new Scene(root);
						stage.setScene(scene);
						stage.setTitle("Inventory Management App");
						stage.show();
						SusController.x = (SusController) loader.getController();
						stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
							@Override
							public void handle(WindowEvent arg0) {
								try {
									Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/mydb", "root",
											"Notapassword!");
									PreparedStatement stmt = con.prepareStatement("UPDATE users\r\n"
											+ "SET user_data = (?) where user_name = \""+name+"\";");
									stmt.setObject(1, SusController.leClient);
									stmt.executeUpdate();
									con.close();
								} catch (SQLException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
								System.out.println("Closing");
							}
							
						});
						((Stage) currScene.getWindow()).close();
					} else {
						fillerText2.setText("The username or password is incorrect! Please try again");
					}
				}
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (ClassNotFoundException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
	}

	public void regClick(ActionEvent event) {
		if (event.getSource()==goBack) { //go back without registering
			((Node) event.getSource()).getScene().setRoot(logRoot);
		} else { //user clicked Register so try to register then go back
			String name = regName.getText();
			String newPass = regPass.getText();
			String newPass2 = regPass2.getText();
			try {
				if (name.length()<8) {
					fillerText.setText("Username is too short!");
				} else if (!newPass.equals(newPass2)) {
					fillerText.setText("Passwords don't match!");
				} else if (!checkPass(newPass)) {
					fillerText.setText("Password didn't meet requirements!");
				} else {
					Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/mydb", "root",
							"Notapassword!");
					Statement stmt0 = con.createStatement();
					ResultSet rs = stmt0.executeQuery("select * from users where user_name = \"" + name + "\";");
					if (rs.next()) {
						fillerText.setText("Username already exists, please choose a" + " different one");
					} else {
						PreparedStatement stmt = con.prepareStatement("CALL addUser(?, ?, ?);");
						stmt.setString(1, name);
						stmt.setString(2, newPass);
						Client mrBruh = new Client(name);
						stmt.setObject(3, mrBruh);
						stmt.executeUpdate();
						((Node) event.getSource()).getScene().setRoot(logRoot);
					}
					con.close();
				} 
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	private static boolean checkPass (String pass) {
		String x = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#$%^&+=])(?=\\S+$).{8,20}$";
		return pass.matches(x);
	}
}
