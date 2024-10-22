package com.example.Repo;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.Entity.Product;

public interface ProductRepo extends JpaRepository<Product, Long> {

	
	@Query(value = "DROP TABLE IF EXISTS product", nativeQuery = true)
    void dropTable();
	
    @Query("SELECT p FROM Product p WHERE p.desireDate >= :todayDate AND p.jobStatus = true")
    List<Product> findAllByDesireDateAndJobStatus(@Param("todayDate") LocalDate todayDate);
	
}
