package edu.packagemanagement;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import edu.packagemanagement.model.Project;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class LoginController {
	@FXML
	TextField account = new TextField();
	@FXML
	PasswordField password = new PasswordField();
	private static String URL;
	@FXML
	private void login() {
		String account1 = account.getText().trim();
		String password1 = password.getText().trim();
		String selectuser = "select * from User1 where account = '" + account1 + "' and password = '" + password1 + "'";
		if(account1.equals("") || password1.equals("")) {
			showerror("please fill account and password");
			return ;
		} else {
			Connection connect = null;

			try {
				connect = DriverManager.getConnection(URL);
				Statement sm = connect.createStatement();
				ResultSet rs = sm.executeQuery(selectuser);
				if (rs.next()) {
					int userid = rs.getInt(1);
					FileWriter fwt = new FileWriter(new File("user.txt"));


					System.out.print(userid);
					MainController.setUserid(userid);
					NewProjectController.setSelectproject(userid + "");
					Project.setUserid(userid);
					fwt.write(userid + "");
					fwt.flush();
				} else {
					showerror("Account or password isn't true");
					return ;
				}
				showinfo("Login succesfully!");
			} catch (SQLException | IOException e) {
				e.printStackTrace();
			}
		}
		try {
			App.setRoot("main");
			App.setsize(753,1238);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	@FXML
	private void switchtoregister() {
		try {
			App.setRoot("register");
			App.setsize(470,408);
		} catch (IOException e) {
		}
	}
	public static void setURL(String uRL) {
		URL = uRL;
	}
	private void showerror(String a) {
		Alert alert = new Alert(AlertType.ERROR);
		alert.setTitle("ERROR");
		alert.setContentText(a);
		alert.getDialogPane().getStyleClass().add("alert");
		alert.getDialogPane().getStylesheets().add(this.getClass().getResource("application.css").toExternalForm());
		alert.showAndWait();
	}
	private void showinfo(String a) {
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("INFOMATION");
		alert.setContentText(a);
		alert.getDialogPane().getStyleClass().add("alert");
		alert.getDialogPane().getStylesheets().add(this.getClass().getResource("application.css").toExternalForm());
		alert.showAndWait();
	}
}
