package com.healthifyme.pages;

import com.healthifyme.common.BaseTest;
import io.appium.java_client.MobileElement;
import io.appium.java_client.pagefactory.AndroidFindBy;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class IFLPage extends BaseTest {
    protected final String ANDROID_HEALTHIFYME_APP_PKG = "com.healthifyme.basic:id/";

    @AndroidFindBy(xpath = "//*[@class='android.widget.TextView']/following-sibling::android.widget.CheckBox")
    private List<MobileElement> addTrackedFoodsButtons;

    @AndroidFindBy(id = ANDROID_HEALTHIFYME_APP_PKG + "tv_food_name")
    private List<MobileElement> foodTypes;

    @AndroidFindBy(id = ANDROID_HEALTHIFYME_APP_PKG + "btn_add_to_meal")
    private MobileElement addToMealButton;


    public ArrayList addNumberOfFoodTypes(int num) {
        ArrayList selectedFoodTypes = new ArrayList();
        for (int i = 0; i < num; i++) {
            addTrackedFoodsButtons.get(i).click();
            selectedFoodTypes.add(foodTypes.get(i).getText());
        }
        return selectedFoodTypes;
    }

    public IFLPage tapOnAddToMealButton() {
        click(addToMealButton);
        return this;
    }

}
