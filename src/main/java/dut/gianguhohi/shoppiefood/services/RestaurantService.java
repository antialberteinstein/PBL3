package dut.gianguhohi.shoppiefood.services;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import dut.gianguhohi.shoppiefood.repositories.Users.RestaurantRepository;
import dut.gianguhohi.shoppiefood.models.Users.Restaurant;
import java.util.List;
import dut.gianguhohi.shoppiefood.models.Users.User;
import dut.gianguhohi.shoppiefood.models.misc.Branch;
import dut.gianguhohi.shoppiefood.models.misc.Address;
import dut.gianguhohi.shoppiefood.repositories.misc.BranchRepository;
import jakarta.transaction.Transactional;
import dut.gianguhohi.shoppiefood.repositories.misc.AddressRepository;
import dut.gianguhohi.shoppiefood.utils.AppServiceException;
import dut.gianguhohi.shoppiefood.utils.Validator;

@Transactional
@Service
public class RestaurantService {

    @Autowired
    private RestaurantRepository restaurantRepository;

    @Autowired
    private BranchRepository branchRepository;

    @Autowired
    private AddressRepository addressRepository;

    public List<Restaurant> getAllRestaurants() {
        return restaurantRepository.findAll();
    }

    public List<Restaurant> getBySeller(User seller) {
        if (seller == null) {
            throw new AppServiceException("Người bán không hợp lệ");
        }
        return restaurantRepository.findBySeller(seller);
    }

    public Restaurant create(User seller, String name, String description) {
        validateRestaurant(name, description, seller);

        Restaurant restaurant = new Restaurant(name, description, seller);
        return restaurantRepository.save(restaurant);
    }

    public Branch createBranch(
        Restaurant restaurant,
        String branchName,
        String city,
        String ward,
        String addressLine1,
        String addressLine2
    ) {
        validateBranch(restaurant, branchName, city, ward);

        Address address = new Address(addressLine1, addressLine2, ward, city);
        address = addressRepository.save(address);

        Branch branch = new Branch(restaurant, address, branchName);
        branch = branchRepository.save(branch);

        return branch;
    }

    public Restaurant readById(int id) {
        if (id <= 0) {
            throw new AppServiceException("ID nhà hàng không hợp lệ");
        }
        Restaurant restaurant = restaurantRepository.findByRestaurantId(id);
        if (restaurant == null) {
            throw new AppServiceException("Không tìm thấy nhà hàng");
        }
        return restaurant;
    }

    // Validation methods
    private void validateRestaurant(String name, String description, User seller) {
        if (seller == null) {
            throw new AppServiceException("Người bán không hợp lệ");
        }
        if (name == null || name.trim().isEmpty()) {
            throw new AppServiceException("Tên nhà hàng không được để trống");
        }
        if (name.length() < 2 || name.length() > 100) {
            throw new AppServiceException("Tên nhà hàng phải từ 2 đến 100 ký tự");
        }
        if (description != null && description.length() > 500) {
            throw new AppSerivceException("Mô tả nhà hàng không được vượt quá 500 ký tự");
        }
    }

    private void validateBranch(Restaurant restaurant, String branchName, String city, String ward) {
        if (restaurant == null) {
            throw new AppServiceException("Nhà hàng không hợp lệ");
        }
        if (branchName == null || branchName.trim().isEmpty()) {
            throw new AppServiceException("Tên chi nhánh không được để trống");
        }
        if (branchName.length() < 2 || branchName.length() > 100) {
            throw new AppServiceException("Tên chi nhánh phải từ 2 đến 100 ký tự");
        }
        if (city == null || city.trim().isEmpty()) {
            throw new AppServiceException("Thành phố không được để trống");
        }
        if (ward == null || ward.trim().isEmpty()) {
            throw new AppServiceException("Phường/Xã không được để trống");
        }
    }
}