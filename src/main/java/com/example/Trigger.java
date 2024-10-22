package com.example;

import java.time.LocalDate;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import com.example.Entity.Product;
import com.example.Repo.ProductRepo;
import com.example.Service.AmezonService;


@Configuration
public class Trigger {
	
	
	@Autowired
	ProductRepo productRepo;
	
	@Autowired
	private MyThreadFactory myThreadFactory;

	
	@Autowired
	private AmezonService amezonService;

	public void run() throws InterruptedException {
		
		
		while(true) {
			
			List<Product> list=productRepo.findAllByDesireDateAndJobStatus(LocalDate.now());
			
			
			for(Product product:list) {
				System.out.println("product:"+product.getId());
			}
			ExecutorService service=Executors.newFixedThreadPool(list.size());
			
			
			
			 for(int i=0;i<list.size();i++) {
				 
				  MyThread myThread = new MyThread( list.get(i),productRepo, amezonService);
				    service.execute(myThread);
			 }
			 
			 
			 
			 service.shutdown();
	         // Wait for all tasks to complete
	         if (!service.awaitTermination(3, TimeUnit.MINUTES)) {
	             System.err.println("Tasks did not finish in the allotted time.");
	         }

	         // Now all tasks are complete
	         System.out.println("All tasks completed. Now we can run the main thread.");
	         
			
			 System.out.print("list size:"+ list.size());
			
			
			Thread.sleep(1000*60*3);
		}
	}

}
