package dut.gianguhohi.shoppiefood.services.restaurant;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import dut.gianguhohi.shoppiefood.repositories.misc.BranchRepository;
import dut.gianguhohi.shoppiefood.repositories.misc.AddressRepository;
import dut.gianguhohi.shoppiefood.models.Users.Restaurant;
import dut.gianguhohi.shoppiefood.models.misc.Branch;
import dut.gianguhohi.shoppiefood.models.misc.Address;

@Service
public class BranchService {

    @Autowired
    private BranchRepository branchRepository;

    @Autowired
    private AddressRepository addressRepository;

    // Get branches by restaurant
    public Page<Branch> getByRestaurant(Restaurant restaurant, int page, int size) {
        if (restaurant == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Nhà hàng không hợp lệ");
        }
        Pageable pageable = PageRequest.of(page, size);
        return branchRepository.findByRestaurant(restaurant, pageable);
    }

    // Create new branch
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
        return branchRepository.save(branch);
    }

    // Update branch
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

        Branch existingBranch = branchRepository.findByRestaurantAndBranchName(restaurant, branchName);
        if (existingBranch != null && existingBranch.getBranchId() != branchId) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Chi nhánh với tên này đã tồn tại trong nhà hàng");
        }

        Branch branch = branchRepository.findByBranchId(branchId);
        if (branch == null || branch.getRestaurant().getRestaurantId() != restaurant.getRestaurantId()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Chi nhánh không tồn tại");
        }

        Address address = branch.getAddress();
        if (address == null) {
            address = new Address(addressLine1, addressLine2, ward, city);
        } else {
            address.setAddressLine1(addressLine1);
            address.setAddressLine2(addressLine2);
            address.setWard(ward);
            address.setCity(city);
        }
        address = addressRepository.save(address);

        branch.setAddress(address);
        branch.setBranchName(branchName);
        branch.setPhoneNumber(phoneNumber);
        branch.setStartTime(startTime);
        branch.setEndTime(endTime);
        return branchRepository.save(branch);
    }

    // Delete branch
    public void deleteBranch(int branchId) {
        Branch branch = branchRepository.findByBranchId(branchId);

        if (branch == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Chi nhánh không tồn tại");
        }

        Address address = branch.getAddress();
        branchRepository.delete(branch);
        if (address != null) {
            Address existingAddress = addressRepository.findByAddressId(address.getAddressId());
            if (existingAddress != null) {
                addressRepository.delete(existingAddress);
            }
        }
    }

    // Get branch by id
    public Branch readBranchById(int id) {
        Branch branch = branchRepository.findByBranchId(id);
        if (branch == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Không tìm thấy chi nhánh");
        }
        return branch;
    }

    // Validation
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