package com.healthifyme.common;

import com.healthifyme.pages.PlayStorePO;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileElement;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.pagefactory.AppiumFieldDecorator;
import io.appium.java_client.remote.AndroidMobileCapabilityType;
import io.appium.java_client.remote.MobileCapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Parameters;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Properties;

public class BaseTest {

    protected static AppiumDriver driver;
    private WebDriverWait wait;
    private long explicitWaitTimeoutInSeconds = 10L;
    private static long INSTALL_DURATION_IN_SECONDS = 60L;

    final String testAppName = "healthifyme app";
    final String testAppPackage = "com.healthifyme.basic";
    final String testAppActivity = ".activities.LaunchActivity";

    PlayStorePO playStorePO;

    protected Properties props;
    InputStream inputStream;

    public BaseTest() {
        PageFactory.initElements(new AppiumFieldDecorator(getDriver()), this);
    }

    public AppiumDriver getDriver() {
        return driver;
    }

    @Parameters({"platformName", "platformVersion", "deviceName", "udid"})
    @BeforeTest
    public void setUp(String platformName, String platformVersion, String deviceName, String udid) {
        try {
            props = new Properties();
            String propFileName = "config.properties";
            inputStream = getClass().getClassLoader().getResourceAsStream(propFileName);
            props.load(inputStream);

            DesiredCapabilities capabilities = new DesiredCapabilities();
            capabilities.setCapability(MobileCapabilityType.PLATFORM_NAME,platformName);
            capabilities.setCapability(MobileCapabilityType.DEVICE_NAME, platformVersion);
            capabilities.setCapability(MobileCapabilityType.PLATFORM_VERSION, deviceName);
            capabilities.setCapability(MobileCapabilityType.UDID, udid);
            capabilities.setCapability(AndroidMobileCapabilityType.APP_PACKAGE, "com.android.vending");
            capabilities.setCapability(AndroidMobileCapabilityType.APP_ACTIVITY, ".AssetBrowserActivity");
            capabilities.setCapability(AndroidMobileCapabilityType.APP_WAIT_ACTIVITY, ".AssetBrowserActivity");
            capabilities.setCapability(AndroidMobileCapabilityType.DEVICE_READY_TIMEOUT, 40);
            capabilities.setCapability(MobileCapabilityType.NEW_COMMAND_TIMEOUT, 180);

            URL url = new URL(props.getProperty("appiumURL"));
            driver = new AndroidDriver(url, capabilities);

            PageFactory.initElements(new AppiumFieldDecorator(driver), this);

            wait = new WebDriverWait(driver, explicitWaitTimeoutInSeconds);

            playStorePO = new PlayStorePO();

            uninstallApp(testAppPackage);
            installAppFromGooglePlayStore();

            // Q current driver instance - this quits the google playstore
            driver.quit();

            // launch HealthiFyMe app installed from playstore
            driver = new AndroidDriver(url, healthifymeDesiredCapabilities(platformName, platformVersion, deviceName, udid));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @AfterTest
    public void afterClass() {
        driver.quit();
    }

    /**
     * To install HealthiFyMe app from
     * @throws Exception
     */
    public void installAppFromGooglePlayStore() throws Exception {
        playStorePO.tapOnSearchAppsAndGames();
        playStorePO.enterAppSearchText(testAppName);
        playStorePO.selectFromSearchResult();
        playStorePO.validateSelectedAppName();
        playStorePO.tapOnAppInstallButton();
        playStorePO.validateAppIsInstalled();
    }

    /**
     * To set desired capabilities for HealthiFyMe app
     * @param platformName
     * @param platformVersion
     * @param deviceName
     * @param udid
     * @return
     */
    private DesiredCapabilities healthifymeDesiredCapabilities(String platformName, String platformVersion, String deviceName, String udid) {
        DesiredCapabilities capabilities = new DesiredCapabilities();
        capabilities.setCapability(MobileCapabilityType.PLATFORM_NAME, platformName);
        capabilities.setCapability(MobileCapabilityType.DEVICE_NAME, deviceName);
        capabilities.setCapability(MobileCapabilityType.PLATFORM_VERSION, platformVersion);
        capabilities.setCapability(MobileCapabilityType.UDID, udid);
        capabilities.setCapability(AndroidMobileCapabilityType.APP_PACKAGE, testAppPackage);
        capabilities.setCapability(AndroidMobileCapabilityType.APP_ACTIVITY, testAppActivity);
        capabilities.setCapability(AndroidMobileCapabilityType.APP_WAIT_ACTIVITY, testAppActivity);
        capabilities.setCapability(AndroidMobileCapabilityType.DEVICE_READY_TIMEOUT, 40);
        capabilities.setCapability(MobileCapabilityType.NEW_COMMAND_TIMEOUT, 180);
        return  capabilities;
    }

    /**
     * Method to uninstall app if already insatlled
     * @param appPackage
     * @throws InterruptedException
     * @throws IOException
     */
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

    /**
     * Method for checking visibility of element
     * @param e
     * @param seconds
     */
    public void waitForVisibility(MobileElement e, int seconds) {
        WebDriverWait wait = new WebDriverWait(driver, seconds);
        wait.until(ExpectedConditions.visibilityOf(e));
    }

    /**
     * click method
     * @param e
     */
    public void click(MobileElement e) {
        waitForVisibility(e, 10);
        e.click();
    }

    /**
     * sendKeys method
     *
     * @param e
     * @param text
     */
    public void sendKeys(MobileElement e, String text) {
        waitForVisibility(e, 10);
        e.sendKeys(text);
    }
}
