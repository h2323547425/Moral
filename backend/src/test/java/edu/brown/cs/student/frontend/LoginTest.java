package edu.brown.cs.student.frontend;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.Before;
import org.junit.After;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class LoginTest {
    private static final String PATH = "http://localhost:3000/";
    private static ChromeDriver driver;

    /**
     * Initialize the Selenium chrome driver before class.
     */
    @Before
    public void setup() {
        WebDriverManager.chromedriver().setup();
        ChromeOptions options = new ChromeOptions();
        driver = new ChromeDriver(options);
        driver.manage().timeouts().implicitlyWait(Duration.ofMillis(500));
        driver.get(PATH);
    }

    /**
     * Test if the button for Google Authentication looks correctly.
     */
    @Test
    public void testGoogleLoginButton() {
        WebElement div = driver.findElement(By.className("googleLoginButton"));
        List<WebElement> buttons = div.findElements(By.tagName("button"));
        assertEquals(1, buttons.size());
        WebElement loginButton = buttons.get(0);
        assertEquals("Login with Google", loginButton.getText());
        assertTrue(loginButton.isDisplayed());
    }

    /**
     * Test if the pop up window of Google Authentication login works.
     */
    @Test
    public void testGoogleAuthPopUp() {
        WebDriverWait wait = new WebDriverWait (driver, Duration.ofMillis(2000));
        // wait for a few second for the button to show up
        try{
            wait.until(driver -> driver.findElement(By.name("not-exist")));
        } catch(Exception ignored){
        }
        WebElement div = driver.findElement(By.className("googleLoginButton"));
        List<WebElement> buttons = div.findElements(By.tagName("button"));
        WebElement loginButton = buttons.get(0);
        loginButton.click();

        // get the selenium to switch to popup window
        Set<String> handles = driver.getWindowHandles(); // get all window handles
        Iterator<String> iterator = handles.iterator();
        String mainWindow = iterator.next();
        String popup = iterator.next();
        driver.switchTo().window(popup); // switch to popup window
        //driver.manage().window().maximize();

        // Now you are in the popup window, find the input for email
        List<WebElement> popup_inputs = driver.findElements(By.tagName("input"));
        WebElement emailInput = popup_inputs.get(0);
        emailInput.sendKeys("cs32.final.moral");
        //F29zPe
        // find the next button to enter  email
        List<WebElement> popup_buttons = driver.findElements(By.tagName("button"));
        WebElement nextButton = popup_buttons.get(3);
        nextButton.click();
        // wait for the next button to work
        try {
            wait.until(driver -> driver.findElement(By.name("not-exist")));

        } catch (Exception ignored) {
        }

        // find the input for password
        WebElement passwordDiv = driver.findElement(By.className("Xb9hP"));
        WebElement passwordInput = passwordDiv.findElement(By.tagName("input"));
        passwordInput.sendKeys("cs32finalmoral");
        // find the submit button to submit
        popup_buttons = driver.findElements(By.tagName("button"));
        WebElement submitButton = popup_buttons.get(1);
        submitButton.click();
        // successfully finish running the test means google login worked
        driver.switchTo().window(mainWindow);
        wait = new WebDriverWait (driver, Duration.ofMillis(3000));
        try {
            wait.until(driver -> driver.findElement(By.name("not-exist")));

        } catch (Exception ignored) {
        }
        WebElement about = driver.findElement(By.className("nav-about"));
        assertEquals("ABOUT", about.getText());
    }

    /**
     * Quit the chrome driver to clear any leftover states after tests.
     */
    @After
    public void end() {
        driver.quit();
    }

}