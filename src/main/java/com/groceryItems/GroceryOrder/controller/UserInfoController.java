package com.groceryItems.GroceryOrder.controller;

import com.groceryItems.GroceryOrder.config.dto.GroceryDto;
import com.groceryItems.GroceryOrder.config.dto.MultipleGroceryDto;
import com.groceryItems.GroceryOrder.entity.Grocery;
import com.groceryItems.GroceryOrder.entity.UserInfo;
import com.groceryItems.GroceryOrder.repository.GroceryRepository;
import com.groceryItems.GroceryOrder.repository.UserInfoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/users")
public class UserInfoController {

    @Autowired
    private UserInfoRepository userInfoRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private GroceryRepository groceryRepository;

    @PostMapping("/register")
    public ResponseEntity<UserInfo> createUser(@RequestBody UserInfo userInfo){
        userInfo.setPassword(passwordEncoder.encode(userInfo.getPassword()));
        return new ResponseEntity<>(userInfoRepository.save(userInfo), HttpStatus.CREATED);
    }

    @GetMapping("/home")
    public String getResponse(){
        return "Welcome To Grocery Shopping";
    }

    @GetMapping
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<List<UserInfo>> getAllUsers(){
        return new ResponseEntity<>(userInfoRepository.findAll(),HttpStatus.OK);
    }

    @PostMapping("/order")
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<List<MultipleGroceryDto>> orderGroceries(@RequestParam("userId") Integer userId, @RequestBody GroceryDto dto){

        List<MultipleGroceryDto> groceries = new ArrayList<>();
        for(String groceryName:dto.getGroceryList()){
            Optional<Grocery> grocery = groceryRepository.findByName(groceryName);
            if(grocery.isPresent()){
                Grocery grocery1 = grocery.get();
                Set<UserInfo> userInfos = new HashSet<>();
                UserInfo userInfo = userInfoRepository.findById(userId.longValue()).orElse(null);
                if(userInfo != null){
                    userInfo.getGroceryList().add(grocery1);
                    userInfoRepository.save(userInfo);
                }
                userInfos.add(userInfo);
                grocery1.getUserInfo().add(userInfo);
                groceries.add(new MultipleGroceryDto(grocery1.getName(),grocery1.getPrice()));
                groceryRepository.save(grocery1);

            }
        }
        return new ResponseEntity<>(groceries,HttpStatus.OK);
    }

    @GetMapping("/groceries")
    public List<MultipleGroceryDto> getAllGroceriesForUser(@RequestParam("userId") String userId){
        Optional<UserInfo> userInfo = userInfoRepository.findById(Long.parseLong(userId));
        List<MultipleGroceryDto> groceries = new ArrayList<>();
        if(userInfo.isPresent()){
            Set<Grocery> groceryList = userInfo.get().getGroceryList();
            for(Grocery grocery: groceryList){
                groceries.add(new MultipleGroceryDto(grocery.getName(),grocery.getPrice()));
            }
        }
        return groceries;
    }



}
