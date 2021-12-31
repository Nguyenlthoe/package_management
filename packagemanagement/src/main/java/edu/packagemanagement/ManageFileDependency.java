package edu.packagemanagement;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;
import java.util.Stack;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class ManageFileDependency {
	public static boolean removeDpMaven(String path, String name, String groupID, String version) {
		File inputFile = new File(path);
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder = null;
		Document doc = null;
		try {
			dBuilder = dbFactory.newDocumentBuilder();
			doc = dBuilder.parse(inputFile);
		} catch (Exception e) {
			return false;
		}
		doc.getDocumentElement().normalize();
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
		//Element remove = null;
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
				if(name.trim().equals(afID)) {
					if(groupID.trim().equals(grID)) {
						if(version.trim().equals(vsion)) {
							dependencies.removeChild(eElement);
							TransformerFactory transformerFactory = TransformerFactory.newInstance();
							Transformer transformer = null;
							try {
								transformer = transformerFactory.newTransformer();
								DOMSource source = new DOMSource(doc);
								StreamResult result = new StreamResult(inputFile);
								transformer.transform(source, result);
							} catch (Exception e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							break;
						}
					}
				}
			}
		}
		return true;
	}
	public static boolean addDpMaven(String path, String name, String groupID, String version) {
		File inputFile = new File(path);
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder = null;
		Document doc = null;
		try {
			dBuilder = dbFactory.newDocumentBuilder();
			doc = dBuilder.parse(inputFile);
		} catch (Exception e) {
			return false;
		}
		doc.getDocumentElement().normalize();
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
		if(dependencies == null) {
			dependencies = doc.createElement("dependencies");
		}
		Element dependency = doc.createElement("dependency");
		Element groupid = doc.createElement("groupId");
		groupid.appendChild(doc.createTextNode(groupID.trim()));
		Element artifactid = doc.createElement("artifactId");
		artifactid.appendChild(doc.createTextNode(name.trim()));
		Element vs = doc.createElement("version");
		vs.appendChild(doc.createTextNode(version.trim()));
		dependency.appendChild(groupid);
		dependency.appendChild(artifactid);
		dependency.appendChild(vs);
		dependencies.appendChild(dependency);
		TransformerFactory transformerFactory = TransformerFactory.newInstance();
		Transformer transformer = null;
		try {
			transformer = transformerFactory.newTransformer();
			DOMSource source = new DOMSource(doc);
			StreamResult result = new StreamResult(inputFile);
			transformer.transform(source, result);
		} catch (Exception e) {
			return false;
		}
		Scanner sc =  null;
		try {
			sc = new Scanner(new File(path));
			String write = "";
			while(sc.hasNextLine()) {
				String line =sc.nextLine().trim();
				write = write.concat(line);
			}
			FileWriter filewt = new FileWriter(path);
			filewt.write(write);
			filewt.flush();
			Document doc2 = dBuilder.parse(new File(path));
			DOMSource source = new DOMSource(doc2);
			StreamResult result = new StreamResult(inputFile);
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");
			transformer.transform(source, result);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			return false;
		}
		return true;
	}
	public static boolean changeVersionDpMaven(String path, String name, String groupID, String version, String change) {
		//System.out.println(path);
		File inputFile = new File(path);
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder = null;
		Document doc = null;
		//System.out.print("done");
		try {
			dBuilder = dbFactory.newDocumentBuilder();
			doc = dBuilder.parse(inputFile);
			//System.out.print("done");
		} catch (Exception e) {
			return false;
		}
		doc.getDocumentElement().normalize();
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
				if(name.trim().equals(afID)) {
					if(groupID.trim().equals(grID)) {
						if(version.trim().equals(vsion)) {
							//dependencies.removeChild(eElement);
							Element ver = (Element)eElement.getElementsByTagName("version").item(0);
							ver.setTextContent(change.trim());
							TransformerFactory transformerFactory = TransformerFactory.newInstance();
							Transformer transformer = null;
							try {
								transformer = transformerFactory.newTransformer();
								DOMSource source = new DOMSource(doc);
								StreamResult result = new StreamResult(inputFile);
								transformer.transform(source, result);
							} catch (Exception e) {
								e.printStackTrace();
								return false;
							}
							break;
						}
					}
				}
			}
		}
		return true;
	}
	public static boolean addDpNPM(String path, String name, String version) {
		try {
			JSONObject obj = (JSONObject) new JSONParser().parse(new FileReader(path));
			JSONObject dp = (JSONObject) obj.get("dependencies");
			if(dp == null) {
				obj.put("dependencies", new JSONObject());
			}
			dp = (JSONObject) obj.get("dependencies");
			dp.put(name.trim(), version.trim());
			FileWriter file = new FileWriter(path);
			file.write(obj.toJSONString());
			file.flush();
			file.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return true;
	}
	public static boolean removeDpNPM(String path, String name) {
		try {
			JSONObject obj = (JSONObject) new JSONParser().parse(new FileReader(path));
			JSONObject dp = (JSONObject) obj.get("dependencies");
			dp.remove(name.trim());
			FileWriter file = new FileWriter(path);
			file.write(obj.toJSONString());
			file.flush();
			file.close();
		} catch (Exception e) {
			return false;
		}
		return true;
	}
	public static boolean changeDpNPM(String path, String name, String version) {
		try {
			JSONObject obj = (JSONObject) new JSONParser().parse(new FileReader(path));
			JSONObject dp = (JSONObject) obj.get("dependencies");
			dp.replace(name, version);
			FileWriter file = new FileWriter(path);
			file.write(obj.toJSONString());
			file.flush();
			file.close();
		} catch (Exception e) {
			return false;
		}
		return true;
	}
	public static boolean addDpGradle(String path, String name, String group, String version) {
		Scanner sc =  null;
		try {
			sc = new Scanner(new File(path));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			return false;
		}
		String sfile = "";
		Stack<String> check = new Stack<String>();
		while (sc.hasNextLine()) {
			String ss = sc.nextLine();

			sfile = sfile.concat(ss + "\n");
			ss = ss.trim();
			if (ss.equals("dependencies {") && check.empty() == true) {
				sfile = sfile.concat("    implementation '" + group.trim() + ":"
						+ name.trim() + ":" + version.trim() + "'\n");
			}
			if(ss.contains("{")) {
				check.add("{");
			}
			if(ss.contains("}")) {
				check.pop();
			}
		}
		try {
			FileWriter filewrite = new FileWriter(path);
			filewrite.write(sfile);
			filewrite.flush();
			filewrite.close();
		} catch (IOException e) {
			return false;
		}

		return true;
	}
	public static boolean removeorchangeDpGradle(String path, String name, String group, String version,boolean change) {
		Scanner sc =  null;
		try {
			sc = new Scanner(new File(path));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			return false;
		}
		String sfile = "";
		Stack<String> check = new Stack<String>();
		while (sc.hasNextLine()) {
			String ss = sc.nextLine();

			sfile = sfile.concat(ss + "\n");
			ss = ss.trim();
			if (ss.equals("dependencies {") && check.empty() == true) {
				while(true) {
					String elm = sc.nextLine();
					String elmtrim = elm.trim();
					if(elmtrim.equals("")) {
						continue;
					} else if(elmtrim.charAt(elmtrim.length() - 1) == '}') {
						sfile = sfile.concat(elm + "\n");
						break;
					}
					String[] nelm = elmtrim.split("//");
					elmtrim = nelm[0];
					String[] words = elmtrim.split("[']");
					if(words.length == 2) {
						String[] infos = words[1].split("[:]");
						if(name.trim().equals(infos[1])) {
							if(group.trim().equals(infos[0])) {
								if(change == true) {
									sfile = sfile.concat("    implementation '" + group.trim() + ":"
											+ name.trim() + ":" + version.trim() + "'\n");
									continue;
								} else {
									if(version.trim().equals(infos[2])) {
										continue;
									}
								}
							}
						}
					} else if ( words.length == 6) {
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
						if(name.trim().equals(afID)) {
							if(group.trim().equals(grID)) {
								if(change == true) {
									sfile = sfile.concat("    implementation '" + group.trim() + ":"
											+ name.trim() + ":" + version.trim() + "'\n");
									continue;
								} else {
									if(version.trim().equals(vers)) {
										continue;
									}
								}
							}
						}
					}
					sfile = sfile.concat(elm + "\n");
				}
			}
			if(ss.contains("{")) {
				check.add("{");
			}
			if(ss.contains("}")) {
				check.pop();
			}
		}
		try {
			FileWriter filewrite = new FileWriter(path);
			filewrite.write(sfile);
			filewrite.flush();
			filewrite.close();
		} catch (IOException e) {
			return false;
		}

		return true;
	}
}
