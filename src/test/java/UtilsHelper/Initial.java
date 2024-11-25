package UtilsHelper;

public class Initial {

	public String testURL;

	public void environment() {

		String environment = "dev";

		if(System.getProperty("test.env")!=null) {
			environment = System.getProperty("test.env");
			System.out.println("System Environment = " + System.getProperty("test.env"));
		}
		else {
			if(PropertyUtils.getProperty("base.URL").contains("uat")) {
				environment = "uat";
			}
		}

		System.out.println("Tests will run on " + environment + " Environment");
		CommonVariables.environment = environment;
	}

	public void initURL() {

		if(System.getProperty("test.URL")!=null) {
			testURL = System.getProperty("test.URL");
			System.out.println("System URL = " + System.getProperty("test.URL"));
		}
		else {
			testURL = PropertyUtils.getProperty("base.URL") + PropertyUtils.getProperty("base.params.URL");
			if(testURL.contains("local")) {
				CommonVariables.environment = "local";
			}
			testURL = testURL.replace("dev", CommonVariables.environment);
		}
		System.out.println("Tests will run with URL : " + testURL);
		CommonVariables.baseURL = testURL;
	}

	public void username() {

		String user = "";
		if(System.getProperty("test.user")!=null) {
			user = System.getProperty("test.user");
			System.out.println("System username = " + System.getProperty("test.user"));
		}
		else {
				user = PropertyUtils.getProperty("username");
		}

		System.out.println("Tests will run with username " + user);
		CommonVariables.username = user;
	}
	
//	public void reportPathName() {
//		//get current date & time
//		String date =  LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy_HH-mm"));
//
//		//set current date & time into report name
//		ExtentProperties.INSTANCE.setReportPath("output" + File.separator + "Report_" + date + File.separator
//				+ "report.html");
//	}

}
