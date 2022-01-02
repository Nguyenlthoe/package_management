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
			sc.close();
		} catch (FileNotFoundException e) {
			System.out.println("URLSQL.txt was not found");
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
		} catch (FileNotFoundException e) {

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

}