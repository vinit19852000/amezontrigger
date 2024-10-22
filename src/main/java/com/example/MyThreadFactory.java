package com.example;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.Entity.Product;
import com.example.Repo.ProductRepo;
import com.example.Service.AmezonService;

@Service
public class MyThreadFactory {

    @Autowired
    private ProductRepo productRepo;

    @Autowired
    private AmezonService amezonService;

    public MyThread createMyThread(Product product) {
        return new MyThread(product, productRepo, amezonService);
    }
}
