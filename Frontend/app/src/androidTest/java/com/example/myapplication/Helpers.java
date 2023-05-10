package com.example.myapplication;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

import androidx.test.espresso.NoMatchingViewException;

public class Helpers {
    static void waitForView(int viewId, long timeout) {
        long startTime = System.currentTimeMillis();
        do {
            try {
                onView(withId(viewId)).check(matches(isDisplayed()));
                return;
            } catch (NoMatchingViewException | AssertionError e) {
                // Ignore the exception and keep waiting
            }

            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                throw new RuntimeException("Interrupted while waiting for view", e);
            }
        } while (System.currentTimeMillis() - startTime < timeout);
        throw new AssertionError("View not displayed within timeout");
    }
}

//./gradlew connectedDebugAndroidTest jacocoTestReport

