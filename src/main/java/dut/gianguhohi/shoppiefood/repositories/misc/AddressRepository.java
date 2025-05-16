package dut.gianguhohi.shoppiefood.repositories.misc;

import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;
import dut.gianguhohi.shoppiefood.models.misc.Address;

@Repository
public interface AddressRepository extends JpaRepository<Address, Integer> {
    Address findByAddressId(int addressId);
    boolean existsByAddressId(int addressId);
}
