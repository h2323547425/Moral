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

public class NavigationBarTest {
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
     * Test the presence of the navigation bar on the top of the home page.
     */
    @Test
    public void testNavigationBar() {
        // check if the navigation bar is present
        WebElement navBar = driver.findElement(By.className("nav-bar"));
        assertTrue(navBar.isDisplayed());
    }

    /**
     * Test if the account section is present and correct in the navigation bar.
     */
    @Test
    public void testAccountDiv() {
        WebElement navBar = driver.findElement(By.className("nav-bar"));
        // check if the section is present
        WebElement accDiv = navBar.findElement(By.className("account"));
        assertTrue(accDiv.isDisplayed());
        // test if the user button is correct
        WebElement userButton = accDiv.findElement(By.className("user-button"));
        assertTrue(userButton.isDisplayed());
        assertEquals("button", userButton.getTagName());
        // test if the google logout section and button is hidden
        WebElement logoutDiv = accDiv.findElement(By.className("googleLogoutButton"));
        assertFalse(logoutDiv.isDisplayed());
        WebElement logoutButton = logoutDiv.findElement(By.tagName("button"));
        assertFalse(logoutButton.isDisplayed());
    }

    /**
     * Test if the logo section is present and correct in the navigation bar.
     */
    @Test
    public void testLogoDiv() {
        WebElement navBar = driver.findElement(By.className("nav-bar"));
        // check if the logo division is present
        WebElement logoDiv = navBar.findElement(By.className("logo"));
        assertTrue(logoDiv.isDisplayed());
        // check if the logo image is correct
        WebElement logo = logoDiv.findElement(By.tagName("img"));
        assertTrue(logo.isDisplayed());
        assertEquals("logo-image", logo.getAttribute("class"));
    }

    /**
     * Test if the heart section is present and correct in the navigation bar.
     */
    @Test
    public void testHeartDiv() {
        WebElement navBar = driver.findElement(By.className("nav-bar"));
        // check if the heart division is present
        WebElement heartDiv = navBar.findElement(By.className("heart"));
        assertTrue(heartDiv.isDisplayed());
        // check if the heart image is correct
        WebElement heart = heartDiv.findElement(By.tagName("img"));
        assertTrue(heart.isDisplayed());
        assertEquals("heart-image", heart.getAttribute("class"));
    }

    /**
     * Test if the about section is present and correct in the navigation bar.
     */
    @Test
    public void testAboutDiv() {
        WebElement navBar = driver.findElement(By.className("nav-bar"));
        // check if the about division is present
        WebElement aboutDiv = navBar.findElement(By.className("nav-about"));
        assertTrue(aboutDiv.isDisplayed());
        // check if the about link is correct
        WebElement aboutLink = aboutDiv.findElement(By.tagName("a"));
        assertTrue(aboutLink.isDisplayed());
        assertEquals("http://localhost:3000/home#", aboutLink.getAttribute("href"));
        assertEquals("ABOUT", aboutLink.getText());
    }

    /**
     * Quit the chrome driver to clear any leftover states after tests.
     */
    @AfterClass
    public static void end() {
        driver.quit();
    }
}
