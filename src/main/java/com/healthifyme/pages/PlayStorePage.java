package com.healthifyme.pages;

import com.healthifyme.common.BaseTest;

import io.appium.java_client.MobileElement;
import io.appium.java_client.pagefactory.AndroidFindBy;

public class PlayStorePage extends BaseTest {
    protected final String ANDROID_PLAY_STORE_PKG = "com.healthifyme.basic:id/";
    protected final String testAppName = "healthifyme app";
    protected final String getTestAppFullName = "HealthifyMe - Calorie Counter, Diet Plan, Trainers";

    @AndroidFindBy (uiAutomator = "new UiSelector().resourceId(\"com.android.vending:id/0_resource_name_obfuscated\").text(\"Search for apps & games\")")
    private MobileElement playStoreSearchField;

    @AndroidFindBy (className = "android.widget.EditText")
    private MobileElement playStoreEditText;

    @AndroidFindBy (uiAutomator = "new UiSelector().resourceId(\"com.android.vending:id/0_resource_name_obfuscated\").text(\"" + testAppName + "\")")
    private MobileElement playStoreSearchResult;

    @AndroidFindBy (uiAutomator = "new UiSelector().resourceId(\"com.android.vending:id/0_resource_name_obfuscated\").descriptionStartsWith(\"" + getTestAppFullName + "\")")
    private MobileElement playStoreAppFullName;

    @AndroidFindBy (uiAutomator = "new UiSelector().resourceId(\"com.android.vending:id/0_resource_name_obfuscated\").text(\"Install\")")
    private MobileElement appInstallButton;

    @AndroidFindBy (uiAutomator = "new UiSelector().resourceId(\"com.android.vending:id/0_resource_name_obfuscated\").text(\"Open\")")
    private MobileElement appOpenButton;


    public PlayStorePage tapOnSearchAppsAndGames() {
        waitForVisibility(playStoreSearchField, 30);
        click(playStoreSearchField);
        return this;
    }

    public PlayStorePage enterAppSearchText(String text) {
        sendKeys(playStoreEditText, text);
        return this;
    }

    public PlayStorePage selectFromSearchResult() {
        click(playStoreSearchResult);
        return this;
    }

    public PlayStorePage validateSelectedAppName() {
        waitForVisibility(playStoreAppFullName, 20);
        return this;
    }

    public PlayStorePage tapOnAppInstallButton() {
        click(appInstallButton);
        return this;
    }

    public PlayStorePage validateAppIsInstalled() {
        waitForVisibility(appOpenButton, 60);
        return this;
    }
}
