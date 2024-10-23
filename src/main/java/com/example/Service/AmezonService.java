package com.example.Service;


import java.time.Duration;
import java.util.Random;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.example.SeleniumConfig;
import com.example.Entity.Product;
import com.example.Repo.ProductRepo;

@Service
public class AmezonService {

	
	@Autowired
	ProductRepo productRepo;
	
	

   
	  @Autowired
	    private JavaMailSender mailSender;

	    public   void sendEmail(String to, String subject, String text) {
	        SimpleMailMessage message = new SimpleMailMessage();
	        message.setTo(to);
	        message.setSubject(subject);
	        message.setText(text);
	        message.setFrom("stockapp1985@gmail.com"); // Replace with your email
	        mailSender.send(message);
	        System.out.println("Email sent to " + to);
	    }
	
	
		public Double chekingProductLivePrice(Product product) {

			
			String url=product.getUrl();
			Double initialPrice=product.getInitialPrice();
			
			
			try {

			      SeleniumConfig seleniumConfig = new SeleniumConfig();
			      WebDriver webDriver = seleniumConfig.webDriver(); // Creating WebDriver without @Autowired

				webDriver.get(url);
				  String pageSource = webDriver.getPageSource();
				
				Document json=Jsoup.parse(pageSource);
				
				System.out.println("json:"+json);
				
			     org.jsoup.nodes.Element priceElement = json.selectFirst("span.a-price-whole");

		         if (priceElement != null) {
		             String priceWhole = priceElement.text();
		             System.out.println("Price: " + priceWhole);
		            
		             Double live=Double.parseDouble(priceWhole);
		             
                        initialPrice=live;
		         } else {
		        	 
		        	 
		             System.out.println("Price not found.");
		         }

		         webDriver.quit();
		     
			}catch(Exception e) {
				
			}
			
	          return initialPrice;
		}
	  
	    
	public  boolean checkingProductFirsttime(Product product) {

		

		 System.out.println("desiredate:"+product.getDesireDate());
		 System.out.println("desirepirce:"+product.getDesirePrice());
		 System.out.println("URL:"+product.getUrl());
		String url=product.getUrl();
		
		 boolean result = false;
	      SeleniumConfig seleniumConfig = new SeleniumConfig();
	      WebDriver webDriver = seleniumConfig.webDriver(); // Creating WebDriver without @Autowired

	        try {


	            webDriver.get(url);
	            webDriver.manage().timeouts().implicitlyWait(Duration.ofSeconds(3));

	            String pageSource = webDriver.getPageSource();
	            Document json = Jsoup.parse(pageSource);

	            System.out.println("json: " + json.toString());
	            Element priceElement = json.selectFirst("span.a-price-whole");

	            if (priceElement != null) {
	                String priceWhole = priceElement.text();
	                System.out.println("Price: " + priceWhole);
	                
	                priceWhole = priceWhole.replaceAll("[^0-9]", "");
	                Double initialPrice = Double.parseDouble(priceWhole);
	                
	                product.setInitialPrice(initialPrice);
	                productRepo.save(product);
	                result = true;
	            } else {
	                System.out.println("Price not found. Checking for CAPTCHA...");

	                // Attempt to find and click "Try different image" link if price is not found
	                try {
	                	Thread.sleep(new Random().nextInt(2000, 5000));
	                    WebElement tryDifferentImageLink = webDriver.findElement(By.xpath("//a[contains(@onclick, 'window.location.reload')]"));
	                    if (tryDifferentImageLink != null) {
	                        System.out.println("Clicking 'Try different image' link.");
	                        tryDifferentImageLink.click();

	                        // Wait for the page to reload and check the price again
	                        webDriver.manage().timeouts().implicitlyWait(Duration.ofSeconds(3));
	                        pageSource = webDriver.getPageSource();
	                        json = Jsoup.parse(pageSource);

	                        priceElement = json.selectFirst("span.a-price-whole");
	                        if (priceElement != null) {
	                            String priceWhole = priceElement.text();
	                            System.out.println("Price after refreshing: " + priceWhole);

	                            priceWhole = priceWhole.replaceAll("[^0-9]", "");
	                            Double initialPrice = Double.parseDouble(priceWhole);

	                            product.setInitialPrice(initialPrice);
	                            productRepo.save(product);
	                            result = true;
	                        } else {
	                            System.out.println("Price still not found after refreshing.");
	                        }
	                    }
	                } catch (Exception e) {
	                    System.out.println("Error while trying to click 'Try different image': " + e.toString());
	                }
	            }

	        } catch (Exception e) {
	            System.out.println("THIS IS ERROR OUT BECAUSE: " + e.toString());
	        } finally {
	            System.out.println("Trying to quit");
	            webDriver.quit();
	            System.out.println("Quited");
	        }

	        return result;	}
}
