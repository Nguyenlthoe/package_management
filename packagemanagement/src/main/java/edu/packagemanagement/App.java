package edu.packagemanagement;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;

import edu.packagemanagement.model.Project;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;

/**
 * JavaFX App
 */
public class App extends Application {

	private static Scene scene;

	@Override
	public void start(Stage stage) throws IOException {
		Scanner sc = null;
		try {
			sc = new Scanner(new File("URLSQL.txt"));
			String url = sc.nextLine();
			MainController.setURL(url);
			LoginController.setURL(url);
			RegisterController.setURL(url);
			ReadfromDB.setDbURL(url);
			sc.close();
		} catch (FileNotFoundException e) {
			System.out.println("URLSQL.txt was not found");
		}
		try {
			sc = new Scanner(new File("linkrepo.txt"));
			String mavenrepo = sc.nextLine();
			String gradlerepo = sc.nextLine();
			ReadDependency.setGradlerepo(gradlerepo);
			ReadDependency.setMavenrepo(mavenrepo);
			sc.close();
		} catch (FileNotFoundException e) {

		}
		// scene = new Scene(loadFXML("main"));
		try {
			sc = new Scanner(new File("user.txt"));
			if (!sc.hasNext()) {
				scene = new Scene(loadFXML("register"));
			} else {
				int userid = sc.nextInt();
				MainController.setUserid(userid);
				NewProjectController.setSelectproject(userid + "");
				Project.setUserid(userid);
				scene = new Scene(loadFXML("main"));
			}
			sc.close();
		} catch (FileNotFoundException e) {
			showerror("User.txt not found");
		}

		scene.getStylesheets().add(this.getClass().getResource("application.css").toExternalForm());
		stage.setScene(scene);
		stage.setTitle("Management dependence (NguyenLT)");
		stage.show();
	}

	public static void setRoot(String fxml) throws IOException {
		scene.setRoot(loadFXML(fxml));
	}

	private static Parent loadFXML(String fxml) throws IOException {
		FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
		return fxmlLoader.load();
	}

	public static void main(String[] args) {
		launch();
	}

	public static void setsize(double height, double width) {
		Stage st = (Stage) scene.getWindow();
		st.setHeight(height);
		st.setWidth(width);
		st.centerOnScreen();
	}
	private void showerror(String a) {
		Alert alert = new Alert(AlertType.ERROR);
		alert.setTitle("ERROR");
		alert.setContentText(a);
		alert.getDialogPane().getStyleClass().add("alert");
		alert.getDialogPane().getStylesheets().add(this.getClass().getResource("application.css").toExternalForm());
		alert.showAndWait();
	}

}