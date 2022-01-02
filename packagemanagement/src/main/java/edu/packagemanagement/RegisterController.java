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

public class RegisterController {
	private static String URL;
	@FXML
	TextField account = new TextField();
	@FXML
	PasswordField password = new PasswordField();
	@FXML
	PasswordField password2 = new PasswordField();
	@FXML
	private void switchtologin() {
		try {
			App.setRoot("login");
			App.setsize(350,408);
		} catch (IOException e) {
		}
	}
	@FXML
	private void registerandlogin() {
		String acc = account.getText().trim();
		String pw = password.getText();
		String pw2 = password.getText();
		if(!pw.equals(pw2)) {
			showerror("No duplicate password");
			return;
		}
		Connection connect = null;
		String selectuser = "select * from User1 where account = '" + acc + "'";
		String selectuserrgt = "select * from User1 where account = '" + acc + "' and password = '" + pw + "'";
		String insertuser = "insert into User1 values('" + acc + "','" + pw + "')";
		try {
			connect = DriverManager.getConnection(URL);
			Statement sm = connect.createStatement();
			ResultSet rs = sm.executeQuery(selectuser);
			if (rs.next()) {
				showerror("account already exists");
				return ;
			} else {
				sm.execute(insertuser);
				rs = sm.executeQuery(selectuserrgt);
				if(rs.next()) {
					int userid = rs.getInt(1);
					FileWriter fwt = new FileWriter(new File("user.txt"));
					MainController.setUserid(userid);
					NewProjectController.setSelectproject(userid + "");
					Project.setUserid(userid);
					fwt.write(userid + "");
					fwt.flush();
				}
				try {
					App.setRoot("main");
					App.setsize(753,1238);
				} catch (IOException e) {
					e.printStackTrace();
				}
				showinfo("Register succesfully!");
				return ;
			}

		} catch (SQLException | IOException e) {
			e.printStackTrace();
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
