package com.example.Entity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

import org.springframework.data.annotation.CreatedDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

@Entity

@Data
public class Product {

	
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	Long id;
	
	
	@Column(columnDefinition = "TEXT") 
	private String url;
	private Double desirePrice;
	private Double initialPrice;
	
	@CreatedDate
	private LocalDate createdDate=LocalDate.now();
	
	private LocalDate desireDate;
	
	private String gmail;
	
	private Boolean jobStatus=true;
	
}
