package edu.packagemanagement;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Optional;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.filechooser.FileSystemView;

import edu.packagemanagement.model.IDE;
import edu.packagemanagement.model.Library;
import edu.packagemanagement.model.Project;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceDialog;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeView;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.DirectoryChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class MainController {
	@FXML
	private Button FileChoosedBT = new Button();
	@FXML
	private StackPane displaystackpane = new StackPane();
	@FXML
	private VBox vboxexplorer = new VBox();
	@FXML
	private VBox display = new VBox();
	@FXML
		private Button viewinfobt = new Button();
	//, updatep = new Button(), removedp = new Button(),
//						changedp = new Button(), adddp = new Button(), deletep = new Button();
	Stage newPrjStage = new Stage();
	static Stage mainstage;
	private static String URL;
	public static Project openproject = null;
	public static IDE ide = null;
	private boolean updatef = false;
	private static int userid;
	public static boolean updateP = false;
	public static int countdp;
	private static Label infoPlabel = new Label();
	private static boolean turnviewinfo = false;
	private static Library prjInTree;
	// private ArrayList<Button> listbutton = new ArrayList<Button>();
	// private ArrayList<TreeView<Library>> listtree = new
	// ArrayList<TreeView<Library>>();
	@FXML
	public void ReadDependency() {

	}

	@FXML
	public void newProject() {
		updatef = false;
		mainstage = (Stage) displaystackpane.getScene().getWindow();
		try {
			NewProjectController nPrjController = new NewProjectController();
			// nPrjController.setInfo(displaystackpane, vboxexplorer, listbutton, listtree);
			nPrjController.setInfo(displaystackpane, vboxexplorer);
			newPrjStage = new Stage();
			Scene newPrjScene = new Scene(loadFXML("newProject"));

			newPrjStage.setScene(newPrjScene);
			newPrjStage.initOwner(mainstage);
			newPrjStage.initModality(Modality.WINDOW_MODAL);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		newPrjStage.show();
	}

	@FXML
	public void openProject() {
		updatef = false;
		mainstage = (Stage) displaystackpane.getScene().getWindow();
		showopendialog();
	}
	@FXML
	public void changelocalgradle() {
		callChangeLocalGradleAlert();
	}
	private void callChangeLocalGradleAlert() {
		String mavenrepo = ReadDependency.getMavenrepo();
		String oldgradlerepo = ReadDependency.getGradlerepo();
		Alert alert = new Alert(AlertType.WARNING);
		alert.setTitle("Change local gralde repo");
		alert.setHeaderText("Choose invalid local repo, application will be broken");
		alert.setContentText(null);
		alert.getDialogPane().getStyleClass().add("alert");
		alert.getDialogPane().getStylesheets().add(this.getClass().getResource("application.css").toExternalForm());
		DirectoryChooser directoryChooser = new DirectoryChooser();
		directoryChooser.setInitialDirectory(new File("C:"));
		VBox nvbox = new VBox();
		Button chooselink = new Button("Choose directory to local repo");
		TextField link = new TextField();
		link.setEditable(false);
		link.setPromptText(oldgradlerepo);
		chooselink.setOnAction(e -> {
			File selectedDirectory = directoryChooser.showDialog(null);
			String a = selectedDirectory.getAbsoluteFile().toString();
			link.setText(a);
		});
		nvbox.getChildren().addAll(chooselink, link);
		alert.getDialogPane().setContent(nvbox);
		Optional<ButtonType> result = alert.showAndWait();
		if (result.get() == ButtonType.OK) {
			String writefile = mavenrepo + "\n" + link.getText();
			System.out.println(writefile);
			FileWriter fwt;
			try {
				fwt = new FileWriter(new File("linkrepo.txt"));
				fwt.write(writefile);
				fwt.flush();
				showinfo("Successfully");
				ReadDependency.setGradlerepo(link.getText());
			} catch (IOException e1) {
				showerror("change false");
			}

		}
	}
	@FXML
	public void changelocalmaven() {
		callChangeLocalMavenAlert();
	}
	private void callChangeLocalMavenAlert() {
		String oldmavenrepo = ReadDependency.getMavenrepo();
		String gradlerepo = ReadDependency.getGradlerepo();
		Alert alert = new Alert(AlertType.WARNING);
		alert.setTitle("Change local maven repo");
		alert.setHeaderText("Choose invalid local repo, application will be broken");
		alert.setContentText(null);
		alert.getDialogPane().getStyleClass().add("alert");
		alert.getDialogPane().getStylesheets().add(this.getClass().getResource("application.css").toExternalForm());
		DirectoryChooser directoryChooser = new DirectoryChooser();
		directoryChooser.setInitialDirectory(new File("C:"));
		VBox nvbox = new VBox();
		Button chooselink = new Button("Choose directory to local repo");
		TextField link = new TextField();
		link.setEditable(false);
		link.setPromptText(oldmavenrepo);
		chooselink.setOnAction(e -> {
			File selectedDirectory = directoryChooser.showDialog(null);
			String a = selectedDirectory.getAbsoluteFile().toString();
			link.setText(a);
		});
		nvbox.getChildren().addAll(chooselink, link);
		alert.getDialogPane().setContent(nvbox);
		Optional<ButtonType> result = alert.showAndWait();
		if (result.get() == ButtonType.OK) {
			String writefile = link.getText() + "\n" + gradlerepo;
			System.out.println(writefile);
			FileWriter fwt;
			try {
				fwt = new FileWriter(new File("linkrepo.txt"));
				fwt.write(writefile);
				fwt.flush();
				showinfo("Successfully");
				ReadDependency.setMavenrepo(link.getText());
			} catch (IOException e1) {
				showerror("change false");
			}

		}
	}

	private static Parent loadFXML(String fxml) throws IOException {
		FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
		return fxmlLoader.load();
	}

	private void showopendialog() {
		ChoiceDialog<Project> dialog = new ChoiceDialog<Project>();
		// String dbURL =
		// "jdbc:sqlserver://localhost;databaseName=QL_LIBRARIES_AND_PACKAGES;user=sa;password=sa";
		String selectproject = "select * from Project where UserID = " + this.userid;
		Connection connect = null;

		try {
			// connect = DriverManager.getConnection(dbURL);
			connect = DriverManager.getConnection(URL);
			Statement sm = connect.createStatement();
			ResultSet rs = sm.executeQuery(selectproject);
			while (rs.next()) {
				int id = rs.getInt(1);
				String nameP = rs.getString(2);
				String Projectinfo = rs.getString(3);
				String typeP = rs.getString(4);
				int idIDE = rs.getInt(5);
				String datecreate = rs.getString(6);
				String dateupdate = rs.getString(7);
				Project nproject = new Project(id, nameP, Projectinfo, typeP, idIDE, datecreate, dateupdate);
				dialog.getItems().add(nproject);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		dialog.setTitle("open project");
		dialog.setHeaderText(null);
		dialog.getDialogPane().getStyleClass().add("dialog");
		dialog.getDialogPane().getStylesheets().add(this.getClass().getResource("application.css").toExternalForm());
		dialog.setContentText("Choose a project from database :");
		dialog.setWidth(400);
		dialog.setHeight(400);
		Optional<Project> result = dialog.showAndWait();
		result.ifPresent(project -> {
			this.openproject = project;
			// System.out.println(project);
			ReadfromDB rfdb = new ReadfromDB();
			TreeView<Library> a = rfdb.read(project);
			displaystackpane.getChildren().add(a);
			setLabel();
		});
		try {
			connect.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@FXML
	public void deleteProject() {
		if (openproject != null) {
			showWarningAlert();
		} else {
			showerror("No Project are opened");
		}
	}

	@FXML
	public void changeversion() {
		if (openproject == null) {
			showerror("No Project are opened");
		} else {
			if (updatef == false) {
				updateP = true;
				updateProject();
				updateP = false;
			}
			changeversiondialog();
		}

	}

	private void changeversiondialog() {
		ChoiceDialog<Library> dialog = new ChoiceDialog<Library>();
		// String dbURL =
		// "jdbc:sqlserver://localhost;databaseName=QL_LIBRARIES_AND_PACKAGES;user=sa;password=sa";
		String selectproject = "select * from Lib_and_package where Lib_id in(select Lib_id from Project_dependency where ID ="
				+ openproject.getId() + ")";
		;
		Connection connect = null;

		try {
			// connect = DriverManager.getConnection(dbURL);
			connect = DriverManager.getConnection(URL);
			Statement sm = connect.createStatement();
			ResultSet rs = sm.executeQuery(selectproject);
			while (rs.next()) {
				Library nlib = new Library(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4));
				dialog.getItems().add(nlib);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		dialog.setTitle("Change version library and package");
		dialog.setHeaderText(null);
		dialog.getDialogPane().getStyleClass().add("dialog");
		dialog.getDialogPane().getStylesheets().add(this.getClass().getResource("application.css").toExternalForm());
		dialog.setContentText("Choose a library (Package):");
		dialog.setWidth(400);
		dialog.setHeight(400);
		Optional<Library> result = dialog.showAndWait();
		result.ifPresent(library -> {
			showEnter(library);
		});
		try {
			connect.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void showEnter(Library lib) {
		Alert alert = new Alert(AlertType.INFORMATION);
		VBox vboxx = new VBox();
		alert.setHeaderText(null);
		alert.getDialogPane().getStyleClass().add("alert");
		alert.getDialogPane().getStylesheets().add(this.getClass().getResource("application.css").toExternalForm());
		TextField tfversion = new TextField();
		Label nlb = new Label("Enter the version: ");
		Label lbif = new Label();
		vboxx.getChildren().addAll(nlb, tfversion, lbif);
		alert.getDialogPane().setContent(vboxx);
		Optional<ButtonType> option = alert.showAndWait();
		if (option.get() == ButtonType.OK) {
			if (tfversion.getText().trim().equals("")) {
				lbif.setText("Please enter version!!");
				alert.show();
			} else {
				if (openproject.getTypeP().contains("NPM")) {
					String cmd = "cmd /c npm i " + lib.getArtifactID().trim() + "@" + tfversion.getText().trim();
					;
					// System.out.println(cmd);
					try {
						Process p = Runtime.getRuntime().exec(cmd, null, new File(openproject.getProjectInfo().trim()));
						BufferedReader r = new BufferedReader(new InputStreamReader(p.getInputStream()));
						if (r.readLine() == null) {
							showerror("Invalid Version!!");
							showEnter(lib);
						} else {
							showinfo("Install sucessfully!!!");
							updateP = true;
							updateProject();
							updateP = false;
						}
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}

				} else if (openproject.getTypeP().contains("MAVEN")) {
					String cmd = "cmd /c mvn clean install";
					boolean changeOK = ManageFileDependency.changeVersionDpMaven(
							openproject.getProjectInfo().trim().concat("\\pom.xml"), lib.getArtifactID().trim(),
							lib.getGroupID().trim(), lib.getVersion().trim(), tfversion.getText().trim());
					if (changeOK == true) {
						try {
							boolean ok = true;
							Process p = Runtime.getRuntime().exec("cmd /c mvn clean install", null,
									new File(openproject.getProjectInfo().trim()));
							BufferedReader r = new BufferedReader(new InputStreamReader(p.getInputStream()));
							String line;
							while (true) {
								line = r.readLine();
								if (line == null) {
									break;
								}
								if (line.contains("WARNING") && line.contains(lib.getArtifactID().trim())
										&& line.contains(tfversion.getText().trim())
										&& line.contains("no dependency")) {
									ManageFileDependency.changeVersionDpMaven(
											openproject.getProjectInfo().trim().concat("\\pom.xml"),
											lib.getArtifactID().trim(), lib.getGroupID().trim(),
											tfversion.getText().trim(), lib.getVersion().trim());
									showerror("INVALID VERSION");
									showEnter(lib);
									ok = false;
									break;
								}
								// System.out.println(line);
							}
							if (ok == true) {
								showinfo("Install Successfully!!");
								updateP = true;
								updateProject();
								updateP = false;
							}
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					} else {
						showerror("Something is wrong");
					}
				} else {
					String cmd = "cmd /c gradle -q dependencies";
					boolean changeOK = ManageFileDependency.removeorchangeDpGradle(
							openproject.getProjectInfo().trim().concat("\\build.gradle"), lib.getArtifactID().trim(),
							lib.getGroupID().trim(), tfversion.getText().trim(), true);
					if(changeOK == true) {
						try {
							boolean ok = true;
							Process p = Runtime.getRuntime().exec("cmd /c gradle -q dependencies", null, new File(openproject.getProjectInfo().trim()));
							BufferedReader r = new BufferedReader(new InputStreamReader(p.getInputStream()));
					        String line;
					        while (true) {
					            line = r.readLine();
					            if (line == null) { break; }
					            if(line.contains(lib.getArtifactID().trim()) && line.contains(lib.getGroupID().trim()) && line.contains(tfversion.getText().trim())
					            		&& line.contains("FAILED")) {
					            	ManageFileDependency.removeorchangeDpGradle(
											openproject.getProjectInfo().trim().concat("\\build.gradle"), lib.getArtifactID().trim(),
											lib.getGroupID().trim(), lib.getVersion().trim(), true);
					            	showerror("Invalid version!");
					            	ok = false;
					            	break;
					            }
					        }
					        if(ok == true) {
					        	showinfo("Install successfully!!");
					        	updateP = true;
					        	updateProject();
					        	updateP = false;
					        }
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					} else {
						showerror("Something is wrong");
					}

				}
			}

		}
	}

	@FXML
	public void showinfoP() {
		if (openproject != null) {
			if(turnviewinfo == true) {
				turnviewinfo = false;
				viewinfobt.setStyle("-fx-background-color: yellow;");
				display.getChildren().remove(infoPlabel);
				return;
			} else {
				turnviewinfo = true;
				viewinfobt.setStyle("-fx-background-color: #1CFC8C;");
			}
			String b = reIDE();
			showInfoProject(b);
		} else {
			showerror("No Project are opened");
		}
	}
	private static String reIDE() {
		String select = "select * from IDE where IDE_id = " + openproject.getIdIDE();
		// System.out.println(project.getIdIDE());
		String nameide = "";
		String vside = "";
		try {
			Connection connect1 = DriverManager.getConnection(URL);
			Statement statement = connect1.createStatement();
			ResultSet rs = statement.executeQuery(select);
			if (rs.next()) {
				nameide = rs.getString(2);
				vside = rs.getString(3);
			}
			connect1.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String b = nameide + "\n		version: " + vside;
		return b;
	}
	public static void setLabel() {
		if(turnviewinfo == true) {
			infoPlabel.setText("		Name: " + openproject.getName() + "\n		Category: " + openproject.getTypeP()
			+ "\n		Location: " + openproject.getProjectInfo() + "\n		Number of Dependency: " + countdp
			+ "\n		Created at: " + openproject.getDateCreate() + "\n		Updated at: " + openproject.getDateUpdate()
			+ "\n		Create by IDE: " + reIDE());
		}
	}
	@FXML
	public void updateProject() {
		if (openproject == null) {
			showerror("No Project are opened");
		} else {
			try {
				if (updatef == false) {
					if (openproject.getTypeP().contains("NPM")) {
						try {
							Process p = Runtime.getRuntime().exec("cmd /c npm i", null,
									new File(openproject.getProjectInfo().trim()));
							BufferedReader r = new BufferedReader(new InputStreamReader(p.getInputStream()));
							String line;
							while (true) {
								line = r.readLine();
								if (line == null) {
									break;
								}
								// System.out.println(line);
							}
						} catch (IOException e) {
						}
						updatef = true;
					} else if (openproject.getTypeP().contains("MAVEN")) {
						String pathname = openproject.getProjectInfo().trim();
						try {
							Process p = Runtime.getRuntime().exec("cmd /c mvn clean install", null, new File(pathname));
							BufferedReader r = new BufferedReader(new InputStreamReader(p.getInputStream()));
							String line;
							while (true) {
								line = r.readLine();
								if (line == null) {
									break;
								}
								// System.out.println(line);
							}
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					} else {
						String pathname = openproject.getProjectInfo().trim();
						try {
							Process p = Runtime.getRuntime().exec("cmd /c gradle -q dependencies", null, new File(pathname));
							BufferedReader r = new BufferedReader(new InputStreamReader(p.getInputStream()));
							String line;
							while (true) {
								line = r.readLine();
								if (line == null) {
									break;
								}
								// System.out.println(line);
							}
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

					}

				}
				Connection connect = DriverManager.getConnection(URL);
				String deletedp = "delete from Project_dependency where ID = " + openproject.getId();
				String updateDate = "update Project set UpdateAt = '" +  java.time.LocalDate.now() + "' where ID = " + openproject.getId();
				Statement stm = connect.createStatement();
				ReadDependency newread = new ReadDependency();
				stm.execute(deletedp);
				stm.execute(updateDate);
				if (openproject.getTypeP().contains("NPM")) {
					String link = openproject.getProjectInfo().trim() + "\\package.json";
					TreeView<Library> newtree = newread.ReadNPM(openproject, link, connect);
					this.displaystackpane.getChildren().clear();
					this.displaystackpane.getChildren().add(newtree);
				} else if (openproject.getTypeP().contains("MAVEN")) {
					String link = openproject.getProjectInfo().trim() + "\\pom.xml";
					TreeView<Library> newtree = newread.ReadMaven(openproject, link, connect);
					this.displaystackpane.getChildren().clear();
					this.displaystackpane.getChildren().add(newtree);
				} else {
					String link = openproject.getProjectInfo().trim() + "\\build.gradle";
					TreeView<Library> newtree = newread.ReadGradle(openproject, link, connect);
					this.displaystackpane.getChildren().clear();
					this.displaystackpane.getChildren().add(newtree);
				}
				if (updateP == false) {
					showinfo("Project has already updated!!");
				}
				connect.close();
			} catch (SQLException e) {
				showerror("Connect fail!!!");
			}
		}
	}

	@FXML
	private void deleteDp() {
		if (openproject != null) {
			updateP = true;
			if (updatef == false) {
				updateProject();
			}
			ChoiceDialog<Library> dialog = new ChoiceDialog<Library>();
			// String dbURL =
			// "jdbc:sqlserver://localhost;databaseName=QL_LIBRARIES_AND_PACKAGES;user=sa;password=sa";
			String selectproject = "select * from Lib_and_package where Lib_id in(select Lib_id from Project_dependency where ID ="
					+ openproject.getId() + ")";
			Connection connect = null;

			try {
				// connect = DriverManager.getConnection(dbURL);
				connect = DriverManager.getConnection(URL);
				Statement sm = connect.createStatement();
				ResultSet rs = sm.executeQuery(selectproject);
				while (rs.next()) {
					Library nlib = new Library(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4));
					dialog.getItems().add(nlib);
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			dialog.setTitle("DELETE DEPENDENCY");
			dialog.setHeaderText(null);
			dialog.getDialogPane().getStyleClass().add("dialog");
			dialog.getDialogPane().getStylesheets()
					.add(this.getClass().getResource("application.css").toExternalForm());
			dialog.setContentText("Choose a library(package) if you want delete :");
			dialog.setWidth(400);
			dialog.setHeight(400);
			Optional<Library> result = dialog.showAndWait();
			result.ifPresent(library -> {
				String pathname = openproject.getProjectInfo().trim();
				if(openproject.getTypeP().contains("NPM")) {
					try {
						Process p = Runtime.getRuntime().exec("cmd /c npm uninstall " + library.getArtifactID().trim(),
								null, new File(pathname));
						BufferedReader r = new BufferedReader(new InputStreamReader(p.getInputStream()));
						String line;
						while (true) {
							line = r.readLine();
							if (line == null) {
								break;
							}
							// System.out.println(line);
						}
						showinfo(library.getArtifactID() + " was removed");
						updateProject();
					} catch (IOException e) {
						showerror(library.getArtifactID() + " has not been deleted");
					}
				} else {
					String linktoFile;
					boolean checkrm;
					if(openproject.getTypeP().contains("MAVEN")) {
						linktoFile = pathname.concat("\\pom.xml");
						checkrm = ManageFileDependency.removeDpMaven(linktoFile,library.getArtifactID().trim(),
								library.getGroupID().trim(), library.getVersion().trim());
					} else {
						linktoFile = pathname.concat("\\build.gradle");
						checkrm = ManageFileDependency.removeorchangeDpGradle(linktoFile,library.getArtifactID().trim(),
								library.getGroupID().trim(), library.getVersion().trim(), false);
					}
					if(checkrm == true) {
						showinfo(library.getArtifactID().trim() + " was removed successfully!");
						updateProject();
					} else {
						showerror("something is wrong");
					}
				}
			});

			updateP = false;
		}
	}

	@FXML
	private void addDp() {
		if (openproject == null) {
			showerror("No Project are openned!!");
		} else {
			if (openproject.getTypeP().contains("NPM")) {
				showAddNPM();
			} else {
				showAddMavenorGradle();
			}
		}
	}

	private boolean checknotfill = false;

	private void showAddNPM() {
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("Add new package (NPM)");
		alert.setHeaderText(null);
		alert.setContentText(null);
		alert.getDialogPane().getStyleClass().add("alert");
		alert.getDialogPane().getStylesheets().add(this.getClass().getResource("application.css").toExternalForm());
		VBox nvbox = new VBox();
		Label namelib = new Label("Name Package: ");
		Label vslib = new Label("Version:"), infolb = new Label();
		if (checknotfill == true) {
			infolb.setText("Please fill all infomation");
			checknotfill = false;
		}
		TextField nametf = new TextField(), versiontf = new TextField();
		Label fill = new Label("(Not fill to install lastest version)");
		nvbox.getChildren().addAll(namelib, nametf, infolb, vslib, versiontf, fill);
		alert.getDialogPane().setContent(nvbox);
		Optional<ButtonType> result = alert.showAndWait();
		if (result.get() == ButtonType.OK) {
			if (nametf.getText().trim().equals("")) {
				checknotfill = true;
				showAddNPM();
			} else {
				Connection connect = null;
				String selectdependence = "select * from Lib_and_package where Lib_id in(select Lib_id from Project_dependency where ID ="
				+ openproject.getId() + ")";
				try {
					connect = DriverManager.getConnection(URL);
					Statement sm = connect.createStatement();
					ResultSet rs = sm.executeQuery(selectdependence);
					while(rs.next()) {
						if(nametf.getText().trim().equals(rs.getString(2))) {
							showerror(nametf.getText().trim() + " has already install");
							return ;
						}
					}
				} catch (Exception e) {
					showerror("failed to connect database");
				}
				try {
					boolean ok = true;
					String cmd;
					if (versiontf.getText().trim().equals("")) {
						cmd = "cmd /c npm i " + nametf.getText().trim();
					} else {
						cmd = "cmd /c npm i " + nametf.getText().trim() + "@" + versiontf.getText().trim();
					}
					// System.out.println(cmd);
					// System.out.println(project.getProjectInfo().trim());
					Process p = Runtime.getRuntime().exec(cmd, null, new File(openproject.getProjectInfo().trim()));
					BufferedReader r = new BufferedReader(new InputStreamReader(p.getInputStream()));
					if (r.readLine() == null) {
						showerror("Invalid Package!!");
					} else {
						updateP = true;
						updateProject();
						updateP = false;
						showinfo("Install sucessfully!!!");
					}
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	};

	private void showAddMavenorGradle() {
		Alert alert = new Alert(AlertType.INFORMATION);
		if (openproject.getTypeP().contains("MAVEN")) {
			alert.setTitle("Add new library (Maven)");
		} else {
			alert.setTitle("Add new library (gradle)");
		}
		alert.setHeaderText(null);
		alert.setContentText(null);
		alert.getDialogPane().getStyleClass().add("alert");
		alert.getDialogPane().getStylesheets().add(this.getClass().getResource("application.css").toExternalForm());
		VBox nvbox = new VBox();
		Label namelib = new Label("ArtifactId: ");
		Label grouplib = new Label("GroupId: ");
		Label vslib = new Label("Version:"), infolb = new Label();
		if (checknotfill == true) {
			infolb.setText("Please fill all infomation");
			checknotfill = false;
		}
		TextField nametf = new TextField(), grouptf = new TextField(), versiontf = new TextField();
		Button choosefile  = new Button("Choose file jar");
		Label linkfilejar = new Label();
		choosefile.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				JFileChooser jfc = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
				jfc.setDialogTitle("Select file manage dependencies");
				jfc.setAcceptAllFileFilterUsed(false);
				FileNameExtensionFilter filter = new FileNameExtensionFilter("jar", "jar");
				jfc.addChoosableFileFilter(filter);
				int returnVal = jfc.showOpenDialog(null);
				linkfilejar.setText(jfc.getSelectedFile().getPath());
			}

        });
		nvbox.getChildren().addAll(namelib, nametf, grouplib, grouptf, vslib, versiontf, infolb);
		if(openproject.getTypeP().contains("MAVEN")) {
			nvbox.getChildren().addAll(choosefile,linkfilejar);
		}
		alert.getDialogPane().setContent(nvbox);
		Optional<ButtonType> result = alert.showAndWait();
		if (result.get() == ButtonType.OK) {
			if (nametf.getText().trim().equals("") || grouptf.getText().trim().equals("")
					|| versiontf.getText().trim().equals("")) {
				checknotfill = true;
				showAddMavenorGradle();
			} else {
				Connection connect = null;
				String selectdependence = "select * from Lib_and_package where Lib_id in(select Lib_id from Project_dependency where ID ="
				+ openproject.getId() + ")";
				try {
					connect = DriverManager.getConnection(URL);
					Statement sm = connect.createStatement();
					ResultSet rs = sm.executeQuery(selectdependence);
					while(rs.next()) {
						if(nametf.getText().trim().equals(rs.getString(2)) && grouptf.getText().trim().equals(rs.getString(3))) {
							showerror(nametf.getText().trim() + "(" +grouptf.getText().trim() + ") have already install");
							return ;
						}
					}
				} catch (Exception e) {
					showerror("failed to connect database");
				}
				if (openproject.getTypeP().contains("MAVEN")) {
					//System.out.print(linkfilejar.getText().trim());
					if(!linkfilejar.getText().trim().equals("")) {
						String install = "cmd /c mvn install:install-file -Dfile="
								+ linkfilejar.getText().trim()
								+ " -DgroupId=" + grouptf.getText().trim()
								+ " -DartifactId=" + nametf.getText().trim()
								+ " -Dversion=" + versiontf.getText().trim() + " -Dpackaging=jar";
						Process p;
						try {
							p = Runtime.getRuntime().exec(install, null,
									new File(openproject.getProjectInfo().trim()));
							BufferedReader r = new BufferedReader(new InputStreamReader(p.getInputStream()));
							String line;
							while (true) {
								line = r.readLine();
								if (line == null) {
									break;
								}
							}
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

					}

					boolean changeOK = ManageFileDependency.addDpMaven(
							openproject.getProjectInfo().trim().concat("\\pom.xml"), nametf.getText().trim(),
							grouptf.getText().trim(), versiontf.getText().trim());
					if (changeOK == true) {
						try {
							boolean ok = true;
							Process p = Runtime.getRuntime().exec("cmd /c mvn clean install", null,
									new File(openproject.getProjectInfo().trim()));
							BufferedReader r = new BufferedReader(new InputStreamReader(p.getInputStream()));
							String line;
							while (true) {
								line = r.readLine();
								if (line == null) {
									break;
								}
								if (line.contains("WARNING") && line.contains(nametf.getText().trim())
										&& line.contains(grouptf.getText().trim()) && line.contains("no dependency")) {
									ManageFileDependency.removeDpMaven(
											openproject.getProjectInfo().trim().concat("\\pom.xml"),
											nametf.getText().trim(), grouptf.getText().trim(),
											versiontf.getText().trim());
									showerror("INVALID LIBRARY: " + nametf.getText().trim() + " : "  + grouptf.getText().trim()
				            				+ " : "+ versiontf.getText().trim());
									ok = false;
									break;
								}
								// System.out.println(line);
							}
							if (ok == true) {
								showinfo("Install Successfully!!");
								updateP = true;
								updateProject();
								updateP = false;
							}
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				} else {
					String cmd = "cmd /c gradle -q dependencies";
					boolean changeOK = ManageFileDependency.addDpGradle(
							openproject.getProjectInfo().trim().concat("\\build.gradle"), nametf.getText().trim(),
							grouptf.getText().trim(), versiontf.getText().trim());
					if(changeOK == true) {
						try {
							boolean ok = true;
							Process p = Runtime.getRuntime().exec("cmd /c gradle -q dependencies", null, new File(openproject.getProjectInfo().trim()));
							BufferedReader r = new BufferedReader(new InputStreamReader(p.getInputStream()));
					        String line;
					        while (true) {
					            line = r.readLine();
					            if (line == null) { break; }
					            if(line.contains(nametf.getText().trim()) && line.contains(grouptf.getText().trim()) && line.contains(versiontf.getText().trim())
					            		&& line.contains("FAILED")) {
					            	ManageFileDependency.removeorchangeDpGradle(
											openproject.getProjectInfo().trim().concat("\\build.gradle"), nametf.getText().trim(),
											grouptf.getText().trim(), versiontf.getText().trim(), false);
					            	showerror("Invalid library: " + nametf.getText().trim() + " : "  + grouptf.getText().trim()
					            				+ " : "+ versiontf.getText().trim());
					            	ok = false;
					            	break;
					            }
					        }
					        if(ok == true) {
					        	showinfo("Install successfully!!");
					        	updateP = true;
					        	updateProject();
					        	updateP = false;
					        }
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					} else {
						showerror("Something is wrong");
					}
				}
			}
		} else {

		}
	}

	private void showWarningAlert() {
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("Delete project!!");
		alert.setHeaderText("Project after deleting cannot be restored. \nAre you sure to delete this project?");
		alert.setContentText(openproject.toString());
		Optional<ButtonType> option = alert.showAndWait();
		if (option.get() == ButtonType.OK) {
			try {
				Connection connect = DriverManager.getConnection(URL);
				String deletedp = "delete from Project_dependency where ID = " + openproject.getId();
				String deleteproject = "delete from Project where ID = " + openproject.getId();
				Statement stm = connect.createStatement();
				stm.execute(deletedp);
				stm.execute(deleteproject);
				showinfo(openproject.getName() + " has been deleted!!");
				openproject = null;
				displaystackpane.getChildren().clear();
				connect.close();
			} catch (SQLException e) {
				showerror("Connect fail!\nProject has not been deleted.");
			}

		}

	}
	@FXML
	private void exitandlogout() {
		FileWriter fwt;
		try {
			fwt = new FileWriter(new File("user.txt"));
			fwt.write("");
			fwt.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Stage st = (Stage) display.getScene().getWindow();
		st.close();
	}
	@FXML
	private void renameProject() {
		if(openproject == null) {
			showerror("No Project are opened");
		} else {
			rename();
		}
	}
	private void rename() {
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("Rename Project");
		alert.setHeaderText(null);
		alert.setContentText(null);
		alert.getDialogPane().getStyleClass().add("alert");
		alert.getDialogPane().getStylesheets().add(this.getClass().getResource("application.css").toExternalForm());
		VBox nvbox = new VBox();
		Label nameprj = new Label("Enter the name: ");
		TextField nametfprj = new TextField();
		alert.getDialogPane().setContent(nvbox);
		nvbox.getChildren().addAll(nameprj, nametfprj);
		Optional<ButtonType> result = alert.showAndWait();
		if(result.get() == ButtonType.OK) {
			if(nametfprj.getText().trim().equals("")) {
				showerror("Please type the name");
				rename();
			} else {
				String rename1 = "update Project set NameProject = '" + nametfprj.getText().trim() + "' where ID = " + openproject.getId();
				String select = "select ID from Project where NameProject = ? and UserID = " + userid;
				Connection connect = null;

				try {
					connect = DriverManager.getConnection(URL);
					Statement sm = connect.createStatement();
					PreparedStatement ptsm = connect.prepareStatement(select);
					ptsm.setString(1, nametfprj.getText());
					ResultSet rs = ptsm.executeQuery();
					if(rs.next()) {
						showerror("Duplicate Name Project");
						rename();
					} else {
						sm.execute(rename1);
						showinfo("Rename Successfully");
						openproject.setName(nametfprj.getText().trim());
						setLabel();
						this.prjInTree.setName(nametfprj.getText().trim());
						displaystackpane.layout();
					}
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
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

	private void showInfoProject(String b) {
//		Alert alert = new Alert(AlertType.INFORMATION);
//		alert.setTitle(openproject.toString());
		String infomation = "		Name: " + openproject.getName() + "\n		Category: " + openproject.getTypeP()
		+ "\n		Location: " + openproject.getProjectInfo() + "\n		Number of Dependency: " + countdp
				+ "\n		Created at: " + openproject.getDateCreate() + "\n		Updated at: " + openproject.getDateUpdate()
				+ "\n		Create by IDE: " + b;
		infoPlabel.setText(infomation);
		infoPlabel.setPrefSize(1020, 340);
		infoPlabel.getStylesheets().add(this.getClass().getResource("application.css").toExternalForm());
		infoPlabel.getStyleClass().add("label1");
		display.getChildren().add(0, infoPlabel);
//		VBox ndialog = new VBox();
//		ndialog.setPrefWidth(500);
//		ndialog.setPrefHeight(200);
//		Label duplicate = new Label(infomation);
//		duplicate.getStylesheets().add(this.getClass().getResource("application.css").toExternalForm());
//		duplicate.getStyleClass().add("info");
//		ndialog.getChildren().add(duplicate);
//		alert.getDialogPane().setContent(ndialog);
//		alert.showAndWait();
	}

	public static void setPrjInTree(Library prjInTree) {
		MainController.prjInTree = prjInTree;
	}

	public static void setOpenproject(Project openproject) {
		MainController.openproject = openproject;
	}

	public static void setURL(String uRL) {
		URL = uRL;
	}

	public static void setUserid(int userid) {
		MainController.userid = userid;
	}
}
