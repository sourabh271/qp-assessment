package com.groceryItems.GroceryOrder.repository;

import com.groceryItems.GroceryOrder.entity.Grocery;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface GroceryRepository extends JpaRepository<Grocery,Long> {

    Optional<Grocery> findByName(String name);

    @Query(value = "SELECT * FROM grocery where user_id = ?1",nativeQuery = true)
    List<Grocery> findGroceriesByUserId(String userId);

}
