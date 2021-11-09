package com.healthifyme.common;

import com.healthifyme.pages.PlayStorePage;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.FindsByAndroidUIAutomator;
import io.appium.java_client.MobileElement;
import io.appium.java_client.TouchAction;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.pagefactory.AppiumFieldDecorator;
import io.appium.java_client.remote.AndroidMobileCapabilityType;
import io.appium.java_client.remote.MobileCapabilityType;
import io.appium.java_client.service.local.AppiumDriverLocalService;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Parameters;

import static io.appium.java_client.touch.WaitOptions.waitOptions;
import static io.appium.java_client.touch.offset.PointOption.point;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.URL;
import java.time.Duration;
import java.util.Properties;

public class BaseTest {

    protected static AppiumDriver driver;
    private WebDriverWait wait;
    private long explicitWaitTimeoutInSeconds = 10L;
    private static long INSTALL_DURATION_IN_SECONDS = 60L;

    final String testAppName = "healthifyme app";
    final String testAppPackage = "com.healthifyme.basic";
    final String testAppActivity = ".activities.LaunchActivity";
    private static AppiumDriverLocalService server;

    PlayStorePage playStorePage;

    protected static Properties props;
    InputStream inputStream;

    public BaseTest() {
        PageFactory.initElements(new AppiumFieldDecorator(getDriver()), this);
    }

    public AppiumDriver getDriver() {
        return driver;
    }

    @BeforeSuite
    public void test() throws Exception {
        server = AppiumDriverLocalService.buildDefaultService();
        if(!checkIfAppiumServerIsRunnning(4723)) {
            server.start();
            server.clearOutPutStreams(); // -> Comment this if you don't want to see server logs in the console
        } else {
            System.out.println("");
        }
    }

    public boolean checkIfAppiumServerIsRunnning(int port) throws Exception {
        boolean isAppiumServerRunning = false;
        ServerSocket socket;
        try {
            socket = new ServerSocket(port);
            socket.close();
        } catch (IOException e) {
            System.out.println("1");
            isAppiumServerRunning = true;
        } finally {
            socket = null;
        }
        return isAppiumServerRunning;
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


            wait = new WebDriverWait(driver, explicitWaitTimeoutInSeconds);

            playStorePage = new PlayStorePage();

            uninstallApp(testAppPackage);
            installAppFromGooglePlayStore();

            // This quits the google play store driver session
            driver.quit();

            // launch HealthifyMe app installed from play store
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
     * To install HealthifyMe app from play store
     * @throws Exception
     */
    public void installAppFromGooglePlayStore() throws Exception {
        playStorePage
                .tapOnSearchAppsAndGames()
                .enterAppSearchText(testAppName)
                .selectFromSearchResult()
                .validateSelectedAppName()
                .tapOnAppInstallButton()
                .validateAppIsInstalled();
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
        capabilities.setCapability(MobileCapabilityType.NO_RESET, true);
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

    /**
     * To tap on screen with position
     * @param x
     * @param y
     */
    public void tapOnPosition(int x, int y) {
        TouchAction touchAction = new TouchAction(driver);
        touchAction.tap(point(x, y)).perform();
    }

    /**
     * To verify the existence of element
     * @param e
     * @param seconds
     * @return
     */
    public boolean isExists(MobileElement e, int seconds) {
        try {
            waitForVisibility(e, seconds);
            return true;
        } catch (StaleElementReferenceException | TimeoutException | NoSuchElementException exception) {
            return false;
        }
    }

    /**
     * Method to scroll to another element
     * @param withText
     * @return
     */
    public MobileElement androidScrollToElement(String withText) {
        return (MobileElement) ((FindsByAndroidUIAutomator) getDriver()).findElementByAndroidUIAutomator(
                "new UiScrollable(new UiSelector()" + ".scrollable(true)).scrollIntoView("
                        + "new UiSelector().description(\"" + withText + "\"));");
    }

    /**
     * Method to swipe left/right/bottom/up
     * @param startX
     * @param startY
     * @param endX
     * @param endY
     * @throws Exception
     */
    public void swipe(int startX, int startY, int endX, int endY) throws Exception {
        new TouchAction(driver)
                .press(point(startX, startY))
                .waitAction((waitOptions(Duration.ofMillis(1000))))
                .moveTo(point(endX, endY))
                .release().perform();
        staticWait(1);
    }

    /**
     * Static wait method
     * @param seconds
     * @throws InterruptedException
     */
    protected void staticWait(int seconds) throws InterruptedException {
        Thread.sleep(seconds * 1000L);
    }
}
