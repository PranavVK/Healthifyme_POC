package com.healthifyme.pages;

import com.healthifyme.common.BaseTest;
import io.appium.java_client.MobileElement;
import io.appium.java_client.pagefactory.AndroidFindBy;
import java.util.List;

public class DashboardPage extends BaseTest {
    protected final String ANDROID_HEALTHIFYME_APP_PKG = "com.healthifyme.basic:id/";

    @AndroidFindBy(accessibility = "Drawer Open")
    private MobileElement hamburgerMenu;

    @AndroidFindBy(id = ANDROID_HEALTHIFYME_APP_PKG + "tv_actionbar_dashboard")
    private MobileElement selectFeedDateButton;

    @AndroidFindBy (uiAutomator = "new UiSelector().resourceId(\"com.android.vending:id/0_resource_name_obfuscated\").text(\"Not now\")")
    private MobileElement appReviewNotNowButton;

    @AndroidFindBy(id = ANDROID_HEALTHIFYME_APP_PKG + "tv_date")
    private List<MobileElement> datePickerViews;

    public MobileElement getHamburgerMenu() {
        // To dismiss play store app review alert popup
        if (isExists(appReviewNotNowButton, 20)) {
            click(appReviewNotNowButton);
        }
        waitForVisibility(hamburgerMenu, 30);
        return hamburgerMenu;
    }

    public String tapOnFeedDatePickerButton() {
        click(selectFeedDateButton);
        String currentDate = selectFeedDateButton.getText();
        return currentDate;
    }

    public String selectPreviousWeekDate() {
        MobileElement firstDatePickerView = datePickerViews.get(0);
        MobileElement lastDatePickerView = datePickerViews.get(6);

        int startX = firstDatePickerView.getLocation().getX();
        int startY = firstDatePickerView.getLocation().getY();
        int endX = lastDatePickerView.getLocation().getX();
        int endY = lastDatePickerView.getLocation().getY();

        try {
            swipe(startX, startY, endX, endY);
        } catch (Exception e) {
            e.printStackTrace();
        }

        String dateAfterSelectingPreviousDate = selectFeedDateButton.getText();
        return dateAfterSelectingPreviousDate;
    }
}
