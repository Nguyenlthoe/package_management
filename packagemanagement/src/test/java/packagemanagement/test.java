package packagemanagement;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 *
 * @author giasutinhoc.vn
 */
public class test {
	public static void main(String[] args) throws IOException {
//  try {
//    String dbURL = "jdbc:sqlserver://localhost;databaseName=QL_LIBRARIES_AND_PACKAGES;user=sa;password=sa";
//    Connection conn = DriverManager.getConnection(dbURL);
//    if (conn != null) {
//      System.out.println("Connected");
//      DatabaseMetaData dm = (DatabaseMetaData) conn.getMetaData();
//      System.out.println("Driver name: " + dm.getDriverName());
//      System.out.println("Driver version: " + dm.getDriverVersion());
//      System.out.println("Product name: " + dm.getDatabaseProductName());
//      System.out.println("Product version: " + dm.getDatabaseProductVersion());
//    }
//   } catch (SQLException ex) {
//     System.err.println("Cannot connect database, " + ex);
//   }

//	 String path= "D:\\a_20211\\Project1\\Maven_test\\my-app\\pom.xml";
//	 Scanner scan = new Scanner(System.in);
//	 String name = scan.nextLine();
//	 String groupid = scan.nextLine();
//	 String version = scan.nextLine();
//	 String change =  scan.nextLine();
//	 //Boolean a = ManageFileDependency.removeDpMaven(path, name, groupid, version);
//	 Boolean a = ManageFileDependency.addDpMaven(path, name, groupid, version);
//	 if(a == true) {
//		 System.out.print("DONE");
//	 }

//	 String path= "D:\\package.json";
//	 Scanner scan = new Scanner(System.in);
//	 String name = scan.nextLine();
//	 String version = scan.nextLine();
//	 //Boolean a = ManageFileDependency.removeDpMaven(path, name, groupid, version);
//	// Boolean a = ManageFileDependency.removeDpNPM(path, name);
//	 Boolean a = ManageFileDependency.changeDpNPM(path, name, version);
//	 if(a == true) {
//		 System.out.print("DONE");
//	 }
//	 if(a == false) {
//		 System.out.print("false");
//	 }
//		String path = "D:\\build.gradle";
//		Scanner scan = new Scanner(System.in);
//		String name = scan.nextLine();
//		String groupid = scan.nextLine();
//		String version = scan.nextLine();
//		//String change = scan.nextLine();
//		// Boolean a = ManageFileDependency.removeDpMaven(path, name, groupid, version);
//		Boolean a = ManageFileDependency.removeorchangeDpGradle(path, name, groupid, version, true);
//		if (a == true) {
//			System.out.print("DONE");
//		}
//	 String a = "//asdfas/afafdsasfddasafd//";
//	 String[] words = a.split("//");

//	 System.out.println(words[1]);


	        String pathname = "D:\\a_20211\\Project1\\Gradletest";

			try {
				Process p = Runtime.getRuntime().exec("cmd /c gradle -q dependencies", null, new File(pathname));
				BufferedReader r = new BufferedReader(new InputStreamReader(p.getInputStream()));
		        String line;
		        while (true) {
		            line = r.readLine();
		            if (line == null) { break; }
		            System.out.println(line);
		        }
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

	}
}