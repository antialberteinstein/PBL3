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
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

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
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Người bán không hợp lệ");
        }
        return restaurantRepository.findBySeller(seller);
    }

    public Page<Branch> getByRestaurant(Restaurant restaurant, int page, int size) {
        if (restaurant == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Nhà hàng không hợp lệ");
        }

        Pageable pageable = PageRequest.of(page, size);
        return branchRepository.findByRestaurant(restaurant, pageable);
    }

    public Restaurant create(User seller, String name, String description) {
        validateRestaurant(name, description, seller);

        Restaurant restaurant = new Restaurant(name, description, seller);
        return restaurantRepository.save(restaurant);
    }

    public Restaurant update(
        int id,
        String name,
        String description,
        String backgroundUrl
    ) {
        if (id <= 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "ID nhà hàng không hợp lệ");
        }

        Restaurant restaurant = restaurantRepository.findByRestaurantId(id);
        if (restaurant == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Không tìm thấy nhà hàng");
        }

        validateRestaurant(name, description, restaurant.getSeller());

        restaurant.setRestaurantName(name);
        restaurant.setDescription(description);
        if (backgroundUrl != null && !backgroundUrl.trim().isEmpty()) {
            restaurant.setBackgroundUrl(backgroundUrl);
        }
        return restaurantRepository.save(restaurant);
    }

    public Branch createBranch(
        Restaurant restaurant,
        String branchName,
        String phoneNumber,
        String startTime,
        String endTime,
        String city,
        String ward,
        String addressLine1,
        String addressLine2
    ) {
        validateBranch(restaurant, branchName, city, ward);

        if (branchRepository.existsByRestaurantAndBranchName(restaurant, branchName)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Chi nhánh với tên này đã tồn tại trong nhà hàng");
        }

        Address address = new Address(addressLine1, addressLine2, ward, city);
        address = addressRepository.save(address);

        Branch branch = new Branch(restaurant, address, branchName, phoneNumber, startTime, endTime);
        branch = branchRepository.save(branch);

        return branch;
    }

    public Branch updateBranch(
        Restaurant restaurant,
        int branchId,
        String branchName,
        String phoneNumber,
        String startTime,
        String endTime,
        String city,
        String ward,
        String addressLine1,
        String addressLine2
    ) {
        validateBranch(restaurant, branchName, city, ward);

        Branch branch = branchRepository.findByBranchId(branchId);
        if (branch == null || branch.getRestaurant().getRestaurantId() != restaurant.getRestaurantId()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Chi nhánh không tồn tại");
        }

        Address address = new Address(addressLine1, addressLine2, ward, city);
        address = addressRepository.save(address);

        branch.setAddress(address);
        branch.setBranchName(branchName);
        branch.setPhoneNumber(phoneNumber);
        branch.setStartTime(startTime);
        branch.setEndTime(endTime);
        return branchRepository.save(branch);
    }

    public void deleteBranch(Restaurant restaurant, int branchId) {
        Branch branch = branchRepository.findByBranchId(branchId);
        if (branch == null || branch.getRestaurant().getRestaurantId() != restaurant.getRestaurantId()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Chi nhánh không tồn tại");
        }
        Address address = branch.getAddress();
        if (address != null) {
            addressRepository.delete(address);
        }
        branchRepository.delete(branch);
    }

    public Restaurant readById(int id) {
        Restaurant restaurant = restaurantRepository.findByRestaurantId(id);
        if (restaurant == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Không tìm thấy nhà hàng");
        }
        return restaurant;
    }

    public Branch readBranchById(int id) {
        Branch branch = branchRepository.findByBranchId(id);
        if (branch == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Không tìm thấy chi nhánh");
        }
        return branch;
    }

    // Validation methods
    private void validateRestaurant(String name, String description, User seller) {
        if (seller == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Người bán không hợp lệ");
        }
        if (name == null || name.trim().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Tên nhà hàng không được để trống");
        }
        if (name.length() < 2 || name.length() > 100) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Tên nhà hàng phải từ 2 đến 100 ký tự");
        }
        if (description != null && description.length() > 500) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Mô tả nhà hàng không được vượt quá 500 ký tự");
        }
    }

    private void validateBranch(Restaurant restaurant, String branchName, String city, String ward) {
        if (restaurant == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Nhà hàng không hợp lệ");
        }
        if (branchName == null || branchName.trim().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Tên chi nhánh không được để trống");
        }
        if (branchName.length() < 2 || branchName.length() > 100) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Tên chi nhánh phải từ 2 đến 100 ký tự");
        }
        if (city == null || city.trim().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Thành phố không được để trống");
        }
        if (ward == null || ward.trim().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Phường/Xã không được để trống");
        }
    }
}