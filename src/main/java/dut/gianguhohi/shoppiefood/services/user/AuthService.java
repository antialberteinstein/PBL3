package dut.gianguhohi.shoppiefood.services.user;

import dut.gianguhohi.shoppiefood.models.Users.User;
import dut.gianguhohi.shoppiefood.repositories.Users.UserRepository;
import dut.gianguhohi.shoppiefood.utils.AppServiceException;
import dut.gianguhohi.shoppiefood.utils.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    // ==========================
    // == Authentication Pane  ==
    // ==========================
    public User login(String loginString, String password) {
        Validator.validateString(loginString, "Số diện thoại/Email không được để trống");
        Validator.validateString(password, "Mật khẩu không được để trống");

        User user = userRepository.findByPhoneNumber(loginString);
        if (user == null) {
            user = userRepository.findByEmail(loginString);
        }
        if (user == null || !user.getPassword().equals(password)) {
            throw new AppServiceException("Sai số diện thoại, email hoặc mật khẩu");
        }
        if (!user.getIsActive()) {
            throw new AppServiceException("Tài khoản của quý khách đã bị ngừng hoạt động");
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
        // You may want to check oldPassword matches user.getPassword() here
        user.setPassword(newPassword);
        return userRepository.save(user);
    }
}