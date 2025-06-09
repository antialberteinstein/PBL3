package dut.gianguhohi.shoppiefood.repositories.misc;

import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;
import dut.gianguhohi.shoppiefood.models.misc.Address;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

@Repository
public interface AddressRepository extends JpaRepository<Address, Integer> {
    Address findByAddressId(int addressId);
    boolean existsByAddressId(int addressId);
    Address findByAddressIdAndUser_UserId(int addressId, int userId);
    boolean existsByAddressIdAndUserId(int addressId, int userId);
    
    boolean existsByAddressIdAndUser_UserId(int addressId, int userId);
    
    @Query("SELECT COUNT(a) > 0 FROM Address a WHERE a.addressId = :addressId AND a.user.userId = :userId")
    boolean checkAddressBelongsToUser(@Param("addressId") int addressId, @Param("userId") int userId);
}
