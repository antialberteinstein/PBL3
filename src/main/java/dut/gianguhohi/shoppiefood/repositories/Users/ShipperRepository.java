package dut.gianguhohi.shoppiefood.repositories.Users;

import dut.gianguhohi.shoppiefood.models.Users.Shipper;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.stereotype.Repository;
import dut.gianguhohi.shoppiefood.models.Users.User;


@Repository
public interface ShipperRepository extends JpaRepository<Shipper, Integer> {
    Shipper findByUser(User user);

    Shipper findByShipperId(int shipperId);

    boolean existsByUser(User user);
}
