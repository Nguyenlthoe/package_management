package edu.packagemanagement;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.Scanner;
import java.util.Stack;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import edu.packagemanagement.model.Library;
import edu.packagemanagement.model.Project;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
public class ReadDependency {
	private ImageView images = null;
	private Label iconnpm = null;
	private ArrayList<TreeItem<Library>> libitem = new ArrayList<TreeItem<Library>>();
	private ArrayList<Library> listlib =  new ArrayList<Library>();
	private static String mavenrepo = "C:\\Users\\APC\\.m2\\repository";
	private static String gradlerepo = "C:\\Users\\APC\\.gradle\\caches\\modules-2\\files-2.1";
	public TreeView<Library> ReadNPM(Project a, String link, Connection connect) {
		Library rootP = new Library(a.getName(), "NPM");
		PreparedStatement ptsm1 = null, ptsm2 = null, ptsm3 = null, ptsm4 = null;
		TreeItem<Library> newTree = new TreeItem<Library>(rootP);
		Tooltip newtoolt = new Tooltip();
		newtoolt.setText("Project name: " + a.getName() + "\nType of Project: NPM \nLocation: " + link);
		newtoolt.getStyleClass().add("tooltip");
		String selectlib = "select Lib_id from Lib_and_Package where NameLib = ? and version_lib = ?";
		String insertlib = "insert into Lib_and_Package \n values(? , null , ?)";
		String insertdependency = "insert into Lib_dependency \n values(? , ?)";
		String insertPrjdependency = "insert into Project_dependency \n values(? , ?)";
		try {
			ptsm1 = connect.prepareStatement(selectlib);
			ptsm2 = connect.prepareStatement(insertlib);
			ptsm3 = connect.prepareStatement(insertdependency);
			ptsm4 = connect.prepareStatement(insertPrjdependency);
		} catch(Exception e) {

		}
		newtoolt.getStyleClass().add(this.getClass().getResource("application.css").toExternalForm());
		newTree.setExpanded(true);
		TreeView<Library> newRoot = new TreeView<Library>(newTree);
		Object obj;
		String pathNode =a.getProjectInfo().concat("\\node_modules");
		//System.out.println(pathNode);
		Queue<Library> queuepk = new LinkedList<Library>();
		try {
			obj = new JSONParser().parse(new FileReader(link));
			JSONObject jsonObject = (JSONObject) obj;
			if (jsonObject.get("dependencies") == null) {

			} else {
				Map address = ((Map) jsonObject.get("dependencies"));
				Iterator<Map.Entry> itr1 = address.entrySet().iterator();
				while (itr1.hasNext()) {
					Map.Entry pair = itr1.next();
					//Library nlib = new Library(pair.getKey() + "", pair.getValue() + "",connect);
					Library nlib = new Library(pair.getKey() + "", pair.getValue() + "",ptsm1, ptsm2);
					ptsm4.setInt(1, a.getId());
					ptsm4.setInt(2, nlib.getIdLib());
					ptsm4.execute();
					listlib.add(nlib);
					nlib.setStt(listlib.size() - 1);
					TreeItem nitem = new TreeItem<Library>(nlib,new ImageView(new Image(new FileInputStream(new File("Npm-logo.png")))));
					libitem.add(nitem);
					newTree.getChildren().add(libitem.get(libitem.size() - 1));
					queuepk.offer(nlib);
				}

				while (!queuepk.isEmpty()) {
					Library x = queuepk.poll();
					try {
						File c = new File(pathNode.concat("\\" + x.getName() + "\\package.json"));
					} catch(Exception e) {
						continue;
					}
					Object newoj = null;
					try{
						newoj = new JSONParser()
							.parse(new FileReader(pathNode.concat("\\" + x.getName() + "\\package.json")));
					} catch(Exception e) {
						continue;
					}
					JSONObject jsonObject1 = (JSONObject) newoj;
					if (jsonObject1.get("dependencies") == null) {
						continue;
					}
					Map address2 = ((Map) jsonObject1.get("dependencies"));
					// đọc address Map
					Iterator<Map.Entry> itr2 = address2.entrySet().iterator();
					if (itr2.hasNext() == false) {
						continue;
					}
					while(itr2.hasNext()) {
						Map.Entry pair = itr2.next();
						Library nlib = null;
						if(x.isExited()) {
							nlib = new Library(pair.getKey() + "", pair.getValue() + "",ptsm1);
						} else {
							nlib = new Library(pair.getKey() + "", pair.getValue() + "",ptsm1, ptsm2);
							ptsm3.setInt(1, x.getIdLib());
							ptsm3.setInt(2, nlib.getIdLib());
							ptsm3.execute();
						}
						//Library nlib = new Library(pair.getKey() + "", pair.getValue() + "", connect);
						listlib.add(nlib);
						nlib.setStt(listlib.size() - 1);
						TreeItem nitem = new TreeItem<Library>(nlib,new ImageView(new Image(new FileInputStream(new File("Npm-logo.png")))));
						libitem.add(nitem);
						libitem.get(x.getStt()).getChildren().add(nitem);
						queuepk.offer(nlib);
					}
				}
			}

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}


		newRoot.getStyleClass().add("tree");
		String css = this.getClass().getResource("application.css").toExternalForm();
		newRoot.getStylesheets().add(css);
		//System.out.println(newRoot.toString());
		return newRoot;
	}

