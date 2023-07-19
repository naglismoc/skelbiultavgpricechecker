import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class Skelbiu {
    public static WebDriver driver;
    public static ArrayList<String> urls = new ArrayList<>();
    public static String search = "triratukas";

    @Test
    public void baigesFantazija(){
        pagination();
        openUrls();
    }

    public void openUrls(){
        double sum = 0;
        int countIncompatibles = 0;
        for (String url : urls  ) {
            driver.get(url);
            try {
                double price = Double.parseDouble(driver.findElement(By.className("price")).getText().split(" ")[0]);
               // System.out.println(price);
                sum += price;
            }catch (Exception e){
                countIncompatibles++;
            }
        }
        System.out.println(search + " kainų vidurkis yra " +  Math.round( sum / (urls.size() - countIncompatibles)  * 100.0) / 100.0+"€.");
    }

    public void pagination(){
        for (int i = 1; i < 201 ; i++) {
            String url = "https://www.skelbiu.lt/skelbimai/" + i + "?keywords=" + search;
            driver.get(url);
            if(!url.equals(driver.getCurrentUrl())){
                return;
            }
          //  System.out.println(driver.getCurrentUrl());
            getUrls();
        }
    }
    public void getUrls(){

        List<WebElement> cards = Stream.concat(
                driver.findElement(By.id("itemsList")).findElements(By.className("simpleAds")).stream(),
                driver.findElement(By.id("itemsList")).findElements(By.className("boldAds")).stream()
        ).toList();

        for (WebElement card: cards ) {
           String url = card.findElement(By.tagName("a")).getAttribute("href");
           urls.add(url);
        }
    }

    @BeforeClass
    public void beforeClass(){
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(20));
        acceptCookies();
    }
    public void acceptCookies(){
        driver.get("https://www.skelbiu.lt");
        driver.findElement(By.id("onetrust-accept-btn-handler")).click();
    }

    @AfterClass
    public void afterClass(){
        // driver.quit();
    }
}
