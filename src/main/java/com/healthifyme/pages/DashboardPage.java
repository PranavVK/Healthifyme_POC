package com.healthifyme.pages;

import com.healthifyme.common.BaseTest;
import io.appium.java_client.MobileElement;
import io.appium.java_client.pagefactory.AndroidFindBy;

public class DashboardPage extends BaseTest {
    protected final String ANDROID_HEALTHIFYME_APP_PKG = "com.healthifyme.basic:id/";

    @AndroidFindBy(accessibility = "Drawer Open")
    private MobileElement hamburgerMenu;

    @AndroidFindBy(id = ANDROID_HEALTHIFYME_APP_PKG + "tv_actionbar_dashboard")
    private MobileElement selectFeedDateButton;

    @AndroidFindBy (uiAutomator = "new UiSelector().resourceId(\"com.android.vending:id/0_resource_name_obfuscated\").text(\"Not now\")")
    private MobileElement appReviewNotNowButton;

    public MobileElement getHamburgerMenu() {
        // To dismiss play store app review alert popup
        if (isExists(appReviewNotNowButton, 20)) {
            click(appReviewNotNowButton);
        }
        waitForVisibility(hamburgerMenu, 30);
        return hamburgerMenu;
    }

    public DashboardPage tapOnFeedDatePickerButton() {
        click(selectFeedDateButton);
        return this;
    }
}
