package edu.packagemanagement.model;

public class IDE {
	private String name;
	private String version;
	private int idIDE;
	private static int ideCount = 0; 
	public IDE(String name, String version) {
		super();
		this.name = name;
		this.version = version;
		this.ideCount++;
		this.idIDE = this.ideCount;
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
