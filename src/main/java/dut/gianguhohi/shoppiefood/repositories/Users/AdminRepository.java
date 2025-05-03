package dut.gianguhohi.shoppiefood.repositories.Users;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import dut.gianguhohi.shoppiefood.models.Users.Admin;

@Repository
public interface AdminRepository extends JpaRepository<Admin, Integer> {
    Admin findByLoginName(String loginName);
    boolean existsByLoginName(String loginName);
}
