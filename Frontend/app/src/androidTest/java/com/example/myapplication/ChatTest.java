package com.example.myapplication;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isAssignableFrom;
import static androidx.test.espresso.matcher.ViewMatchers.isDescendantOfA;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.isRoot;
import static androidx.test.espresso.matcher.ViewMatchers.withChild;
import static androidx.test.espresso.matcher.ViewMatchers.withContentDescription;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.CoreMatchers.allOf;

import android.os.Bundle;
import android.widget.TextView;

import androidx.fragment.app.testing.FragmentScenario;
import androidx.navigation.Navigation;
import androidx.navigation.testing.TestNavHostController;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.espresso.ViewAction;
import androidx.test.espresso.action.ViewActions;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;

import com.example.myexperiments.Chat;
import com.example.myexperiments.Dashboard;
import com.example.myexperiments.MainActivity;
import com.example.myexperiments.Manager;
import com.example.myexperiments.R;
import com.google.android.material.textfield.TextInputEditText;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class ChatTest {

    private static final String mockUser = "{\"id\":1,\"firstName\":\"Zane\",\"lastName\":\"Eason\",\"emailAddress\":\"zseason@iastate.edu\",\"password\":\"123\",\"classType\":3}";

    private FragmentScenario<Chat> chatScenario;

    @Rule
    public ActivityScenarioRule<MainActivity> activityScenarioRule = new ActivityScenarioRule<>(MainActivity.class);

    @Test
    public void connectButtonTest() {
        //setup user
        Bundle bundle = new Bundle();
        bundle.putString("currUser", mockUser);
        chatScenario = FragmentScenario.launchInContainer(Chat.class, bundle, R.style.TestTheme);

        TestNavHostController navController = new TestNavHostController(ApplicationProvider.getApplicationContext());
        InstrumentationRegistry.getInstrumentation().runOnMainSync(() -> {
            navController.setGraph(R.navigation.nav_graph);
            navController.setCurrentDestination(R.id.Chat);
        });

        chatScenario.onFragment(fragment -> Navigation.setViewNavController(fragment.requireView(), navController));

        onView(withId(R.id.connect_button)).perform(click());
        try{
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        String output = "\nServer:User: [Manager] zseason has Joined the Chat";
        onView(withChild(withText(output))).check(matches(isDisplayed()));
    }

    @Test
    public void sendButtonTest() {
        //setup
        Bundle bundle = new Bundle();
        bundle.putString("currUser", mockUser);
        chatScenario = FragmentScenario.launchInContainer(Chat.class, bundle, R.style.TestTheme);

        TestNavHostController navController = new TestNavHostController(ApplicationProvider.getApplicationContext());
        InstrumentationRegistry.getInstrumentation().runOnMainSync(() -> {
            navController.setGraph(R.navigation.nav_graph);
            navController.setCurrentDestination(R.id.Chat);
        });

        chatScenario.onFragment(fragment -> Navigation.setViewNavController(fragment.requireView(), navController));

        //Connect
        onView(withId(R.id.connect_button)).perform(click());
        Helpers.waitForView(R.id.connect_button, 1000);

        //Send message
        onView(withId(R.id.chat_input)).perform(ViewActions.typeText("Hello"));
        onView(isRoot()).perform(closeSoftKeyboard());
        onView(withId(R.id.send_button)).perform(click());
        Helpers.waitForView(R.id.send_button, 1000);
        onView(withText("\nServer:User: [Manager] zseason has Joined the Chat\nServer:[Manager] zseason: Hello")).check(matches(isDisplayed()));
    }

    @Test
    public void sendButtonWhenNotConnected() {
        //setup user
        Bundle bundle = new Bundle();
        bundle.putString("currUser", mockUser);
        chatScenario = FragmentScenario.launchInContainer(Chat.class, bundle, R.style.TestTheme);

        TestNavHostController navController = new TestNavHostController(ApplicationProvider.getApplicationContext());
        InstrumentationRegistry.getInstrumentation().runOnMainSync(() -> {
            navController.setGraph(R.navigation.nav_graph);
            navController.setCurrentDestination(R.id.Chat);
        });

        chatScenario.onFragment(fragment -> Navigation.setViewNavController(fragment.requireView(), navController));

        //Send message
        onView(withId(R.id.chat_input)).perform(ViewActions.typeText("Hello"));
        onView(isRoot()).perform(closeSoftKeyboard());
        onView(withId(R.id.send_button)).perform(click());
        Helpers.waitForView(R.id.send_button, 1000);

        onView(withText("Not connected!")).check(matches(isDisplayed()));
    }

    @Test
    public void sendButtonWhenEmpty() {
        //setup user
        Bundle bundle = new Bundle();
        bundle.putString("currUser", mockUser);
        chatScenario = FragmentScenario.launchInContainer(Chat.class, bundle, R.style.TestTheme);

        TestNavHostController navController = new TestNavHostController(ApplicationProvider.getApplicationContext());
        InstrumentationRegistry.getInstrumentation().runOnMainSync(() -> {
            navController.setGraph(R.navigation.nav_graph);
            navController.setCurrentDestination(R.id.Chat);
        });

        chatScenario.onFragment(fragment -> Navigation.setViewNavController(fragment.requireView(), navController));

        onView(withId(R.id.send_button)).perform(click());
        Helpers.waitForView(R.id.send_button, 1000);

        onView(withText("Type something first!")).check(matches(isDisplayed()));
    }
}
