package dut.gianguhohi.shoppiefood.services.user;

import dut.gianguhohi.shoppiefood.models.Users.User;
import dut.gianguhohi.shoppiefood.repositories.Users.UserRepository;
import dut.gianguhohi.shoppiefood.utils.AppServiceException;
import dut.gianguhohi.shoppiefood.utils.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;

    // ==========================
    // == Authentication Pane  ==
    // ==========================
    public User login(String loginString, String password) {
        Validator.validateString(loginString, "Số diện thoại/Email không được để trống");
        Validator.validateString(password, "Mật khẩu không được để trống");

        // Tìm user theo số điện thoại hoặc email
        User user = null;
        if (validateEmail(loginString)) {
            user = userRepository.findByEmail(loginString);
        } else {
            user = userRepository.findByPhoneNumber(loginString);
        }

        // Kiểm tra user có tồn tại không
        if (user == null) {
            throw new AppServiceException("Tài khoản không tồn tại");
        }
        
        // Kiểm tra mật khẩu bằng passwordEncoder
        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new AppServiceException("Mật khẩu không đúng");
        }
        
        return user;
    }

    public User changePassword(int id, String oldPassword, String newPassword) {
        Validator.validateId(id, "ID không hợp lệ");
        Validator.validatePassword(
            newPassword, 
            "Mật khẩu mới không được để trống",
            "Mật khẩu mới không hợp lệ");

        User user = userRepository.findByUserId(id);
        if (user == null) {
            throw new AppServiceException("Người dùng không tồn tại");
        }
        
        // Kiểm tra mật khẩu cũ
        if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
            throw new AppServiceException("Mật khẩu cũ không đúng");
        }
        
        // Kiểm tra mật khẩu mới khác mật khẩu cũ
        if (passwordEncoder.matches(newPassword, user.getPassword())) {
            throw new AppServiceException("Mật khẩu mới không được trùng với mật khẩu cũ");
        }
        
        // Mã hóa và lưu mật khẩu mới
        user.setPassword(passwordEncoder.encode(newPassword));
        return userRepository.save(user);
    }

    public static boolean validateEmail(String email) {
        return email != null && email.matches("^[A-Za-z0-9+_.-]+@(.+)$");
    }
}