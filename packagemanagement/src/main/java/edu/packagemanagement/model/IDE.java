package edu.packagemanagement.model;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class IDE {
	private String name;
	private String version;
	private int idIDE;
	public IDE(String name, String version) {
		super();
		this.name = name;
		this.version = version;
	}
	public IDE(String name, String version, Connection connect) {
		this.name = name;
		this.version = version;
		String selectide = "select * from IDE where Name_ide = '" + name + "' and Version_ide = '" + version + "'";
		String insertid = "insert into IDE values('" + name + "','" + version + "')";
		Statement sm = null;
		ResultSet rs = null;
		try {
			sm = connect.createStatement();
			rs = sm.executeQuery(selectide);
			if(rs.next()) {
				this.idIDE = rs.getInt(1);
			} else {
				sm.execute(insertid);
				rs = sm.executeQuery(selectide);
				if(rs.next()) {
					this.idIDE = rs.getInt(1);
				}
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getVersion() {
		return version;
	}
	public void setVersion(String version) {
		this.version = version;
	}
	public int getIdIDE() {
		return idIDE;
	}
}
