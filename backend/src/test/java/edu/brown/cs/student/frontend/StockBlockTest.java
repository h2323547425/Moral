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

public class StockBlockTest {
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
     * Test the presence of the stock list block in the middle of the home page and the
     * correctness of the structure.
     */
    @Test
    public void testStockBlockStructure() {
        WebElement stockBlock = driver.findElement(By.className("stock-list-block"));
        assertTrue(stockBlock.isDisplayed());
        // test the title
        WebElement stockTitle = stockBlock.findElement(By.tagName("h1"));
        assertTrue(stockTitle.isDisplayed());
        assertEquals("Suggested Stocks", stockTitle.getText());
        assertEquals("font-title", stockTitle.getAttribute("class"));
        // check if the main division is class "scrollable"
        WebElement stockDiv = stockBlock.findElement(By.tagName("div"));
        assertTrue(stockDiv.isDisplayed());
        assertEquals("scrollable", stockDiv.getAttribute("class"));
    }


    /**
     * Test if the stock suggestion are the correct number and order.
     */
    @Test
    public void testStockSuggestions() {
        WebElement stockBlock = driver.findElement(By.className("stock-list-block"));
        // test the list of div elements with class "stock"
        List<WebElement> stockList = stockBlock.findElements(By.className("stock"));
        assertTrue(400 < stockList.size());
        // test the first few see if they match
        assertEquals("Netflix, Inc.", stockList.get(0).getAttribute("id"));
        assertEquals("Cooper-Standard Holdings Inc.", stockList.get(1).getAttribute("id"));
        assertEquals("PayPal Holdings, Inc.", stockList.get(2).getAttribute("id"));
        assertEquals("Block, Inc.", stockList.get(3).getAttribute("id"));
    }

    /**
     * Test if the stock suggestion are the correct number and order.
     */
    @Test
    public void testStockStructure() {
        WebElement stockBlock = driver.findElement(By.className("stock-list-block"));
        // test the list of div elements with class "stock"
        WebElement stock = stockBlock.findElement(By.className("stock"));
        // test if stock suggestion has a stockname, price, score, title, and a like button
        WebElement stockName = stockBlock.findElement(By.className("stockname"));
        assertTrue(stockName.isDisplayed());
        WebElement stockPrice = stockBlock.findElement(By.className("price"));
        assertTrue(stockPrice.isDisplayed());
        WebElement stockScore = stockBlock.findElement(By.className("score"));
        assertTrue(stockScore.isDisplayed());
        WebElement stockTitle = stockBlock.findElement(By.className("font-little"));
        assertTrue(stockTitle.isDisplayed());
        WebElement likeButton = stockBlock.findElement(By.className("LikeButton"));
        assertTrue(likeButton.isDisplayed());
    }

    /**
     * Test if the like button is clickable and interactable.
     */
    @Test
    public void testLikeButton() {
        WebElement stockBlock = driver.findElement(By.className("stock-list-block"));
        WebElement likeButton = stockBlock.findElement(By.className("LikeButton"));
        // save the class name for the span element before click
        WebElement likeSpan = likeButton.findElement(By.tagName("span"));
        String spanClass = likeSpan.getAttribute("class");
        // click the like input
        WebElement likeInput = likeButton.findElement(By.tagName("input"));
        assertEquals("PrivateSwitchBase-input-4", likeInput.getAttribute("class"));
        likeInput.click();
        // check if the span element class name changed
        likeSpan = likeButton.findElement(By.tagName("span"));
        assertFalse(likeSpan.getAttribute("class").equals(spanClass));
        // check if the span element class name changed back
        likeInput.click();
        likeSpan = likeButton.findElement(By.tagName("span"));
        assertTrue(likeSpan.getAttribute("class").equals(spanClass));
    }

    /**
     * Quit the chrome driver to clear any leftover states after tests.
     */
    @AfterClass
    public static void end() {
        driver.quit();
    }
}
