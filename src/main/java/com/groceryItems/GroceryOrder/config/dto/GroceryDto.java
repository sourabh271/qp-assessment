package com.groceryItems.GroceryOrder.config.dto;

import java.io.Serializable;
import java.util.List;

public class GroceryDto implements Serializable {

    List<String> groceryList;

    public GroceryDto(){

    }

    public GroceryDto(List<String> groceryList) {
        this.groceryList = groceryList;
    }

    public List<String> getGroceryList() {
        return groceryList;
    }

    public void setGroceryList(List<String> groceryList) {
        this.groceryList = groceryList;
    }
}
