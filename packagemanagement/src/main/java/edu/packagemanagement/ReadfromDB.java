package edu.packagemanagement;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

import edu.packagemanagement.model.Library;
import edu.packagemanagement.model.Project;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class ReadfromDB {
	private ArrayList<TreeItem<Library>> libitem = new ArrayList<TreeItem<Library>>();
	private ArrayList<Library> listlib =  new ArrayList<Library>();
	private int countdpp = 0;
	public TreeView<Library> read(Project a){
		Library rootP = null;
		if(a.getTypeP().contains("NPM")) {
			rootP = new Library(a.getName(), "NPM");
		} else if(a.getTypeP().contains("MAVEN")) {
			rootP = new Library(a.getName(), "MAVEN");
		} else {
			rootP = new Library(a.getName(), "GRADLE");
		}
		MainController.setPrjInTree(rootP);
		PreparedStatement ptsm1 = null, ptsm2 = null, ptsm3 = null, ptsm4 = null;
		TreeItem<Library> newTree = new TreeItem<Library>(rootP);
		String dbURL = "jdbc:sqlserver://localhost;databaseName=QL_LIBRARIES_AND_PACKAGES;user=sa;password=sa";
		String selectdependence = "select * from Lib_and_package where Lib_id in(select Lib_id from Project_dependency where ID =" + a.getId() + ")";
		String prp1 = "select * from Lib_and_package where Lib_id in(select lib.Lib_id_dependency from Lib_dependency lib where lib.Lib_id = ?)";
		Connection connect = null;
		Queue<Library> queuepk = new LinkedList<Library>();
		try {
			connect = DriverManager.getConnection(dbURL);
			ptsm1 = connect.prepareStatement(prp1);
			Statement sm = connect.createStatement();
			ResultSet rs = sm.executeQuery(selectdependence);
			while(rs.next()) {
				Library nlib = new Library(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4));
				listlib.add(nlib);
				nlib.setStt(listlib.size() - 1);
				TreeItem nitem = null;
				if(a.getTypeP().contains("NPM")) {
					nitem = new TreeItem<Library>(nlib,new ImageView(new Image(new FileInputStream(new File("Npm-logo.png")))));
				} else {
					nitem = new TreeItem<Library>(nlib,new ImageView(new Image(new FileInputStream(new File("icon-logo.png")))));
				}
				libitem.add(nitem);
				newTree.getChildren().add(libitem.get(libitem.size() - 1));
				queuepk.offer(nlib);
			}
			while (!queuepk.isEmpty()) {
				Library x = queuepk.poll();
				ptsm1.setInt(1, x.getIdLib());
				//System.out.println(x.getIdLib() + " " + x.getArtifactID());
				ResultSet rs2 = ptsm1.executeQuery();
				while(rs2.next()) {
					Library nlib = new Library(rs2.getInt(1), rs2.getString(2), rs2.getString(3), rs2.getString(4));
					listlib.add(nlib);
					nlib.setStt(listlib.size() - 1);
					TreeItem nitem = null;
					if(a.getTypeP().contains("NPM")) {
						nitem = new TreeItem<Library>(nlib,new ImageView(new Image(new FileInputStream(new File("Npm-logo.png")))));
					} else {
						nitem = new TreeItem<Library>(nlib,new ImageView(new Image(new FileInputStream(new File("icon-logo.png")))));
					}
					libitem.add(nitem);
					libitem.get(x.getStt()).getChildren().add(nitem);
					queuepk.offer(nlib);
				}
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		for(int i = 0; i < listlib.size(); i++) {
			boolean check = true;
			for(int j = i - 1; j >= 0; j--) {
				if(listlib.get(i).equals(listlib.get(j))) {
					check = false;
					break;
				}
			}
			if(check == true) {
				countdpp++;
			}
		}
		MainController.countdp = countdpp;
		newTree.setExpanded(true);
		TreeView<Library> newRoot = new TreeView<Library>(newTree);
		newRoot.getStyleClass().add("tree");
		String css = this.getClass().getResource("application.css").toExternalForm();
		newRoot.getStylesheets().add(css);
		//System.out.println(newRoot.toString());
		return newRoot;
	}
}
