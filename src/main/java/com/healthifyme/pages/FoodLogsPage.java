package com.healthifyme.pages;

import com.healthifyme.common.BaseTest;
import io.appium.java_client.MobileElement;
import io.appium.java_client.pagefactory.AndroidFindBy;

import javax.xml.xpath.XPath;
import java.util.ArrayList;
import java.util.List;

public class FoodLogsPage extends BaseTest {
    protected final String ANDROID_HEALTHIFYME_APP_PKG = "com.healthifyme.basic:id/";

    @AndroidFindBy(xpath = "//*[@text='Breakfast']/following-sibling::android.widget.ImageButton")
    private MobileElement addBreakFastFoodLogButton;

    @AndroidFindBy(xpath = "//*[@text='Lunch']/following-sibling::android.widget.ImageButton")
    private MobileElement addLunchFoodLogButton;

    @AndroidFindBy(xpath = "//*[@text='Dinner']/following-sibling::android.widget.ImageButton")
    private MobileElement addDinnerFoodLogButton;


    public FoodLogsPage tapOnAddFoodLogForMealType(String mealType) {
        switch (mealType) {
            case "Breakfast" :
                click(addBreakFastFoodLogButton);
                break;

            case "Lunch" :
                click(addLunchFoodLogButton);
                break;

            case "Dinner" :
                click(addDinnerFoodLogButton);
                break;
        }

        return this;
    }
}
