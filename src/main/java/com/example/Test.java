package com.example;

import java.time.Duration;
import java.util.Random;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.By;

import io.github.bonigarcia.wdm.WebDriverManager;

public class Test {

    public static void main(String[] args) {
        
        WebDriver webDriver = MyDriver();
        
        String url = "https://www.amazon.in/iQOO-Storage-Snapdragon-Processor-Brightness/dp/B07WHR9ZJ9";
        
        try {
            webDriver.get(url);

            
            // Wait a random time between 2-5 seconds
            //Thread.sleep(new Random().nextInt(3000) + 2000);

            String pageSource = webDriver.getPageSource();
            Document json = Jsoup.parse(pageSource);

            System.out.println("json: " + json.toString());
            org.jsoup.nodes.Element priceElement = json.selectFirst("span.a-price-whole");

            if (priceElement != null) {
                String priceWhole = priceElement.text();
                System.out.println("Price: " + priceWhole);

                priceWhole = priceWhole.replaceAll("[^0-9]", ""); 
                Double initialprice = Double.parseDouble(priceWhole);
                System.out.println("Parsed Price: " + initialprice);
            } else {
                System.out.println("Price not found.");
            }

        } catch (Exception e) {
            System.out.println("THIS IS ERROR OUT BECAUSE: " + e.toString());
        } finally {
            System.out.println("trying to quit");
            
            System.out.println("quited");
        }
    }
    
    public static WebDriver MyDriver() {
        WebDriverManager.chromedriver().setup();

        // Set Chrome options for headless mode
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless"); // Use headless mode
        options.addArguments("--no-sandbox"); // Bypass OS security model
        options.addArguments("--disable-dev-shm-usage"); // Overcome limited resource problems
        options.addArguments("--disable-gpu"); // Disable GPU hardware acceleration
        options.addArguments("--window-size=1920,1080"); // Set window size for consistent rendering

        // Set a realistic user-agent
        options.addArguments("user-agent=Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/130.0.6723.69 Safari/537.36");

        // Set Tor proxy (assuming Tor is running on port 9150)
        options.addArguments("--proxy-server=socks5://127.0.0.1:9150");
        options.addArguments("--host-resolver-rules=MAP * 0.0.0.0 , EXCLUDE 127.0.0.1");

        return new ChromeDriver(options);
    }
}
