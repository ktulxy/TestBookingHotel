import org.apache.commons.io.FileUtils;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.logging.LogEntries;
import org.openqa.selenium.logging.LogEntry;
import org.openqa.selenium.logging.LogType;
import org.openqa.selenium.logging.LoggingPreferences;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;


public class TestReservation {

    private static WebDriver driver;

    @BeforeClass
    public static void comeIn()
    {
        System.setProperty("webdriver.chrome.driver", "C:\\chromedriver.exe");
        DesiredCapabilities caps = DesiredCapabilities.chrome();
        LoggingPreferences logPrefs = new LoggingPreferences();
        logPrefs.enable(LogType.BROWSER, Level.ALL);
        caps.setCapability(CapabilityType.LOGGING_PREFS, logPrefs);
        driver = new ChromeDriver(caps);
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        driver.manage().window().maximize();
        driver.get("http://knickerbocker-hotel-new-york.nochi.com/?btest=119");
    }



    public void analyzeLog() throws IOException
    {
        LogEntries logEntries = driver.manage().logs().get(LogType.BROWSER);
        for (LogEntry entry : logEntries)
        {
            String logi = new Date(entry.getTimestamp()) + " " + entry.getLevel() + " " + entry.getMessage();
            FileUtils.writeStringToFile(new File("C:/Logs/Logs.txt"), logi);
        }
    }

    private void save(String nameFile) throws IOException
    {
        File scrFile = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
        FileUtils.copyFile(scrFile, new File("C:\\screenshots\\"+ nameFile+".jpg"));
    }



    @Test
    public void booking() throws IOException, InterruptedException
    {
        driver.findElement(By.xpath("(.//*[normalize-space(text()) and normalize-space(.)='сб'])[2]/following::a[1]")).click();
        Thread.sleep(500);
        save("0");
        Thread.sleep(1000);
        driver.findElement(By.xpath("(//a[contains(@href, '#facilities')])[2]")).click();
        save("1");
        driver.findElement(By.xpath("(.//*[normalize-space(text()) and normalize-space(.)='Удобства'])[3]/following::a[1]")).click();
        save("2");
        driver.findElement(By.xpath("(.//*[normalize-space(text()) and normalize-space(.)='Номера'])[3]/following::a[1]")).click();
        Thread.sleep(500);
        save("3");
        driver.findElement(By.xpath("(.//*[normalize-space(text()) and normalize-space(.)='Расположение'])[3]/following::a[1]")).click();
        Thread.sleep(500);
        save("4");

        ((JavascriptExecutor)driver).executeScript("scroll(0,400)");

        driver.findElement(By.xpath("(.//*[normalize-space(text()) and normalize-space(.)='Дата заезда'])[1]/following::span[3]")).click();
        driver.findElement(By.cssSelector("svg.svg-icon.svg-icon-calendar-arrow.arrow-next")).click();
        driver.findElement(By.cssSelector("svg.svg-icon.svg-icon-calendar-arrow.arrow-next")).click();
        driver.findElement(By.cssSelector("svg.svg-icon.svg-icon-calendar-arrow.arrow-next")).click();
        driver.findElement(By.cssSelector("svg.svg-icon.svg-icon-calendar-arrow.arrow-next")).click();
        driver.findElement(By.xpath("(.//*[normalize-space(text()) and normalize-space(.)='сб'])[2]/following::div[26]")).click();
        driver.findElement(By.xpath("(.//*[normalize-space(text()) and normalize-space(.)='сб'])[2]/following::div[27]")).click();
        driver.findElement(By.xpath("(.//*[normalize-space(text()) and normalize-space(.)='Гости /'])[1]/following::span[2]")).click();
        driver.findElement(By.xpath("(.//*[normalize-space(text()) and normalize-space(.)='Взрослый'])[1]/following::div[4]")).click();
        driver.findElement(By.xpath("(.//*[normalize-space(text()) and normalize-space(.)='Ребенок'])[1]/following::div[4]")).click();
        driver.findElement(By.xpath("(.//*[normalize-space(text()) and normalize-space(.)='Ребенок'])[1]/following::div[4]")).click();

        WebElement child1 = driver.findElement(By.xpath("(.//*[normalize-space(text()) and normalize-space(.)='Возраст детей'])[1]/following::select[1]"));
        WebElement countguests = driver.findElement(By.xpath("(.//*[normalize-space(text()) and normalize-space(.)='Номера и Гости'])[1]/following::span[1]"));
        String count = countguests.getText();
        Assert.assertEquals(count,"5");
        WebElement countrooms = driver.findElement(By.xpath("(.//*[normalize-space(text()) and normalize-space(.)='Гости /'])[1]/following::span[1]"));
        String count1 = countrooms.getText();
        Assert.assertEquals(count1,"1");

        child1.click();
        for (int i = 0; i<=2; i++)
        {
            child1.sendKeys(Keys.ARROW_DOWN);
        }
        child1.sendKeys(Keys.ENTER);

        WebElement child2 = driver.findElement(By.xpath("(.//*[normalize-space(text()) and normalize-space(.)='Возраст детей'])[1]/following::select[2]"));
        child2.click();
        for (int i = 0; i <= 10; i++)
        {
            child2.sendKeys(Keys.ARROW_DOWN);
        }
        child2.sendKeys(Keys.ENTER);

        String currentWindow = driver.getWindowHandle();
        driver.findElement(By.xpath("(.//*[normalize-space(text()) and normalize-space(.)='лучшие онлайн предложения!'])[1]/following::button[1]")).click();
        Thread.sleep(2000);

        for (String handle : driver.getWindowHandles()) {
            if (!handle.equalsIgnoreCase(currentWindow)) {
                driver.switchTo().window(handle);
                Thread.sleep(2000, 0);
                break;
            }
        }
        driver.findElement(By.xpath("//div[4]/button")).click();
        Thread.sleep(3000);
        save("5");
        WebElement arrivalDate = driver.findElement(By.xpath("(.//*[normalize-space(text()) and normalize-space(.)='Дата заезда:'])[1]/following::span[1]"));
        String arriveDate1 = arrivalDate.getText();
        Assert.assertEquals(arriveDate1,"2019-01-24");
        WebElement departureDate = driver.findElement(By.xpath("//div[@id='container']/div[3]/div/div/div[3]/div/div[2]/span[2]"));
        String departureDate1 = departureDate.getText();
        Assert.assertEquals(departureDate1,"2019-01-25");
        WebElement numberOfPeople = driver.findElement(By.xpath("//div[@id='container']/div[3]/div/div/div[3]/div[2]/div/span[2]"));
        String numberOfPeople1 = numberOfPeople.getText();
        Assert.assertEquals(numberOfPeople1,"3 Взрослых,\n" + "2 Детей");
        analyzeLog();
    }

    @AfterClass
    public static void end()
    {
        driver.quit();
    }
}

