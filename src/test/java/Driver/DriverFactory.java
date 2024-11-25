package Driver;

import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.WebDriver;

/**
 * A factory for creating Driver objects.
 */
public final class DriverFactory {

	/** The drivers. */
	/** Thread local driver object for webdriver*/
	private static ThreadLocal<WebDriver> drivers = new ThreadLocal<>();

	/** The stored drivers. */
	private static List<WebDriver> storedDrivers = new ArrayList<>();

	/** Quit the drivers and browsers at the end only.*/
	static {
		Runtime.getRuntime().addShutdownHook(new Thread() {
			public void run() {
				storedDrivers.stream().forEach(WebDriver::quit);
			}
		});
	}

	/**
	 * Instantiates a new driver factory.
	 */
	private DriverFactory() {}

	/**
	 * Gets the driver.
	 * Call this method to get the driver object and launch the browser
	 * @return the driver
	 */
	public static WebDriver getDriver() { 
		return drivers.get();
	}

	/**
	 * Adds the driver.
	 *
	 * @param driver the driver
	 */
	public static void addDriver(WebDriver driver) {
		//System.out.println("addDriver " + driver);
		storedDrivers.add(driver);
		drivers.set(driver);
	}

	/**
	 * Quits the driver and closes the browser
	 */
	public static void removeDriver() {
		storedDrivers.remove(drivers.get());
		drivers.remove();
	}
}
