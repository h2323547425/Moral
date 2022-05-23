package edu.brown.cs.student.frontend;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.BeforeClass;
import org.junit.AfterClass;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class AboutPageTest {
    private static final String PATH = "http://localhost:3000/";
    private static ChromeDriver driver;
    private static WebDriverWait wait;

    /**
     * Initialize the Selenium chrome driver before class. Pass the google
     * authentication login.
     */
    @BeforeClass
    public static void setupAndLogin() {
        WebDriverManager.chromedriver().setup();
        ChromeOptions options = new ChromeOptions();
        driver = new ChromeDriver(options);
        driver.manage().timeouts().implicitlyWait(Duration.ofMillis(500));
        driver.get(PATH);
        // wait for a few second for the button to show up
        wait = new WebDriverWait (driver, Duration.ofMillis(1000));
        try{
            wait.until(driver -> driver.findElement(By.name("not-exist")));
        } catch(Exception ignored){
        }
        // click the login button
        WebElement div = driver.findElement(By.className("googleLoginButton"));
        div.findElements(By.tagName("button")).get(0).click();

        // get the selenium to switch to popup window
        Iterator<String> iterator = driver.getWindowHandles().iterator();
        String mainWindow = iterator.next();
        String popup = iterator.next();
        driver.switchTo().window(popup);

        // input for email, password, and then submit
        driver.findElements(By.tagName("input")).get(0).sendKeys("cs32.final.moral");
        driver.findElements(By.tagName("button")).get(3).click();
        try {
            wait.until(driver -> driver.findElement(By.name("not-exist")));
        } catch (Exception ignored) {
        }
        driver.findElement(By.className("Xb9hP"))
                .findElement(By.tagName("input")).sendKeys("cs32finalmoral");
        driver.findElements(By.tagName("button")).get(1).click();

        // successfully finish running the test means google login worked
        driver.switchTo().window(mainWindow);
        wait = new WebDriverWait (driver, Duration.ofMillis(3000));
        try {
            wait.until(driver -> driver.findElement(By.name("not-exist")));

        } catch (Exception ignored) {
        }
    }

    /**
     * Test the navigation from home page to about page and vice versa.
     */
    @Test
    public void testAboutPageNavigation() {
        // navigate to the about page by clicking the link on top right corner
        WebElement aboutDiv = driver.findElement(By.className("nav-about"));
        WebElement aboutLink = aboutDiv.findElement(By.tagName("a"));
        aboutLink.click();
        // check if we are on the about page
        WebElement aboutPage = driver.findElement(By.className("about-page"));
        assertFalse(aboutPage == null);
        // navigate back to home page
        WebElement returnLink = driver.findElement(By.className("return-home"));
        returnLink.click();
        // check if we are on the home page
        WebElement homeContainer = driver.findElement(By.className("container"));
        assertFalse(homeContainer == null);
    }


    /**
     * Quit the chrome driver to clear any leftover states after tests.
     */
    @AfterClass
    public static void end() {
        driver.quit();
    }
}
