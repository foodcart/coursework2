package controller;
import java.io.File;
import java.net.URISyntaxException;
import java.security.CodeSource;

public class foodCart {

	private static String getCurrentDirectory(){
		
		CodeSource codeSource = Manager.class.getProtectionDomain().getCodeSource();
		File jarFile = null;
		try {
			jarFile = new File(codeSource.getLocation().toURI().getPath());
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//System.out.println("foodCart Coffee Shop is running in dir : " + jarFile.getParentFile().getPath());
		//System.out.println("Please ensure the menuitems.db and orderlist.db files are placed here");
		return jarFile.getParentFile().getPath();
	}
	
	
/*
 * Startup the application manager	
 */
	public static void main(String[] args){
		
		Manager.getInstance(getCurrentDirectory(), 5);
		
	}
}
