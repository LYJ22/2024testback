package com.sample.spring.api;

import org.springframework.web.bind.annotation.RestController;

import com.sample.spring.api.request.CreateAndEditFoodRequest;
import com.sample.spring.api.response.FoodDetailView;
import com.sample.spring.api.response.FoodView;
import com.sample.spring.model.FoodEntity;
import com.sample.spring.service.FoodService;

import java.time.ZonedDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;


@RestController
public class FoodApi {
	
	@Autowired
	private FoodService foodservice;
	
	@GetMapping("/foods")
	public List<FoodView> getFoods() {
		return foodservice.getAllFoods();
	}
	
	@GetMapping("/food/{foodId}")
	public FoodDetailView viewFoods(
			@PathVariable("foodId") Long foodId
			) {
		return foodservice.getFoodDetail(foodId);
	}
	
	@PostMapping("/food")
	public FoodEntity postFood(
			@RequestBody CreateAndEditFoodRequest request
			){
		return foodservice.createFood(request);
//		return "postFood / name: " + request.getName() + ", address: " + request.getAddress() +", 메뉴[0]: " + request.getMenus().get(1).getName();
	}
	
	@PutMapping("/food/{foodId}")
	public String editFood(
			@PathVariable("foodId") Long foodId,
			@RequestBody CreateAndEditFoodRequest request
			) {
		foodservice.editFood(foodId, request);
		return "editFood"+ foodId + ", / name: " + request.getName() + ", address: " + request.getAddress();
	}
	
	@DeleteMapping("/food/{foodId}")
	public void deleteFood(
			@PathVariable("foodId") Long foodId
			) {
		foodservice.deleteFood(foodId);
	}
}
