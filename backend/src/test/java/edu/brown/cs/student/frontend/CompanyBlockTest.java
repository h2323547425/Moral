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

public class CompanyBlockTest {
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
     * Test the presence of the company view block in the middle of the home page and the
     * correctness of the structure.
     */
    @Test
    public void testCompanyBlockStructure() {
        WebElement companyBlock = driver.findElement(By.className("company-block"));
        assertTrue(companyBlock.isDisplayed());
        // check the title
        WebElement blockTitle = companyBlock.findElement(By.tagName("h1"));
        assertTrue(blockTitle.isDisplayed());
        assertEquals("Company View", blockTitle.getText());
        assertEquals("font-title", blockTitle.getAttribute("class"));
        // check the paragraph description
        WebElement stockView = companyBlock.findElement(By.tagName("div"));
        assertTrue(stockView.isDisplayed());
        assertEquals("one-stock-view", stockView.getAttribute("class"));
    }


    /**
     * Test if the company logo is present and correctly displayed and linked.
     */
    @Test
    public void testLogo() {
        WebElement companyBlock = driver.findElement(By.className("company-block"));
        // test if the logo is present and correctly formatted
        WebElement logoDiv = companyBlock.findElement(By.className("logo-company"));
        assertTrue(logoDiv.isDisplayed());
        WebElement logoImage = logoDiv.findElement(By.tagName("img"));
        assertTrue(logoImage.isDisplayed());
        assertEquals("image for company", logoImage.getAttribute("alt"));
        WebElement logoLink = logoDiv.findElement(By.tagName("a"));
        assertTrue(logoLink.getAttribute("href").contains("https://www.google.com/search"));
    }

    /**
     * Test if the company title is present and correctly styled.
     */
    @Test
    public void testTitle() {
        WebElement companyBlock = driver.findElement(By.className("company-block"));
        // test if the company title is present and correctly formatted
        WebElement titleDiv = companyBlock.findElement(By.className("description-display"));
        assertTrue(titleDiv.isDisplayed());
        WebElement title = titleDiv.findElement(By.tagName("h1"));
        assertTrue(title.isDisplayed());
        assertEquals("Netflix Inc", title.getText());
        assertEquals("font", title.getAttribute("class"));
    }

    /**
     * Test if the company description and detail in the view is correct.
     */
    @Test
    public void testDescription() {
        WebElement companyBlock = driver.findElement(By.className("company-block"));
        // test if the company title is present and correctly formatted
        WebElement detailDiv = companyBlock.findElement(By.className("parent3"));
        assertTrue(detailDiv.isDisplayed());
        // check if the do well div matches expectation
        WebElement doWellDiv = detailDiv.findElement(By.className("field_do_well"));
        assertTrue(doWellDiv.isDisplayed());
        List<WebElement> doWellList = doWellDiv.findElements(By.tagName("p"));
        assertEquals(5, doWellList.size());
        // check if the company-score div matches expectation
        WebElement scoreDiv = detailDiv.findElement(By.className("company-score"));
        assertTrue(scoreDiv.isDisplayed());
        List<WebElement> scoreList = scoreDiv.findElements(By.tagName("h3"));
        assertEquals(5, scoreList.size());
    }

    /**
     * Quit the chrome driver to clear any leftover states after tests.
     */
    @AfterClass
    public static void end() {
        driver.quit();
    }
}
