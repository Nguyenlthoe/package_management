package edu.packagemanagement.model;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class Project {
	private String name;
	private int id;
	private String dateCreate;
	private String dateUpdate;
	private String projectInfo;
	private String typeP;
	ArrayList<String> idLibs = new ArrayList<String>();
	private int idIDE;
	
	public void addLibDependency(String newid) {
		this.idLibs.add(newid);
	}
	public Project( int id, String name, String projectInfo, String typeP, int idIDE, String dateCreate, String dateUpdate) {
		super();
		this.name = name;
		this.id = id;
		this.dateCreate = dateCreate;
		this.dateUpdate = dateUpdate;
		this.projectInfo = projectInfo;
		this.typeP = typeP;
		this.idIDE = idIDE;
	}
	public Project(String name, String projectInfo, String typeP, Connection connect) {
		this.name = name;
		this.projectInfo = projectInfo;
		this.typeP = typeP;
		this.idIDE = 1;
		String a = java.time.LocalDate.now() + "";
		this.dateCreate = a.replace('-', '/');
		this.dateUpdate = a.replace('-', '/');
		String select = "select ID from Project where NameProject = '" + name + "' and UserID = 1";
		String insert = "insert into Project \n values('" + name + "', '" + projectInfo + "', '"
					+ typeP + "', 1 , '" + dateCreate + "', '" + dateUpdate + "', 1)";
		Statement statement = null;
		ResultSet rs = null;
		try {
			statement = connect.createStatement();
			rs = statement.executeQuery(select);
			if(!rs.next()) {
				statement.executeUpdate(insert);
			}
			rs = statement.executeQuery(select);
			while(rs.next()) {
				this.id= rs.getInt(1);
				
				//System.out.println("idIDE: "+ idIDE);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public Project(String name, String dateCreate, String projectInfo, int idIDE, Connection connect) {
		super();
		this.name = name;
		this.dateCreate = dateCreate;
		this.projectInfo = projectInfo;
		this.dateUpdate = dateCreate;
		this.idIDE = idIDE;
	}
	public Project() {
		// TODO Auto-generated constructor stub
	}
	public String getName() {
		return name;
	}
	public int getIdIDE() {
		return idIDE;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getId() {
		return id;
	}
	public String getDateCreate() {
		return dateCreate;
	}
	public String getDateUpdate() {
		return dateUpdate;
	}
	public String getProjectInfo() {
		return projectInfo;
	}

	public String getTypeP() {
		return typeP;
	}
	public ArrayList<String> getIdLibs() {
		return idLibs;
	}
	public String toString() {
		return this.name + " -- " + this.typeP;
	}
}
