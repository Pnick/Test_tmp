package UtilsHelper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.concurrent.TimeUnit;

public class CheckEnvironment {

	public enum OS {
		WINDOWS, LINUX, MAC, SOLARIS
	};

	protected static OS os = null;

	public OS getOS() {
		if (os == null) {
			String osName = System.getProperty("os.name").toLowerCase();
			if (osName.contains("win")) {
				os = OS.WINDOWS;
			} else if (osName.contains("nix") || osName.contains("nux")	|| osName.contains("aix")) {
				os = OS.LINUX;
			} else if (osName.contains("mac os") || osName.contains("macos") || osName.contains("darwin")) {
				os = OS.MAC;
			} else if (osName.contains("solaris") || osName.contains("sunos")) {
				os = OS.SOLARIS;
			}
		}
		System.out.println("Test Suite will run on " + os);
		return os;
	}

	public boolean isDriverPidExist(){
		String line;
		String pidInfo ="";
		Process p = null;
		boolean pidexist = false;
		
		if(os==null) {
			getOS();
		}

		System.out.println("********** [Info] Check for running drivers! **********");
		
		switch (os) {
		case WINDOWS:
			try {
				String findProcess = "chromedriver.exe";
				String filenameFilter = " /nh /fi \"Imagename eq "+findProcess+"\"";
				p = Runtime.getRuntime().exec(System.getenv("windir") +"\\system32\\"+"tasklist.exe" + filenameFilter);
			} catch (IOException e) { e.printStackTrace(); }
			break;
		case LINUX:
			try {
				p = Runtime.getRuntime().exec(new String[]{"/bin/bash","-c","ps -aux |egrep 'chromedriver|gecko' | grep -v 'grep'"});
			} catch (IOException e) { e.printStackTrace(); }
			break;
		case MAC:
			try {
				p = Runtime.getRuntime().exec(new String[]{"/bin/bash","-c","ps aux | egrep 'chromedriverMac|gecko|Safari.app' | grep -v 'grep'"});
			} catch (IOException e) { e.printStackTrace(); }
			break;
		case SOLARIS:
			// TODO
		default:
			break;
		}

		BufferedReader input =  new BufferedReader(new InputStreamReader(p.getInputStream()));

		try {
			while ((line = input.readLine()) != null) {
				pidInfo+=line;
				if(!line.contains("No tasks are running"))
					System.out.print(line + "\n");
			}
		} catch (IOException e) {e.printStackTrace(); }
		try {
			input.close();
			p.destroy();
		} catch (IOException e) {e.printStackTrace(); }

		if (pidInfo.contains("chromedriver")) {
			System.out.println("Chrome driver is running");
			pidexist = true;
		} else {
			System.out.println("Chrome driver is NOT running.");
		}
		if (pidInfo.contains("IEDriverServer.exe")) {
			System.out.println("Internet Explorer driver is running");
			pidexist = true;
		} else {
			System.out.println("Internet Explorer driver is NOT running.");
		}
		if (pidInfo.contains("gecko")) { 
			System.out.println("Firefox/Gecko driver is running");
			pidexist = true;
		} else {
			System.out.println("Firefox/Gecko driver is NOT running.");
		}
		if (pidInfo.contains("Safari")) { 
			System.out.println("Safari driver is running");
			pidexist = true;
		} else {
			System.out.println("Safari driver is NOT running.");
		}

		if(pidexist)
			return true;
		else
			return false;
	}
	
	public void killRunningDrivers() throws IOException, InterruptedException{

		//TODO set driver
//		if (BaseSeleniumFunc.driver!=null){
//			BaseSeleniumFunc.driver.close();
//			//BaseSeleniumFunc.driver.quit();
//			BaseSeleniumFunc.driver=null;
//		}

		TimeUnit.SECONDS.sleep(2);
		
		if(isDriverPidExist()==true){
			Process p = null;

			System.out.println("********** [Info] Kill running drivers! **********");

			switch (CheckEnvironment.os) {
			case WINDOWS:
				p=Runtime.getRuntime().exec("taskkill /F /IM chromedriver.exe /IM geckodriver.exe");
				TimeUnit.SECONDS.sleep(3);
				//p=Runtime.getRuntime().exec("taskkill /F /IM geckodriver.exe");
				break;
			case LINUX:
				String[] cmd = new String[]{"/bin/bash","-c","kill $(ps aux|egrep 'chromedriver|gecko')"};
				p =  Runtime.getRuntime().exec(cmd);
				TimeUnit.SECONDS.sleep(3);
				break;
			case MAC:
				String[] terminal = new String[] {"/bin/bash", "-c", "kill $(ps aux | egrep 'chromedriverMac|gecko|Safari' | awk '{print $2}')"};
				p =  Runtime.getRuntime().exec(terminal);
				TimeUnit.SECONDS.sleep(3);
			default:
				break;
			}
			p.destroy();
		}
	}
}