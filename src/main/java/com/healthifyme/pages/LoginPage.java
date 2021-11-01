package com.healthifyme.pages;

import com.healthifyme.common.BaseTest;
import io.appium.java_client.MobileElement;
import io.appium.java_client.pagefactory.AndroidFindBy;

public class LoginPage extends BaseTest {
    protected final String ANDROID_HEALTHIFYME_APP_PKG = "com.healthifyme.basic:id/";

    @AndroidFindBy(id = ANDROID_HEALTHIFYME_APP_PKG + "tv_sign_in")
    private MobileElement signInOptionButton;

    @AndroidFindBy(id = ANDROID_HEALTHIFYME_APP_PKG + "tv_phone_email")
    private MobileElement loginWithEmailButton;

    @AndroidFindBy(id = ANDROID_HEALTHIFYME_APP_PKG + "tiet_username")
    private MobileElement userNameTextField;

    @AndroidFindBy(id = ANDROID_HEALTHIFYME_APP_PKG + "tiet_password")
    private MobileElement passwordTextField;

    @AndroidFindBy(id = ANDROID_HEALTHIFYME_APP_PKG + "btn_login_signup")
    private MobileElement loginButton;

    public LoginPage navigateToLoginScreen() {
        click(signInOptionButton);
        // To dismiss continue with mobile number alert
        tapOnPosition(100, 50);
        return this;
    }

    public LoginPage tapOnLoginWithEmailButton() {
        click(signInOptionButton);
        return this;
    }

    public LoginPage enterUserName(String userName) {
        sendKeys(userNameTextField, userName);
        return this;
    }

    public LoginPage enterPassword(String password) {
        sendKeys(passwordTextField, password);
        return this;
    }

    public DashboardPage tapOnLoginButton() {
        click(loginButton);
        return new DashboardPage();
    }
}
