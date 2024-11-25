package UtilsHelper;

import java.io.IOException;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import Driver.DriverFactory;
import Tests.Hook;

public class AttachFile extends Hook{
	
	 public void Screenshot() throws IOException {

 		byte[] screenshot = ((TakesScreenshot) DriverFactory.getDriver()).getScreenshotAs(OutputType.BYTES);
 		scenario.attach(screenshot, "image/png", "");

 		// call -> 		new AttachFile().Screenshot();
 		//set link in report ->   .info("?a href=’props.properties’ target=’_blank’?LOG FILE?/a?");

		//System.out.println("getRelativePath" + ExtentService.getScreenshotReportRelatvePath());
		//System.out.println("getFolder" + ExtentService.getScreenshotFolderName());
     }
	 
	 //OLD VERSION
//		public void takeScreenShot(WebDriver driver, String nameShot) throws IOException {
//
//			ExtentTest reporter = ExtentCucumberAdapter.getCurrentStep();
//			String newShotName = null;
//			List<Integer> numbers = new ArrayList<Integer>();
//			needToSleep(1500);
//			File screenshot = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
//			String reportPath = ExtentService.getScreenshotFolderName();
//			 
//			//reportPath = reportPath.substring(0, reportPath.indexOf("report.html"));
//
//			File dir = new File(reportPath);
//			if (!dir.exists()) {
//				if (dir.mkdir()) {
//					System.out.println("Directory 'screenshots' is created!");
//				} else {
//					System.out.println("Failed to create directory 'screenshots'!");
//				}
//			}
//			String[] files = dir.list();
//			if (files.length == 2) {
//				System.out.println("The directory is empty");
//				newShotName = nameShot.replaceAll("page[0-9]", "page") + 1;
//			} else {
//				for (String aFile : files)
//				{
//					if(aFile.contains(nameShot))
//					{
//						if(aFile.matches(".*\\d+.*"))
//						{
//							String num = aFile.replaceAll(".*page", "");
//							num = num.replaceAll("[^0-9]", "");
//							numbers.add(Integer.parseInt(num));  //  [^0-9]
//						}
//						else {
//							newShotName = nameShot.replaceAll("page[0-9]", "page") + 1;
//						}
//					}
//				}     
//
//				if(numbers.size() > 0) {
//					Collections.sort(numbers);
//					int number = numbers.get(numbers.size()-1);
//					newShotName = nameShot.replaceAll("page[0-9]", "page") + (number + 1);
//				}
//				else {
//					newShotName = nameShot.replaceAll("page[0-9]", "page") + 1;
//				}
//			}
//
//			String imageRelative  = newShotName +".png";
//			//System.out.println("The image name and shot folder are " + imageRelative);
//
//			String relativePath = reportPath + imageRelative;
//			//System.out.println("The shot relative path is " + relativePath);
//
//			//FileUtils.copyFile(screenshot, new File("output//screenshots//" + newShotName +".png"));
//			FileUtils.copyFile(screenshot, new File(relativePath));
//
//			//absolutePath = FileSystems.getDefault().getPath("output//screenshots//screenshot_non_runners.png").normalize().toAbsolutePath().toString();
//			//absolutePath = new File("output//screenshots//" + newShotName +".png").getCanonicalPath();
//
//			System.out.println(ExtentService.getScreenshotFolderName());
//			ExtentCucumberAdapter.getCurrentStep().addScreenCaptureFromPath(ExtentService.getScreenshotFolderName());
//
//			//reporter.addScreenCaptureFromPath(relativePath);
//		}
}
