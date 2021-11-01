package com.healthifyme.tests;

import com.healthifyme.common.BaseTest;
import com.healthifyme.pages.DashboardPage;
import com.healthifyme.pages.LoginPage;
import io.appium.java_client.MobileElement;
import org.testng.Assert;
import org.testng.annotations.*;

public class LoginTest extends BaseTest {
    LoginPage loginPage;
    DashboardPage dashboardPage;

    @BeforeMethod
    public void beforeMethod() {
        loginPage = new LoginPage();
    }

    @Test
    public void loginValidation() {
        loginPage.navigateToLoginScreen();
        loginPage.tapOnLoginWithEmailButton();
        loginPage.enterUserName("hme-testpr501@example.com");
        loginPage.enterPassword("password");

        dashboardPage = loginPage.tapOnLoginButton();
        MobileElement hamburgerElement = dashboardPage.getHamburgerMenu();

        Assert.assertTrue(hamburgerElement.isDisplayed());
    }
}
