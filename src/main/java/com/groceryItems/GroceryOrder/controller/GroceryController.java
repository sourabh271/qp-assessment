package com.groceryItems.GroceryOrder.controller;

import com.groceryItems.GroceryOrder.config.dto.MultipleGroceryDto;
import com.groceryItems.GroceryOrder.entity.Grocery;
import com.groceryItems.GroceryOrder.entity.UserInfo;
import com.groceryItems.GroceryOrder.repository.GroceryRepository;
import com.groceryItems.GroceryOrder.repository.UserInfoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/groceries")
public class GroceryController {

    @Autowired
    private GroceryRepository groceryRepository;

    @Autowired
    private UserInfoRepository userInfoRepository;

    @GetMapping
    public ResponseEntity<List<Grocery>> getAllGroceries(){
        return new ResponseEntity<>(groceryRepository.findAll(), HttpStatus.OK);
    }

    @PostMapping
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Grocery> addGrocery(@RequestBody Grocery grocery){
        return new ResponseEntity<>(groceryRepository.save(grocery),HttpStatus.CREATED);
    }


    @DeleteMapping
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<HttpStatus> deleteGrocery(@RequestParam Long id){
        Optional<Grocery> grocery = groceryRepository.findById(id);
        if(grocery.isPresent()){
            Grocery groceryFromDB = grocery.get();
            for(UserInfo userInfo:groceryFromDB.getUserInfo()){
                userInfo.getGroceryList().remove(groceryFromDB);
                userInfoRepository.save(userInfo);
                groceryFromDB.getUserInfo().remove(userInfo);
            }
            groceryRepository.delete(groceryFromDB);

        }

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PatchMapping
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Grocery> updateGrocery(@RequestBody Grocery grocery){
        Optional<Grocery> groceryFromDB = groceryRepository.findById(grocery.getId());
        Grocery result = null;
        if(groceryFromDB.isPresent()){
            Grocery groceryDB = groceryFromDB.get();
            boolean modified = false;
            if(groceryDB.getName()!= grocery.getName()){
                groceryDB.setName(grocery.getName());
                modified = true;
            }
            if(groceryDB.getPrice() != grocery.getPrice() ){
                groceryDB.setPrice(grocery.getPrice());
                modified = true;
            }
            if(modified){
               result = groceryRepository.save(groceryDB);
            }
        }
        return new ResponseEntity<>(result,HttpStatus.OK);
    }




}
