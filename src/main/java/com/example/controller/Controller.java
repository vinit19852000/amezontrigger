package com.example.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.Trigger;
import com.example.Entity.Product;
import com.example.Model.Mydata;
import com.example.Repo.ProductRepo;
import com.example.Service.AmezonService;



@RestController
@RequestMapping("/amezon")
public class Controller {

	@Autowired
	Trigger trigger;
	
	@Autowired
	AmezonService amezonService;
	
	@Autowired
	ProductRepo  productRepo;
	
	@GetMapping("/allproduct")
	public ResponseEntity<List<Product>> getProductList(){
		
		List<Product> list=productRepo.findAll();
		
		return ResponseEntity.ok(list);
	}
	
	
	@GetMapping("/deleteall")
	public String delete() {
		
		productRepo.deleteAll();
		return "deleted";
	}	
	@GetMapping("/droptable")
	public String drop() {
		
		productRepo.dropTable();
		
		return "dropped";
	}
	
	@GetMapping("/trigger")
	public String trigger() throws InterruptedException {
		
		
	    new Thread(() -> {
	        try {
	            trigger.run(); // Run in a separate thread
	        } catch (InterruptedException e) {
	            Thread.currentThread().interrupt(); // Handle interruption
	        }
	    }).start();
		return "triger start";
	}
	
	@PostMapping("/savealert")
	public ResponseEntity<String>  AddProduct(@RequestBody Product product) {
		
		
		

             boolean result=(product.getUrl()!=null)?amezonService.checkingProductFirsttime(product):false;
             
             String SUCCESS="YOUR AMEZON PRICE ALERT  ACTIVATED"+"\n"+"YOU WILL BE SHORTLY  NOTIFIED WHEN PRICE DROPS FROM "+product.getInitialPrice()+" TO "+product.getDesirePrice()+" ";
             String FAILED="PLEASE CHECK PRODUCT  LINK AGAIN";
             
             
             return (result)?ResponseEntity.ok(SUCCESS):ResponseEntity.status(HttpStatus.BAD_REQUEST).body(FAILED);
             
	}
}
