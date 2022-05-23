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

public class FavoritesPageTest {
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
     * Test the navigation from home page to favorites page and vice versa.
     */
    @Test
    public void testFavoritesPageNavigation() {
        // navigate to the favorites page by clicking the icon on top left corner
        WebElement heartDiv = driver.findElement(By.className("heart"));
        WebElement heartImg = heartDiv.findElement(By.tagName("img"));
        heartImg.click();
        // check if we are on the favorites page
        WebElement favoritesHeader = driver.findElement(By.className("favorites-header"));
        assertFalse(favoritesHeader == null);
        // navigate back to home page
        WebElement returnLink = driver.findElement(By.className("return-home"));
        returnLink.click();
        // check if we are on the home page
        WebElement homeContainer = driver.findElement(By.className("container"));
        assertFalse(homeContainer == null);
    }

    /**
     * Test the favorite list on the page is correct.
     */
    @Test
    public void testFavoritesStockList() {
        // navigate to the favorites page by clicking the icon on top left corner
        driver.findElement(By.className("heart"))
                .findElement(By.tagName("img")).click();

        // check if we are on the favorites list is correct
        WebElement favoritesBlock = driver.findElement(By.className("fave-stock-list-block"));
        List<WebElement> favoritesList = favoritesBlock.findElements(By.className("fave-stock"));
        assertEquals(3, favoritesList.size());
        assertEquals("OCN", favoritesList.get(0).getAttribute("id"));
        assertEquals("UBER", favoritesList.get(1).getAttribute("id"));
        assertEquals("QRTEA", favoritesList.get(2).getAttribute("id"));

        // navigate back to home page
        driver.findElement(By.className("return-home")).click();
    }

    /**
     * Quit the chrome driver to clear any leftover states after tests.
     */
    @AfterClass
    public static void end() {
        driver.quit();
    }
}
