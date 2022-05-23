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

public class FilterBlockTest {
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
     * Test the presence of the filter block on the left of the home page and the
     * correctness of the text.
     */
    @Test
    public void testFilterBlockText() {
        WebElement filterBlock = driver.findElement(By.className("filter-block"));
        assertTrue(filterBlock.isDisplayed());
        // test the title "filter"
        WebElement filterTitle = filterBlock.findElement(By.tagName("h1"));
        assertTrue(filterTitle.isDisplayed());
        assertEquals("Filter", filterTitle.getText());
        assertEquals("font-title", filterTitle.getAttribute("class"));
        // check the paragraph description
        WebElement filterDescription = filterBlock.findElement(By.tagName("p"));
        assertTrue(filterDescription.isDisplayed());
        assertEquals("Use the sliding bars to denote the level of importance of " +
                "the following categories", filterDescription.getText());
    }


    /**
     * Test if the filters are correctly displayed.
     */
    @Test
    public void testFilters() {
        WebElement filterBlock = driver.findElement(By.className("filter-block"));
        // test the number of filter is correct
        List<WebElement> filters = filterBlock.findElements(By.className("horizontal-slider"));
        assertEquals(5, filters.size());
        // check the number of elements for each filter
        assertEquals(3, filters.get(0).findElements(By.tagName("div")).size());
        assertEquals(3, filters.get(1).findElements(By.tagName("div")).size());
        assertEquals(3, filters.get(2).findElements(By.tagName("div")).size());
        assertEquals(3, filters.get(3).findElements(By.tagName("div")).size());
        assertEquals(3, filters.get(4).findElements(By.tagName("div")).size());
    }

    /**
     * Quit the chrome driver to clear any leftover states after tests.
     */
    @AfterClass
    public static void end() {
        driver.quit();
    }
}
