package com.delivery_api.Delivery.API.service;



import com.delivery_api.Delivery.API.entity.Restaurant;
import com.delivery_api.Delivery.API.repository.RestaurantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class RestaurantService {
    
    @Autowired
    private RestaurantRepository restaurantRepository;
    
    /*
        Register a new Restaurant
     */

    public Restaurant register(Restaurant restaurant) {

        validateRestaurantData(restaurant);

        restaurant.setActive(true);

        return restaurantRepository.save(restaurant);
    }

    @Transactional(readOnly = true)
    public List<Restaurant> listActives() {
        return restaurantRepository.findByActiveTrue();
    }

    @Transactional(readOnly = true)
    public Optional<Restaurant> searchById(Long id) {
        return restaurantRepository.findById(id);
    }

    public Restaurant update(Long id, Restaurant restaurantUpdated) {
        Restaurant restaurant = searchById(id).orElseThrow(() -> new IllegalArgumentException("restaurant not found: " + id));

        if (restaurant.getActive() != null && !restaurant.getActive()) {
            throw new IllegalArgumentException("Cannot update inactive restaurant");
        }

        validateRestaurantData(restaurantUpdated);

        if (!restaurant.getPhone().equals(restaurantUpdated.getPhone())) {
            if (restaurantRepository.existsByPhone(restaurantUpdated.getPhone())) {
                throw new IllegalArgumentException("Phone already exists: " +  restaurantUpdated.getPhone());
            }
        }

        restaurant.setName(restaurantUpdated.getName());
        restaurant.setCategory(restaurantUpdated.getCategory());
        restaurant.setPhone(restaurantUpdated.getPhone());
        restaurant.setAddress(restaurantUpdated.getAddress());
        restaurant.setDeliveryFee(restaurantUpdated.getDeliveryFee());
        restaurant.setRating(restaurantUpdated.getRating());

        return restaurantRepository.save(restaurant);
    }

    public void deactivateRestaurant(Long id) {
        Restaurant restaurant = searchById(id).orElseThrow(() -> new IllegalArgumentException("Restaurant not found: " + id));

        restaurant.deactivate();
        restaurantRepository.save(restaurant);
    }

    @Transactional(readOnly = true)
    public List<Restaurant> searchByName(String name) {
        return restaurantRepository.findByNameContainingIgnoreCaseAndActiveTrue(name);
    }

    @Transactional(readOnly = true)
    public List<Restaurant> searchByCategory(String category) {
        return restaurantRepository.findByCategory(category);
    }

    @Transactional(readOnly = true)
    public List<Restaurant> searchByRating(Double min) {
        return restaurantRepository.findByRatingBetween(min, 5.0);
    }

    private void validateRestaurantData(Restaurant restaurant) {
        if (restaurant.getName() == null || restaurant.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("Name is empty");
        }

        if (restaurant.getPhone() == null || restaurant.getPhone().trim().isEmpty()) {
            throw new IllegalArgumentException("Phone is empty");
        }

        if (restaurant.getName().length() < 2) {
            throw new IllegalArgumentException("Name should have at least 2 characters");
        }
    }
}
