package com.sample.spring.service;

import java.time.ZonedDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sample.spring.api.request.CreateAndEditFoodRequest;
import com.sample.spring.api.response.FoodDetailView;
import com.sample.spring.api.response.FoodView;
import com.sample.spring.model.FoodEntity;
import com.sample.spring.model.MenuEntity;
import com.sample.spring.repository.FoodRepository;
import com.sample.spring.repository.MenuRepository;

import jakarta.transaction.Transactional;

@Service
public class FoodService {
	@Autowired
	private FoodRepository foodrepository;
	
	@Autowired
	private MenuRepository menurepository;
	
	@Transactional
	public FoodEntity createFood(
			CreateAndEditFoodRequest request
			) {
		FoodEntity food = FoodEntity.builder()
				.name(request.getName())
				.address(request.getAddress())
				.createAt(ZonedDateTime.now())
				.updateAt(ZonedDateTime.now())
				.build();
		
		foodrepository.save(food);
		
		request.getMenus().forEach((menu)->{
			MenuEntity menuEntity = MenuEntity.builder()
					.foodId(food.getId())
					.name(menu.getName())
					.price(menu.getPrice())
					.createAt(ZonedDateTime.now())
					.updateAt(ZonedDateTime.now())
					.build();
			
			menurepository.save(menuEntity);
		});
		
		return food;
	}
	
	@Transactional
	public void editFood(
			Long foodId,
			CreateAndEditFoodRequest request
			) {
		FoodEntity food = foodrepository.findById(foodId).orElseThrow(()->new RuntimeException("no food"));
		food.changeNameAndAddress(request.getName(), request.getAddress());
		foodrepository.save(food);
		
		List<MenuEntity> menus = menurepository.findAllByFoodId(foodId);
		menurepository.deleteAll(menus);
		
		request.getMenus().forEach((menu)->{
			MenuEntity menuEntity = MenuEntity.builder()
					.foodId(food.getId())
					.name(menu.getName())
					.price(menu.getPrice())
					.createAt(ZonedDateTime.now())
					.updateAt(ZonedDateTime.now())
					.build();
			
			menurepository.save(menuEntity);
		});
	}
	
	public void deleteFood(Long foodId) {
		FoodEntity food = foodrepository.findById(foodId).orElseThrow();
		foodrepository.delete(food);
		
		List<MenuEntity> menus = menurepository.findAllByFoodId(foodId);
		menurepository.deleteAll(menus);
	}
	
	public List<FoodView> getAllFoods(){
		List<FoodEntity> foods = foodrepository.findAll();
		return foods.stream().map((food)->
			FoodView.builder()
			.id(food.getId())
			.name(food.getName())
			.address(food.getAddress())
			.createdAt(food.getCreateAt())
			.updatedAt(food.getUpdateAt())
			.build()
		).toList();
	}
	
	public FoodDetailView getFoodDetail(Long foodId) {
		FoodEntity food = foodrepository.findById(foodId).orElseThrow();
		
		List<MenuEntity> menus = menurepository.findAllByFoodId(foodId);
		return FoodDetailView.builder()
				.id(0L)
				.name(food.getName())
				.address(food.getAddress())
				.createdAt(food.getCreateAt())
				.updatedAt(food.getUpdateAt())
				.menus(menus.stream().map((menu)->
					FoodDetailView.Menu.builder()
					.id(menu.getFoodId())
					.name(menu.getName())
					.price(menu.getPrice())
					.createdAt(menu.getCreateAt())
					.updatedAt(menu.getUpdateAt())
					.build()
					).toList())
				.build();
	}
}
