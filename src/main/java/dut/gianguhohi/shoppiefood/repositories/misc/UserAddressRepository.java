package dut.gianguhohi.shoppiefood.repositories.misc;

import dut.gianguhohi.shoppiefood.models.misc.UserAddress;
import dut.gianguhohi.shoppiefood.models.Users.User;
import dut.gianguhohi.shoppiefood.models.misc.Address;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;


import java.util.List;

@Repository
public interface UserAddressRepository extends JpaRepository<UserAddress, Integer> {
    UserAddress findByUserAndAddress(User user, Address address);

    UserAddress findByUserAddressId(int userAddressId);

    List<UserAddress> findByUser(User user);
}