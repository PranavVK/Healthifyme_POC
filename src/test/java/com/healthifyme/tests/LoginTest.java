package com.healthifyme.tests;

import com.healthifyme.common.BaseTest;
import com.healthifyme.pages.DashboardPage;
import com.healthifyme.pages.FoodLogsPage;
import com.healthifyme.pages.IFLPage;
import com.healthifyme.pages.LoginPage;
import io.appium.java_client.MobileElement;
import org.testng.Assert;
import org.testng.annotations.*;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.InputStream;
import java.util.ArrayList;

public class LoginTest extends BaseTest {
    LoginPage loginPage;
    FoodLogsPage foodLogsPage;
    DashboardPage dashboardPage;
    IFLPage iflPage;

    InputStream testData;
    JSONObject loginUser;

    @BeforeClass
    public void beforeClass() throws Exception {
        try {
            String dataFileName = "TestData.json";
            testData = getClass().getClassLoader().getResourceAsStream(dataFileName);
            JSONTokener jsonTokener = new JSONTokener(testData);
            loginUser = new JSONObject(jsonTokener);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (testData != null) {
                testData.close();
            }
        }
    }

    @BeforeMethod
    public void beforeMethod() {
        loginPage = new LoginPage();
        dashboardPage = new DashboardPage();
        foodLogsPage = new FoodLogsPage();
        iflPage = new IFLPage();
    }

    @Test
    public void loginValidation() {
        String userName = loginUser.getJSONObject("validUser").getString("username");
        String password = loginUser.getJSONObject("validUser").getString("password");

        dashboardPage = loginPage
                .navigateToLoginScreen()
                .tapOnLoginWithEmailButton()
                .enterUserName(userName)
                .enterPassword(password)
                .tapOnLoginButton();

        MobileElement hamburgerElement = dashboardPage.getHamburgerMenu();

        Assert.assertTrue(hamburgerElement.isDisplayed(), "Dashboard screen is not seen");
    }

    @Test
    public void validateFeedBySelectingPreviousWeekDate(){
        String currentDate = dashboardPage.tapOnFeedDatePickerButton();
        String dateAfterSelectingPreviousDate = dashboardPage.selectPreviousWeekDate();
        Assert.assertNotEquals(currentDate, dateAfterSelectingPreviousDate, "Date picker date not updated/selected");
    }

    @Test
    public void testFoodLog() {
        dashboardPage.selectCalEatenOptionFromDashboardView();
        foodLogsPage.tapOnAddFoodLogForMealType("Breakfast");
        ArrayList selectedBreakFastFoodTypes = iflPage.addNumberOfFoodTypes(2);
        iflPage.tapOnAddToMealButton();

        foodLogsPage.tapOnAddFoodLogForMealType("Lunch");
        ArrayList selectedLunchFoodTypes = iflPage.addNumberOfFoodTypes(2);
        iflPage.tapOnAddToMealButton();

        foodLogsPage.tapOnAddFoodLogForMealType("Dinner");
        ArrayList selectedDinnerFoodTypes = iflPage.addNumberOfFoodTypes(2);
        iflPage.tapOnAddToMealButton();
    }
}
