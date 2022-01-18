package edu.packagemanagement.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class Library {
	private String name = null;
	private String version;
	private String artifactID;
	private String groupID;
	private int idLib;
	private int stt;
	private boolean exited = false;
	ArrayList<String> idLibs = new ArrayList<String>();

	public Library(String name, String version, Connection connect) {
		super();
		this.name = name;
		this.version = version;
		String select = "select Lib_id from Lib_and_Package where NameLib = '" + name
				+ "' and version_lib = '" + version + "'";
		String insert = "insert into Lib_and_Package \n values('" + name + "', null, '" + version + "')";
		Statement statement = null;
		ResultSet rs = null;
		try {
			statement = connect.createStatement();
			rs = statement.executeQuery(select);
			if(!rs.next()) {
				statement.executeUpdate(insert);
			} else {
				this.exited = true;
			}
			rs = statement.executeQuery(select);
			while(rs.next()) {
				this.idLib = rs.getInt(1);

				//System.out.println("idIDE: "+ idIDE);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public Library(String name, String version,PreparedStatement ptsm1, PreparedStatement ptsm2) {
		super();
		this.name = name;
		this.version = version;
		ResultSet rs = null;
		try {
			ptsm1.setString(1, name);
			ptsm1.setString(2, version);
			rs = ptsm1.executeQuery();
			if(!rs.next()) {
				ptsm2.setString(1, name);
				ptsm2.setString(2, version);
				ptsm2.execute();
			} else {
				exited = true;
			}
			rs = ptsm1.executeQuery();
			if(rs.next()) {
				this.idLib = rs.getInt(1);
			}


		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	public Library(String name, String version,PreparedStatement ptsm1) {
		super();
		this.name = name;
		this.version = version;
		this.exited = true;
		ResultSet rs = null;
		try {
			ptsm1.setString(1, name);
			ptsm1.setString(2, version);
			rs = ptsm1.executeQuery();
			if(rs.next()) {
				this.idLib = rs.getInt(1);
			}


		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public Library(String name, String version) {
		super();
		this.name = name;
		this.version = version;
	}
	public Library(int id, String name, String version) {
		super();
		this.name = name;
		this.version = version;
		this.idLib = id;
	}
	public Library(String artifactID, String groupID, String version) {
		this.version = version;
		this.artifactID = artifactID;
		this.groupID = groupID;
	}
	public Library(int id,String artifactID, String groupID, String version) {
		this.version = version;
		this.artifactID = artifactID;
		this.groupID = groupID;
		this.idLib = id;
	}
	public Library(String artifactID, String groupID, String version, PreparedStatement ptsm1) {
		super();
		this.artifactID = artifactID;
		this.version = version;
		this.groupID = groupID;
		this.exited = true;
		ResultSet rs = null;
		try {
			ptsm1.setString(1, artifactID);
			ptsm1.setString(2, version);
			ptsm1.setString(3, groupID);
			rs = ptsm1.executeQuery();
			if(rs.next()) {
				this.idLib = rs.getInt(1);
			}


		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	public Library(String artifactID, String groupID, String version, PreparedStatement ptsm1, PreparedStatement ptsm2) {
		this.version = version;
		this.artifactID = artifactID;
		this.groupID = groupID;
		ResultSet rs = null;
		try {
			ptsm1.setString(1, artifactID);
			ptsm1.setString(2, version);
			ptsm1.setString(3, groupID);
			rs = ptsm1.executeQuery();
			if(!rs.next()) {
				ptsm2.setString(1, artifactID);
				ptsm2.setString(2, groupID);
				ptsm2.setString(3, version);
				ptsm2.execute();
			} else {
				exited = true;
			}
			rs = ptsm1.executeQuery();
			if(rs.next()) {
				this.idLib = rs.getInt(1);
			}


		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public Library() {
		// TODO Auto-generated constructor stub
	}
	public boolean isExited() {
		return exited;
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
	public String getArtifactID() {
		return artifactID;
	}
	public void setArtifactID(String artifactID) {
		this.artifactID = artifactID;
	}
	public String getGroupID() {
		return groupID;
	}
	public void setGroupID(String groupID) {
		this.groupID = groupID;
	}
	public int getStt() {
		return stt;
	}
	public void setStt(int stt) {
		this.stt = stt;
	}
	public int getIdLib() {
		return idLib;
	}
	public ArrayList<String> getIdLibs() {
		return idLibs;
	}
	@Override
	public String toString() {
		if(this.name == null) {
			if(this.groupID == null) {
				return this.artifactID + " : " + this.version;
			} else {
				return this.artifactID + " : " + this.version + " : " + this.groupID;
			}
		}
		return this.name + " : "+ this.version ;
	}
	public boolean equals(Library y) {
		if(this.name != null) {
			if(y.getName().equals(this.name) && y.getVersion().equals(version)) {
				return true;
			} else {
				return false;
			}
		}
		if(y.getArtifactID().equals(this.artifactID)) {
			if(y.getGroupID() == null) {
				if(y.getVersion().equals(this.version)) {
					return true;
				} else {
					return false;
				}
			}
			if(y.getGroupID().equals(this.groupID)) {
				if(y.getVersion().equals(this.version)) {
					return true;
				}
			}
		}
		return false;
	}
}
