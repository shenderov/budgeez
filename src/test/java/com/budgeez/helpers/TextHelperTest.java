package com.budgeez.helpers;

import com.budgeez.BudgeezBootApplicationTests;
import org.testng.Assert;
import org.testng.annotations.Test;

public class TextHelperTest extends BudgeezBootApplicationTests {

    @Test
    public void validateConvertForOneCharEmail() {
        String email = "a@b.c";
        Assert.assertEquals(textHelper.getSecretEmail(email), "*@b.c");
    }

    @Test
    public void validateConvertForTwoCharsEmail() {
        String email = "ab@b.c";
        Assert.assertEquals(textHelper.getSecretEmail(email), "a*@b.c");
    }

    @Test
    public void validateConvertForNormalEmail() {
        String email = "budgeez@domain.com";
        Assert.assertEquals(textHelper.getSecretEmail(email), "k...i@domain.com");
    }
}