	public TreeView<Library> ReadMaven(Project a, String link, Connection connect) {
		Library rootP = new Library(a.getName(), "MAVEN");
		TreeItem<Library> newTree = new TreeItem<Library>(rootP);
		newTree.setExpanded(true);
		TreeView<Library> newRoot = new TreeView<Library>(newTree);
		Queue<Library> queuelib = new LinkedList<Library>();
		String pathNode = mavenrepo;
		File inputFile = new File(link);
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		PreparedStatement ptsm1 = null, ptsm2 = null, ptsm3 = null, ptsm4 = null;
		DocumentBuilder dBuilder = null;
		Document doc = null;
		String selectlib = "select Lib_id from Lib_and_Package where NameLib = ? and version_lib = ? and Group_id = ?";
		String insertlib = "insert into Lib_and_Package \n values(? , ? , ?)";
		String insertdependency = "insert into Lib_dependency \n values(? , ?)";
		String insertPrjdependency = "insert into Project_dependency \n values(? , ?)";
		try {
			ptsm1 = connect.prepareStatement(selectlib);
			ptsm2 = connect.prepareStatement(insertlib);
			ptsm3 = connect.prepareStatement(insertdependency);
			ptsm4 = connect.prepareStatement(insertPrjdependency);
			dBuilder = dbFactory.newDocumentBuilder();
			doc = dBuilder.parse(inputFile);
		} catch(Exception e) {

		}

		doc.getDocumentElement().normalize();

		// System.out.println("Root element :" +
		// doc.getDocumentElement().getNodeName());
		Element dependencies = null;
		for (int i = 0; i < 100; i++) {
			if(doc.getElementsByTagName("dependencies").item(i) == null) {
				break;
			}
			if (doc.getElementsByTagName("dependencies").item(i).getParentNode().toString().equals("[project: null]")) {
				dependencies = (Element) doc.getElementsByTagName("dependencies").item(i);
				break;
			}
		}
		if(dependencies != null) {
			NodeList nList = dependencies.getElementsByTagName("dependency");
			for(int tmp = 0; tmp < nList.getLength(); tmp++) {
				Node nNode = nList.item(tmp);
				if(nNode.getNodeType() == Node.ELEMENT_NODE) {
					Element eElement = (Element) nNode;
					if (eElement.getElementsByTagName("scope").item(0) != null) {
						if (eElement.getElementsByTagName("scope").item(0).getTextContent().equals("test")) {
							continue;
						}
						if (eElement.getElementsByTagName("scope").item(0).getTextContent().equals("provided")) {
							continue;
						}
					}
					String grID = eElement.getElementsByTagName("groupId").item(0).getTextContent();
					String afID = eElement.getElementsByTagName("artifactId").item(0).getTextContent();
					// System.out.println(afID);
					String vsion = eElement.getElementsByTagName("version").item(0).getTextContent();
					int len = vsion.length() - 1;
					if (vsion.substring(0, 2).equals("${") && vsion.charAt(len) == '}') {
						vsion = vsion.substring(2, len);
						vsion = doc.getElementsByTagName(vsion).item(0).getTextContent();
					}
					Library nlib = new Library(afID, grID, vsion, ptsm1, ptsm2);
					try {
						ptsm4.setInt(1, a.getId());
						ptsm4.setInt(2, nlib.getIdLib());
						ptsm4.execute();
					} catch(SQLException e) {

					}
					listlib.add(nlib);
					nlib.setStt(listlib.size() - 1);

					TreeItem nitem = null;
					try {
						nitem = new TreeItem<Library>(nlib,new ImageView(new Image(new FileInputStream(new File("icon-logo.png")))));
					} catch (FileNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					libitem.add(nitem);
					newTree.getChildren().add(libitem.get(libitem.size() - 1));
					queuelib.offer(nlib);
				}
			}
			while(!queuelib.isEmpty()) {
				Library x = queuelib.poll();
				String[] groupidd = x.getGroupID().split("[.]");
				String libRoute = "\\";
				for (int i = 0; i < groupidd.length; i++) {
					libRoute = libRoute.concat(groupidd[i] + "\\");
				}
				libRoute = libRoute.concat(x.getArtifactID() + "\\" + x.getVersion() + "\\" + x.getArtifactID() + "-" + x.getVersion());
				ArrayList<Library> liblist = readPomFile(pathNode + libRoute + ".pom", x, ptsm1, ptsm2);
				if(liblist == null) {
					continue;
				}
				if(!x.isExited()) {
					for(int tmp = 0; tmp < liblist.size(); tmp++) {
						try {
							ptsm3.setInt(1, x.getIdLib());
							ptsm3.setInt(2, liblist.get(tmp).getIdLib());
							ptsm3.execute();
						} catch (SQLException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
							System.out.println("loi dong 263");
						}

					}
				}
				for(int tmp = 0; tmp < liblist.size(); tmp++) {
					listlib.add(liblist.get(tmp));
					liblist.get(tmp).setStt(listlib.size() - 1);

					TreeItem nitem = null;
					try {
						nitem = new TreeItem<Library>(liblist.get(tmp),new ImageView(new Image(new FileInputStream(new File("icon-logo.png")))));
					} catch (FileNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					libitem.add(nitem);
					libitem.get(x.getStt()).getChildren().add(libitem.get(libitem.size() - 1));
					queuelib.offer(liblist.get(tmp));
				}
			}
		}
		newRoot.getStyleClass().add("tree");
		String css = this.getClass().getResource("application.css").toExternalForm();
		newRoot.getStylesheets().add(css);
		//System.out.println(newRoot.toString());
		return newRoot;
	}

	public TreeView<Library> ReadGradle(Project a, String link, Connection connect) {
		Library rootP = new Library(a.getName(), "GRADLE");
		TreeItem<Library> newTree = new TreeItem<Library>(rootP);
		newTree.setExpanded(true);
		TreeView<Library> newRoot = new TreeView<Library>(newTree);
		Queue<Library> queuelib = new LinkedList<Library>();
		String pathNode = gradlerepo;
		PreparedStatement ptsm1 = null, ptsm2 = null, ptsm3 = null, ptsm4 = null;
		String selectlib = "select Lib_id from Lib_and_Package where NameLib = ? and version_lib = ? and Group_id = ?";
		String insertlib = "insert into Lib_and_Package \n values(? , ? , ?)";
		String insertdependency = "insert into Lib_dependency \n values(? , ?)";
		String insertPrjdependency = "insert into Project_dependency \n values(? , ?)";
		try {
			ptsm1 = connect.prepareStatement(selectlib);
			ptsm2 = connect.prepareStatement(insertlib);
			ptsm3 = connect.prepareStatement(insertdependency);
			ptsm4 = connect.prepareStatement(insertPrjdependency);
		} catch(Exception e) {

		}

		Scanner sc =  null;
		try {
			sc = new Scanner(new File(link));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			return newRoot;
		}
		Stack<String> check = new Stack<String>();
		while (sc.hasNextLine()) {
			String an = sc.nextLine();
			String ss = an.trim();
			if (ss.equals("dependencies {") && check.empty() == true) {
				while (true) {
					String elm = sc.nextLine();
					elm = elm.trim();
					if (elm.equals("")) {
						continue;
					} else {
						if (elm.charAt(elm.length() - 1) == '}') {
							break;
						}
					}
					String[] nelm = elm.split("//");
					elm= nelm[0];
					String[] words = elm.split("[']");
					if(words[0].contains("test")) {
						continue;
					}
					Library nlib = null;
					boolean checkk = false;
					if(words.length == 2) {
						String[] infos = words[1].split("[:]");
						nlib = new Library(infos[1], infos[0], infos[2],ptsm1,ptsm2);
						checkk = true;
						try {
							ptsm4.setInt(1, a.getId());
							ptsm4.setInt(2, nlib.getIdLib());
							ptsm4.execute();
						} catch(SQLException e) {

						}
					} else if(words.length == 6){
						checkk = true;
						String afID = "";
						String grID = "";
						String vers = "";
						if(words[0].contains("group")) {
							grID = words[1];
						} else if (words[0].contains("name")){
							afID = words[1];
						} else {
							vers = words[1];
						}
						if(words[2].contains("name")) {
							afID = words[3];
						} else if (words[0].contains("group")){
							grID = words[3];
						} else {
							vers = words[3];
						}
						if(words[4].contains("version")) {
							vers = words[5];
						} else if (words[4].contains("name")){
							afID = words[5];
						} else {
							grID = words[4];
						}
						//nlib = new Library(afID, grID, vers);
						nlib = new Library(afID, grID, vers, ptsm1, ptsm2);
						try {
							ptsm4.setInt(1, a.getId());
							ptsm4.setInt(2, nlib.getIdLib());
							ptsm4.execute();
						} catch(SQLException e) {

						}
					}
					if(checkk == false) {
						continue;
					}
					listlib.add(nlib);
					nlib.setStt(listlib.size() - 1);
					TreeItem nitem = null;
					try {
						nitem = new TreeItem<Library>(nlib,new ImageView(new Image(new FileInputStream(new File("icon-logo.png")))));
					} catch (FileNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					libitem.add(nitem);
					newTree.getChildren().add(libitem.get(libitem.size() - 1));
					queuelib.offer(nlib);
				}
			}
			if(ss.contains("{")) {
				check.add("{");
			}
			if(ss.contains("}")) {
				check.pop();
			}
		}
		while(!queuelib.isEmpty()) {
			Library x = queuelib.poll();
			String libRoute = "\\" + x.getGroupID() + "\\" + x.getArtifactID() + "\\" + x.getVersion();
			File fileExplore = new File(pathNode + libRoute);
			//System.out.println(fileExplore.getPath());
			File[] children = fileExplore.listFiles();
			if(children == null) {
				continue;
			}
			String linkPom = "";
			for(int tmp = 0; tmp < children.length; tmp++) {
				File nfile = new File(children[tmp].getPath());
				//System.out.println(nfile + ":::");
				File[] nchildren = nfile.listFiles();
				if(nchildren == null) {
					continue;
				}
				for(int tmp2 = 0; tmp2 < nchildren.length; tmp2++) {
					String lfile = nchildren[tmp2].getPath();
					//System.out.println(lfile + "");
					if(lfile.substring(lfile.length() - 3, lfile.length()).equals("pom")) {
						linkPom = lfile;
						break;
					}
				}
			}
			//libRoute = children[0].getPath() + "\\"+ x.getArtifactID() + "-"+ x.getVersion() + ".pom";
			ArrayList<Library> liblist = readPomFile(linkPom, x, ptsm1, ptsm2);
			if(liblist == null) {
				continue;
			}
			if(!x.isExited()) {
				for(int tmp = 0; tmp < liblist.size(); tmp++) {
					try {
						ptsm3.setInt(1, x.getIdLib());
						ptsm3.setInt(2, liblist.get(tmp).getIdLib());
						ptsm3.execute();
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						//System.out.println(x.getIdLib());
						//System.out.println(liblist.get(tmp).getIdLib());
						e.printStackTrace();
						System.out.println("loi dong 500");
					}

				}
			}
			for(int tmp = 0; tmp < liblist.size(); tmp++) {
				listlib.add(liblist.get(tmp));
				liblist.get(tmp).setStt(listlib.size() - 1);
				TreeItem nitem = null;
				try {
					nitem = new TreeItem<Library>(liblist.get(tmp),new ImageView(new Image(new FileInputStream(new File("icon-logo.png")))));
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				libitem.add(nitem);
				libitem.get(x.getStt()).getChildren().add(libitem.get(libitem.size() - 1));
				queuelib.offer(liblist.get(tmp));
			}
		}
		newRoot.getStyleClass().add("tree");
		String css = this.getClass().getResource("application.css").toExternalForm();
		newRoot.getStylesheets().add(css);
		//System.out.println(newRoot.toString());
		return newRoot;
	}
	private ArrayList<Library> readPomFile(String linkfile, Library x, PreparedStatement ptsm1, PreparedStatement ptsm2){
		ArrayList<Library> listlibrary = new ArrayList<Library>();
		File inputFile1 = null;
		//System.out.print(x.getArtifactID());
		DocumentBuilderFactory dbFactory1 = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder1 = null;
		Document doc1 = null;
		try {
			inputFile1= new File(linkfile);
			//System.out.println("done1");

			//System.out.println("done2");
			dBuilder1 = dbFactory1.newDocumentBuilder();
			doc1 = dBuilder1.parse(inputFile1);
			//System.out.println("done3");
		} catch(Exception e) {
			return null;
		}


		//System.out.println("done4");
		doc1.getDocumentElement().normalize();

		Element dependencies1 = null;
		for (int i = 0; i < 100; i++) {
			if(doc1.getElementsByTagName("dependencies").item(i) == null) {
				break;
			}
			if (doc1.getElementsByTagName("dependencies").item(i).getParentNode().toString().equals("[project: null]")) {
				dependencies1 = (Element) doc1.getElementsByTagName("dependencies").item(i);
				break;
			}
		}
		if (dependencies1 == null) {
			return null;
		}
		if (doc1.getElementsByTagName("dependency").item(0) == null) {
			return null;
		}
		NodeList nList1 = doc1.getElementsByTagName("dependency");
		for (int temp = 0; temp < nList1.getLength(); temp++) {
			Node nNode = nList1.item(temp);
			if (nNode.getNodeType() == Node.ELEMENT_NODE) {
				Element eElement = (Element) nNode;
				// System.out.println(nNode.getTextContent());
				if (eElement.getElementsByTagName("scope").item(0) != null) {
					if (eElement.getElementsByTagName("scope").item(0).getTextContent().equals("test")
							|| eElement.getElementsByTagName("scope").item(0).getTextContent()
									.equals("provided")) {
						continue;
					}
				}
				//System.out.println("1");
				String grID = eElement.getElementsByTagName("groupId").item(0).getTextContent();
				//System.out.println("" + grID);
				String afID = eElement.getElementsByTagName("artifactId").item(0).getTextContent();
				//System.out.println("" + afID);
				if (eElement.getElementsByTagName("version").item(0) == null) {
					continue;
				}
				String vsion = eElement.getElementsByTagName("version").item(0).getTextContent();
				//System.out.println(vsion);
				int len = vsion.length() - 1;
				if (vsion.substring(0, 2).equals("${") && vsion.charAt(len) == '}') {
					vsion = vsion.substring(2, len);
					if (doc1.getElementsByTagName(vsion).item(0) == null) {
						continue;
					}
					vsion = doc1.getElementsByTagName(vsion).item(0).getTextContent();
				}
				Library nlib;
				if(x.isExited()) {
					nlib = new Library(afID, grID, vsion,ptsm1);
				} else {
					nlib = new Library(afID, grID, vsion,ptsm1, ptsm2);
				}
				if(nlib.equals(x)) {
					continue;
				}
				listlibrary.add(nlib);
			}
		}
		//System.out.println("done");
		return listlibrary;
	}
}
