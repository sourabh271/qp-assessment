package com.groceryItems.GroceryOrder.config.dto;

import com.groceryItems.GroceryOrder.entity.Grocery;

import java.util.List;

public class MultipleGroceryDto {

    private String name;

    private double price;

    public MultipleGroceryDto(){

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public MultipleGroceryDto(String name, double price) {
        this.name = name;
        this.price = price;
    }
}
