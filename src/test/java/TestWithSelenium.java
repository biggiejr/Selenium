/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.util.List;
import static org.hamcrest.CoreMatchers.is;
import org.junit.AfterClass;
import org.junit.Assert;
import static org.junit.Assert.assertThat;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;

/**
 *
 * @author Mato
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TestWithSelenium {

    final int MAX = 5;
    static WebDriver driver;

    public TestWithSelenium() {
    }

    @BeforeClass
    public static void setUp() {
        System.setProperty("webdriver.gecko.driver", "/Users/Mato/Documents/SeleniumWebDriver/geckodriver");
        System.setProperty("webdriver.chrome.driver", "/Users/Mato/Documents/SeleniumWebDriver/chromedriver");

        //data reset
        com.jayway.restassured.RestAssured.given()
                .get("http://localhost:3000/reset");
        driver = new ChromeDriver();

        driver.get("http://localhost:3000");
    }

    @Test
    public void tDomConstructedTableReady() {
        //searching for tbody
        WebElement table = (new WebDriverWait(driver, MAX)).until((ExpectedCondition<WebElement>) (WebDriver d) -> {
            return d.findElement(By.tagName("tbody"));
        });

        Assert.assertThat(table.findElements(By.tagName("tr")).size(), is(5));
    }

    @Test
    public void uTest2002Filter() {
        //searching for filter & sending key
        WebElement filter = (new WebDriverWait(driver, MAX)).until((ExpectedCondition<WebElement>) (WebDriver d) -> {
            return d.findElement(By.id("filter"));
        });
        filter.sendKeys("2002");

        //searching for tbody
        WebElement table = (new WebDriverWait(driver, MAX)).until((ExpectedCondition<WebElement>) (WebDriver d) -> {
            return d.findElement(By.tagName("tbody"));
        });

        //checking whether number of rows is 2
        Assert.assertThat(table.findElements(By.tagName("tr")).size(), is(2));

    }

    @Test
    public void vTestClearFilter() {
        //searching for filter & sending key
        WebElement filter = (new WebDriverWait(driver, MAX)).until((ExpectedCondition<WebElement>) (WebDriver d) -> {
            return d.findElement(By.id("filter"));
        });

        filter.sendKeys(Keys.BACK_SPACE);

        //searching for tbody
        WebElement table = (new WebDriverWait(driver, MAX)).until((ExpectedCondition<WebElement>) (WebDriver d) -> {
            return d.findElement(By.tagName("tbody"));
        });

        //checking whether number of rows is 5
        Assert.assertThat(table.findElements(By.tagName("tr")).size(), is(5));
    }

    @Test
    public void wTestSorting() {
        //searching for sort button
        WebElement sortButton = (new WebDriverWait(driver, MAX)).until((ExpectedCondition<WebElement>) (WebDriver d) -> {
            return d.findElement(By.id("h_year"));
        });
        sortButton.click();

        //searching for tbody
        WebElement table = (new WebDriverWait(driver, MAX)).until((ExpectedCondition<WebElement>) (WebDriver d) -> {
            return d.findElement(By.tagName("tbody"));
        });

        //listing all tr
        List<WebElement> tableSorted = table.findElements(By.tagName("tr"));
        Assert.assertThat(tableSorted.get(0).findElements(By.tagName("td")).get(0).getText(), is("938"));
        Assert.assertThat(tableSorted.get(4).findElements(By.tagName("td")).get(0).getText(), is("940"));

    }

//    Press the edit button for the car with the id 938. Change the Description to "Cool car", and save changes.
//	Verify that the row for car with id 938 now contains "Cool car" in the Description column
    @Test
    public void xTestEdit() {
        //searching for tbody
        List<WebElement> tableRows = (new WebDriverWait(driver, MAX)).until((ExpectedCondition<WebElement>) (WebDriver d) -> {
            return d.findElement(By.tagName("tbody"));
        }).findElements(By.tagName("tr"));

        WebElement editButton = null;
        for (int i = 0; i < tableRows.size(); i++) {
            if (tableRows.get(i).findElements(By.tagName("td")).get(0).getText().equalsIgnoreCase("938")) {
                editButton = tableRows.get(i);
                break;
            }
        }

        editButton.findElements(By.tagName("td")).get(7).findElements(By.tagName("a")).get(0).click();
        WebElement element = driver.findElement(By.id("description"));
        element.clear();
        element.sendKeys("Cool car");
        driver.findElement(By.id("save")).click();

        //searching for new tbody
        List<WebElement> tableRowsUpdated = (new WebDriverWait(driver, MAX)).until((ExpectedCondition<WebElement>) (WebDriver d) -> {
            return d.findElement(By.tagName("tbody"));
        }).findElements(By.tagName("tr"));

        String descriptionNew = "";
        for (int i = 0; i < tableRowsUpdated.size(); i++) {
            if (tableRowsUpdated.get(i).findElements(By.tagName("td")).get(0).getText().equalsIgnoreCase("938")) {
                descriptionNew = tableRowsUpdated.get(i).findElements(By.tagName("td")).get(5).getText();
                break;
            }
        }
        assertThat(descriptionNew, is("Cool car"));
    }

    @Test
    public void yTestError() {
        driver.findElement(By.id("new")).click();
        driver.findElement(By.id("save")).click();

        String submiterrMessage = (new WebDriverWait(driver, MAX)).until((ExpectedCondition<String>) (WebDriver d) -> {
            return d.findElement(By.id("submiterr")).getText();
        });

        assertThat(submiterrMessage, is("All fields are required"));

    }

    @Test
    public void zAddNew() {
        driver.findElement(By.id("new")).click();
        driver.findElement(By.id("year")).sendKeys("2008");
        driver.findElement(By.id("registered")).sendKeys("2002-5-5");
        driver.findElement(By.id("make")).sendKeys("Kia");
        driver.findElement(By.id("model")).sendKeys("Rio");
        driver.findElement(By.id("description")).sendKeys("As new");
        driver.findElement(By.id("price")).sendKeys("31000");
        driver.findElement(By.id("save")).click();

        WebElement table = (new WebDriverWait(driver, MAX)).until((ExpectedCondition<WebElement>) (WebDriver d) -> {
            return d.findElement(By.tagName("tbody"));
        });
        Assert.assertThat(table.findElements(By.tagName("tr")).size(), is(6));

    }

    @AfterClass
    public static void tearDown() {
        driver.quit();
    }

}
