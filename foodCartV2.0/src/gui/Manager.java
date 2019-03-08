package gui;

import java.io.File;
import java.net.URISyntaxException;
import java.security.CodeSource;

public class Manager {
	public static void main(String[] args) {
		
// get current directory
		CodeSource codeSource = Manager.class.getProtectionDomain().getCodeSource();
		File jarFile = null;
		try {
			jarFile = new File(codeSource.getLocation().toURI().getPath());
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String jarDir = jarFile.getParentFile().getPath();
		System.out.println("foodCart Coffee Shop is running in dir : " + jarDir);
		System.out.println("Please ensure the menuitems.db and orderlist.db files are placed here");
// call gui		
///		new ShopGUI(jarDir);
	}

}
