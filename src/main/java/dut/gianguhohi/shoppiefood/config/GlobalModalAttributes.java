package dut.gianguhohi.shoppiefood.config;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.ui.Model;
import org.springframework.beans.factory.annotation.Autowired;

import dut.gianguhohi.shoppiefood.models.Users.User;
import dut.gianguhohi.shoppiefood.services.user.UserService;
import jakarta.servlet.http.HttpSession;
import dut.gianguhohi.shoppiefood.models.Users.User;
import dut.gianguhohi.shoppiefood.services.restaurant.RestaurantService;

import dut.gianguhohi.shoppiefood.models.Users.Restaurant;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import dut.gianguhohi.shoppiefood.services.ShipperService;
import dut.gianguhohi.shoppiefood.models.Users.Shipper;



@ControllerAdvice
public class GlobalModalAttributes {

    @Autowired
    private UserService userService;

    @Autowired
    private RestaurantService restaurantService;

    @Autowired
    private HttpSession session;

    @Autowired
    private ShipperService shipperService;


    @ModelAttribute
    public void addGlobalAttributes(Model model) {
        Object userIdObj = session.getAttribute("userId");

        if (userIdObj == null) {
            return; // No user is logged in
        }

        Integer userId = (Integer) userIdObj;

        User user = userService.readById(userId);
        if (user != null) {
            model.addAttribute("userAvatarUrl", user.getAvatarUrl());
            model.addAttribute("userPhoneNumber", user.getPhoneNumber());
            
            Object restaurantIdObj = session.getAttribute("restaurantId");
            if (restaurantIdObj != null) {
                Integer restaurantId = (Integer) restaurantIdObj;
                Restaurant restaurant = restaurantService.readById(restaurantId);
                if (restaurant != null) {
                    model.addAttribute("restaurantId", restaurant.getRestaurantId());
                    model.addAttribute("restaurantName", restaurant.getRestaurantName());
                    model.addAttribute("restaurantDescription", restaurant.getDescription());
                    model.addAttribute("restaurantBackgroundUrl", restaurant.getBackgroundUrl());
                }
            }

            Object shipperIdObj = session.getAttribute("shipperId");
            if (shipperIdObj != null) {
                Integer shipperId = (Integer) shipperIdObj;
                Shipper shipper = shipperService.getShipperById(shipperId);
                if (shipper != null) {
                    model.addAttribute("shipperId", shipper.getShipperId());
                    model.addAttribute("shipperVehicleType", shipper.getVehicleType());
                    model.addAttribute("shipperPlateNumber", shipper.getPlateNumber());
                    model.addAttribute("shipperDriverLicense", shipper.getDriverLicense());
                }
            }


            Object roleObj = session.getAttribute("role");

            if (roleObj != null) {
                String role = (String) roleObj;
                if (role.equals("user")) {
                    List<Restaurant> restaurants = restaurantService.getBySeller(user);
                    if (restaurants != null && !restaurants.isEmpty()) {
                        Map<Integer, String> restaurantMap = restaurants.stream()
                                .collect(Collectors.toMap(Restaurant::getRestaurantId, Restaurant::getRestaurantName));

                        model.addAttribute("restaurantMap", restaurantMap);
                    }
                } else if (role.equals("shipper")) {
                    // Do later.
                } else if (role.equals("restaurant")) {
                    // Do later.
                }
            }
        }
    }
}
