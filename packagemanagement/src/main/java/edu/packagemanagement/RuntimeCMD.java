package edu.packagemanagement;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

public class RuntimeCMD {

	public static boolean runNpmInstall(String namelib, String vslib, String linktoProject) {
		String cmd;
		if(namelib.equals("")) {
			cmd = "cmd /c npm i";
		} else if (vslib == null) {
			cmd = "cmd /c npm i " + namelib;
		} else {
			cmd = "cmd /c npm i " + namelib + "@" + vslib;
		}
		try {
			Process p = Runtime.getRuntime().exec(cmd, null, new File(linktoProject));
			BufferedReader r = new BufferedReader(new InputStreamReader(p.getInputStream()));
			if (r.readLine() == null) {
				return false;
			} else {
				String line;
				while(true) {
					line  = r.readLine();
					if(line == null) {
						break;
					}
				}
				return true;
			}
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		return false;
	}
	public static boolean runNpmRemove(String namelib,String linktoProject) {
		String cmd = "cmd /c npm uninstall " + namelib;
		try {
			Process p = Runtime.getRuntime().exec(cmd, null, new File(linktoProject));
			BufferedReader r = new BufferedReader(new InputStreamReader(p.getInputStream()));
			if (r.readLine() == null) {
				return false;
			} else {
				String line;
				while(true) {
					line  = r.readLine();
					if(line == null) {
						break;
					}
				}
				return true;
			}
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		return false;
	}
	public static boolean runMavenInstall(String name, String groupid, String version, String linktoProject) {
		try {
			Process p = Runtime.getRuntime().exec("cmd /c mvn clean install", null,
					new File(linktoProject));
			BufferedReader r = new BufferedReader(new InputStreamReader(p.getInputStream()));
			String line;
			while (true) {
				line = r.readLine();
				if (line == null) {
					break;
				}
				if (line.contains("WARNING") && line.contains(name)
						&& line.contains(groupid)
						&& line.contains("no dependency")) {
					return false;
				}
			}
			return true;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}
	public static boolean runMavenInstallnewliblocal(String linkfilejar, String name, String groupid, String version, String linktoProject) {
		String install = "cmd /c mvn install:install-file -Dfile="
				+ linkfilejar
				+ " -DgroupId=" + groupid.trim()
				+ " -DartifactId=" + name.trim()
				+ " -Dversion=" + version.trim() + " -Dpackaging=jar";
		Process p;
		try {
			p = Runtime.getRuntime().exec(install, null,
					new File(linktoProject.trim()));
			BufferedReader r = new BufferedReader(new InputStreamReader(p.getInputStream()));
			String line;
			while (true) {
				line = r.readLine();
				if (line == null) {
					break;
				}
			}
			return true;
		} catch (IOException e) {
			return false;
		}
	}
	public static boolean runGradleInstall(String name, String groupid, String version, String linktoProject) {
		try {
			Process p = Runtime.getRuntime().exec("cmd /c gradle -q dependencies", null, new File(linktoProject.trim()));
			BufferedReader r = new BufferedReader(new InputStreamReader(p.getInputStream()));
	        String line;
	        while (true) {
	            line = r.readLine();
	            if (line == null) { break; }
	            if(line.contains(name.trim()) && line.contains(groupid.trim()) && line.contains(version.trim())
	            		&& line.contains("FAILED")) {
	            	return false;

	            }
	        }
	        return true;
		} catch (IOException e) {
			return false;
		}
	}
}
