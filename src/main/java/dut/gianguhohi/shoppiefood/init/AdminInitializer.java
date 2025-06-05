package dut.gianguhohi.shoppiefood.init;
import dut.gianguhohi.shoppiefood.models.Users.Admin;
import dut.gianguhohi.shoppiefood.repositories.Users.AdminRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;


@Component
public class AdminInitializer implements CommandLineRunner {

    private final AdminRepository adminRepository;

    public AdminInitializer(AdminRepository adminRepository) {
        this.adminRepository = adminRepository;
    }

    @Override
    public void run(String... args) {
        String defaultAdminLogin = "admin";

        if (adminRepository.findByLoginName(defaultAdminLogin) == null) {
            System.out.println("Creating default admin user...");
            Admin admin = new Admin();
            admin.setLoginName(defaultAdminLogin);
            admin.setPassword("admin"); // ⚠️ Use env vars or config later

            adminRepository.save(admin);
            System.out.println("Default admin user created.");
        } else {
            System.out.println("Admin user already exists.");
        }
    }
}
