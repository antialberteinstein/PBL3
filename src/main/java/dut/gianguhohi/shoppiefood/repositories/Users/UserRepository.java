package dut.gianguhohi.shoppiefood.repositories.Users;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import dut.gianguhohi.shoppiefood.models.Users.User;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {  
    User findByPhoneNumber(String phoneNumber);
    boolean existsByPhoneNumber(String phoneNumber);
    boolean existsByEmail(String email);

    User findByEmail(String email);
}
