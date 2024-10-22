package com.example;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.example.Entity.Product;
import com.example.Repo.ProductRepo;
import com.example.Service.AmezonService;

public class MyThread implements Runnable {

    private final Product product;
    private final ProductRepo productRepo; // Don't instantiate this manually
    private final AmezonService amezonService; // Don't instantiate this manually

    public MyThread(Product product, ProductRepo productRepo, AmezonService amezonService) {
        this.product = product;
        this.productRepo = productRepo;
        this.amezonService = amezonService;
    }

    @Override
    public void run() {
        Double liveprice = amezonService.chekingProductLivePrice(product);

        if (liveprice <= product.getDesirePrice()) {
            product.setJobStatus(false);
            productRepo.save(product);
            amezonService.sendEmail(product.getGmail(),
                    "VINIT AMEZON:YOUR PRODUCT PRICE DROPPED",
                    "YOUR INITIAL PRODUCT PRICE WAS " + product.getInitialPrice() +
                    " WHICH IS NOW BECOME " + liveprice +
                    " CHECK OUT BELOW LINK \n\n\n" + product.getUrl());
        }
        
        System.out.println(Thread.currentThread().getName()+":"+Thread.currentThread().getId()+" :"+"  finished execution");
    }
}
