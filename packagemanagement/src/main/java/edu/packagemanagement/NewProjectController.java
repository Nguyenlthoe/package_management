package edu.packagemanagement;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.filechooser.FileSystemView;

import edu.packagemanagement.model.Library;
import edu.packagemanagement.model.Project;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class NewProjectController {
	@FXML
		private Button createBT = new Button();
	@FXML
		private TextField dirFileTF = new TextField();
	@FXML
		private TextField namePrjTF = new TextField();
	private static StackPane displaystackpane = null;
	private static VBox vboxexplorer1 = null;
	private static PreparedStatement ptsm= null;
	private static String selectproject = "select ID from Project where NameProject = ? and UserID = 1";
	private  String linktoprj = null;
	@FXML
	private void ChooseFile() {
		JFileChooser jfc = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
		jfc.setDialogTitle("Select file manage dependencies");
		jfc.setAcceptAllFileFilterUsed(false);
		FileNameExtensionFilter filter = new FileNameExtensionFilter("xml, json, gradle", "xml", "json",
				"gradle");
		jfc.addChoosableFileFilter(filter);
		int returnVal = jfc.showOpenDialog(null);
		dirFileTF.setText(jfc.getSelectedFile().getPath());
		linktoprj = jfc.getSelectedFile().getParent();
	}
	@FXML
	private void CreateProject() throws FileNotFoundException {
		String dbURL = "jdbc:sqlserver://localhost;databaseName=QL_LIBRARIES_AND_PACKAGES;user=sa;password=sa";
		Connection connect = null;
		//Statement statement = null;
	    try {
			connect = DriverManager.getConnection(dbURL);
			ptsm = connect.prepareStatement(this.selectproject);
			ptsm.setString(1, this.namePrjTF.getText());
			ResultSet rs = ptsm.executeQuery();
			if(rs.next()) {
				showAlert();
				return ;
			}
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
//	    try {
//			statement = connect.createStatement();
//		} catch (SQLException e1) {
//			// TODO Auto-generated catch block
//			e1.printStackTrace();
//		}
		ReadDependency readd = new ReadDependency();
		TreeView<Library> newTree = null;
		Project nPrj = null;
		String[] words;
		Button newButton = null;
		String path = dirFileTF.getText();
		words = path.split("[.]");
		if(words[words.length - 1].equals("json")) {
			nPrj = new Project(namePrjTF.getText(),linktoprj, "NPM", connect);
			final TreeView<Library> newTree1 = readd.ReadNPM(nPrj, dirFileTF.getText(), connect);
			newTree = newTree1;
			newButton = new Button(namePrjTF.getText() + " (NPM)", new ImageView(new Image(new FileInputStream(new File("forder.png")))));
			String css = this.getClass().getResource("application.css").toExternalForm();
			newButton.getStylesheets().add(css);
			newButton.getStyleClass().add("btn");
			newButton.setOnAction(new EventHandler<ActionEvent>() {

				@Override
				public void handle(ActionEvent event) {
					displaystackpane.getChildren().clear();
					displaystackpane.getChildren().add(newTree1);

				}

			});
		} else if (words[words.length - 1].equals("xml")) {
			nPrj = new Project(namePrjTF.getText(),linktoprj, "MAVEN", connect);
			final TreeView<Library> newTree1 = readd.ReadMaven(nPrj, dirFileTF.getText(), connect);
			newTree = newTree1;
			newButton = new Button(namePrjTF.getText() + " (MAVEN)", new ImageView(new Image(new FileInputStream(new File("forder.png")))));
			String css = this.getClass().getResource("application.css").toExternalForm();
			newButton.getStylesheets().add(css);
			newButton.getStyleClass().add("btn");
			newButton.setOnAction(new EventHandler<ActionEvent>() {

				@Override
				public void handle(ActionEvent event) {
					displaystackpane.getChildren().clear();
					displaystackpane.getChildren().add(newTree1);

				}

			});
		} else {
			nPrj = new Project(namePrjTF.getText(),linktoprj, "GRADLE", connect);
			final TreeView<Library> newTree1 = readd.ReadGradle(nPrj, dirFileTF.getText(), connect);
			newTree = newTree1;
			newButton = new Button(namePrjTF.getText() + " (Gradle)", new ImageView(new Image(new FileInputStream(new File("forder.png")))));
			String css = this.getClass().getResource("application.css").toExternalForm();
			newButton.getStylesheets().add(css);
			newButton.getStyleClass().add("btn");
			newButton.setOnAction(new EventHandler<ActionEvent>() {

				@Override
				public void handle(ActionEvent event) {
					displaystackpane.getChildren().clear();
					displaystackpane.getChildren().add(newTree1);

				}

			});
		}
		//System.out.println(newTree.getChildrenUnmodifiable().get(0).toString());
		newTree.prefHeight(displaystackpane.getPrefHeight());
		newTree.prefWidth(displaystackpane.getPrefWidth());
		displaystackpane.getChildren().add(newTree);
		MainController.setOpenproject(nPrj);
		Stage endd = (Stage) createBT.getScene().getWindow();
		endd.close();
	}
	public void setInfo(StackPane x, VBox vboxexplorer) {
		this.displaystackpane = x;
		this.vboxexplorer1 = vboxexplorer;
	}
	private void showAlert() {
		Alert alert = new Alert(AlertType.WARNING);
		alert.setTitle("Name Project is duplicate");
		VBox ndialog = new VBox();
		Label duplicate = new Label(this.namePrjTF.getText() + "Project already exists in the database\n Please rename Project");
		ndialog.getChildren().add(duplicate);
		alert.getDialogPane().setContent(ndialog);
		alert.showAndWait();
	}
	private void showInfoProject(Project a,String b) {
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle(a.toString());
		String information = "Name: " + a.getName() + "\nCategory: " + a.getTypeP()
							+ "\nCreated at: " + a.getDateCreate()
							+ "\nUpdated at: " + a.getDateUpdate()
							+ "\nCreate by IDE: " + b;
		VBox ndialog = new VBox();
		ndialog.setPrefWidth(500);
		ndialog.setPrefHeight(200);
		Label duplicate = new Label(information);
		duplicate.getStylesheets().add(this.getClass().getResource("application.css").toExternalForm());
		duplicate.getStyleClass().add("info");
		ndialog.getChildren().add(duplicate);
		alert.getDialogPane().setContent(ndialog);
		alert.showAndWait();
	}
}
