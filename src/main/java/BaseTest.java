import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileBy;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.remote.AndroidMobileCapabilityType;
import io.appium.java_client.remote.MobileCapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.IOException;
import java.net.URL;
import java.io.BufferedReader;
import java.io.InputStreamReader;

public class BaseTest {

    private AppiumDriver driver;
    private WebDriverWait wait;
    private long explicitWaitTimeoutInSeconds = 10L;
    private static long INSTALL_DURATION_IN_SECONDS = 60L;

    final String testAppName = "healthifyme app";
    final String getTestAppFullName = "HealthifyMe - Calorie Counter, Diet Plan, Trainers";
    final String testAppPackage = "com.healthifyme.basic";
    final String testAppActivity = ".activities.LaunchActivity";

    public static void main(String[] args) throws Exception {
        BaseTest baseTest = new BaseTest();
        baseTest.setUp();
    }

    public void setUp() throws Exception {
        DesiredCapabilities capabilities = new DesiredCapabilities();
        capabilities.setCapability(MobileCapabilityType.PLATFORM_NAME, "Android");
        capabilities.setCapability(MobileCapabilityType.DEVICE_NAME, "Pixel 4");
        capabilities.setCapability(MobileCapabilityType.PLATFORM_VERSION, "11");
        capabilities.setCapability(MobileCapabilityType.UDID, "R52R6013TVA");
        capabilities.setCapability(AndroidMobileCapabilityType.APP_PACKAGE, "com.android.vending");
        capabilities.setCapability(AndroidMobileCapabilityType.APP_ACTIVITY, ".AssetBrowserActivity");
        capabilities.setCapability(AndroidMobileCapabilityType.APP_WAIT_ACTIVITY, ".AssetBrowserActivity");
        capabilities.setCapability(AndroidMobileCapabilityType.DEVICE_READY_TIMEOUT, 40);
        capabilities.setCapability(MobileCapabilityType.NEW_COMMAND_TIMEOUT, 180);

        URL url = new URL("http://0.0.0.0:4723/wd/hub");
        this.driver = new AndroidDriver(url, capabilities);

        wait = new WebDriverWait(driver, explicitWaitTimeoutInSeconds);

        this.uninstallApp(testAppPackage);
        this.installAppFromGooglePlayStore();
    }

    public void installAppFromGooglePlayStore() throws Exception {
        // wait until search bar is visible, and then tap on it
        wait.until(ExpectedConditions.visibilityOf(
                driver.findElement(MobileBy.AndroidUIAutomator("new UiSelector().resourceId(\"com.android.vending:id/0_resource_name_obfuscated\")"))))
                .click();

        // type in the name of the app into the search bar
        driver.findElement(MobileBy.className("android.widget.EditText"))
                .sendKeys(testAppName);

        // tap on the suggested option that contains the app name
        // im using lowercase because of Google's design choice - they list all suggestions in lower case
        wait.until(ExpectedConditions.visibilityOf(
                driver.findElement(MobileBy.AndroidUIAutomator("new UiSelector().resourceId(\"com.android.vending:id/0_resource_name_obfuscated\").text(\"" + testAppName.toLowerCase() + "\")"))))
                .click();

        // wait for the app title to be displayed
//        wait.until(ExpectedConditions.visibilityOf(
//                driver.findElement(MobileBy.AndroidUIAutomator("new UiSelector().resourceId(\"com.android.vending:id/0_resource_name_obfuscated\").text(\"" + getTestAppFullName + "\")"))));

        wait.until(ExpectedConditions.visibilityOf(
                driver.findElement(MobileBy.AndroidUIAutomator("new UiSelector().resourceId(\"com.android.vending:id/0_resource_name_obfuscated\").text(\"Install" + "\")"))))
                .click();

//        // tap on the Install button
//        driver.findElement(MobileBy.AndroidUIAutomator("new UiSelector().className(\"android.widget.TextView\").resourceId(\"com.android.vending:id/title\").text(\"Install\")"))
//                .click();

//        // tap on accept
//        driver.findElement(MobileBy.AndroidUIAutomator("new UiSelector().resourceId(\"com.android.vending:id/continue_button\")"))
//                .click();

        // wait until "installed" shows up for INSTALL_DURATION_IN_SECONDS
        new WebDriverWait(driver, INSTALL_DURATION_IN_SECONDS).until(ExpectedConditions.presenceOfElementLocated(
                MobileBy.AndroidUIAutomator("new UiSelector().resourceId(\"com.android.vending:id/0_resource_name_obfuscated\").text(\"Open" + "\")")));

        // quit current driver instance - this quits the google playstore
        // and allows us to prepare for next stage - starting up the freshly installed app
        driver.quit();

        // launch newly installed app
        driver = new AndroidDriver(new URL("http://0.0.0.0:4723/wd/hub"), healthifymeDesiredCapabilities());
        driver.launchApp();

    }

    private DesiredCapabilities healthifymeDesiredCapabilities() throws Exception {
        DesiredCapabilities capabilities = new DesiredCapabilities();
        capabilities.setCapability(MobileCapabilityType.PLATFORM_NAME, "Android");
        capabilities.setCapability(MobileCapabilityType.DEVICE_NAME, "Pixel 4");
        capabilities.setCapability(MobileCapabilityType.PLATFORM_VERSION, "11");
        capabilities.setCapability(MobileCapabilityType.UDID, "R52R6013TVA");
        capabilities.setCapability(AndroidMobileCapabilityType.APP_PACKAGE, testAppPackage);
        capabilities.setCapability(AndroidMobileCapabilityType.APP_ACTIVITY, testAppActivity);
        capabilities.setCapability(AndroidMobileCapabilityType.APP_WAIT_ACTIVITY, testAppActivity);
        capabilities.setCapability(AndroidMobileCapabilityType.DEVICE_READY_TIMEOUT, 40);
        capabilities.setCapability(MobileCapabilityType.NEW_COMMAND_TIMEOUT, 180);
        return  capabilities;
    }

    private static void uninstallApp(String appPackage) throws InterruptedException, IOException {
        final Process p = Runtime.getRuntime().exec("adb uninstall " + appPackage);

        new Thread(() -> {
            BufferedReader input = new BufferedReader(new InputStreamReader(p.getInputStream()));
            String line = null;

            try {
                while ((line = input.readLine()) != null)
                    System.out.println(line);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();

        p.waitFor();
    }
}
