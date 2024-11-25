package UtilsHelper;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

public class PropertyUtils {
	private static Properties props = new Properties();

	static String workingDir;
	
	static
	{
		workingDir = System.getProperty("user.dir");

		try
		{
			loadPropertyFile(workingDir + File.separator + "configs//" + Runner.RunnerHistoryIT.configPropertiesRun);
		} catch (FileNotFoundException realCause)
		{
			//Assert.fail("Unable to load file!" + realCause);
		} catch (IOException realCause)
		{
			//Assert.fail("Unable to load file!" + realCause);
		}
	}

	public static void loadPropertyFile(String propertyFileName) throws FileNotFoundException, IOException
	{
		props.load(new FileInputStream(propertyFileName));
		//		env = ReadEnv();
		//		loop env
		//		for( element el : env )
		//			props.put(key, value)

	}

	public static String getProperty(String propertyKey)
	{
		String propertyValue = "";
		if (props.containsKey(propertyKey)) {
			propertyValue = props.getProperty(propertyKey.trim());

			if (propertyValue.trim().length() == 0)
			{
				System.out.println("WARNING : Property " + propertyValue + "does NOT exist on .properties file");
				System.out.println("WARNING : Property " + propertyValue + " = empty string");
			}
		}
		return propertyValue;
	}

	public static void setProperty(String propertyKey, String value) throws FileNotFoundException, IOException
	{
		try {  
			props.setProperty(propertyKey, value);

			FileOutputStream fos = new FileOutputStream(workingDir + File.separator + "configs//" + Runner.RunnerManagementIT.configPropertiesRun);
			props.store(fos,null);
			System.out.println("SUCCESS");
			fos.flush();
			fos.close();
		}
		catch(Exception e) {
			System.out.println("FAILED");
		}
	}
}
