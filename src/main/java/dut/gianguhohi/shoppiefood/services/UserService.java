package dut.gianguhohi.shoppiefood.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import dut.gianguhohi.shoppiefood.models.Users.User;
import dut.gianguhohi.shoppiefood.repositories.Users.UserRepository;
import jakarta.transaction.Transactional;
import dut.gianguhohi.shoppiefood.utils.AppServiceException;
import dut.gianguhohi.shoppiefood.utils.Validator;

@Transactional
@Service
public class UserService {
    
    @Autowired
    private UserRepository userRepository;

    public User readByPhoneNumber(String phoneNumber) {
        User user = userRepository.findByPhoneNumber(phoneNumber);
        return user;
    }

    public User readByEmail(String email) {
        User user = userRepository.findByEmail(email);
        return user;
    }

    public User readById(int id) {
        User user = userRepository.findByUserId(id);
        return user;
    }

    public User login(String loginString, String password) {
        Validator.validateString(loginString, "Số diện thoại/Email không được để trống");
        Validator.validateString(password, "Mật khẩu không được để trống");

        User user = readByPhoneNumber(loginString);
        if (user == null) {
            user = readByEmail(loginString);
        }
        if (user == null || !user.getPassword().equals(password)) {
            throw new AppServiceException("Sai số diện thoại, email hoặc mật khẩu");
        }
        if (!user.getIsActive()) {
            throw new AppServiceException("Tài khoản của quý khách đã bị ngừng hoạt động");
        }

        return user;
    }

    public User register(
        String phoneNumber,
        String email,
        String password,
        String confirmPassword,
        String name,
        String dateOfBirth,
        String gender
    ) {
        validateRegister(phoneNumber, email, password, confirmPassword, name, dateOfBirth, gender);

        // Check if user exists
        if (userRepository.existsByPhoneNumber(phoneNumber)) {
            throw new AppServiceException("Số điện thoại đã có người sử dụng");
        }

        if (userRepository.existsByEmail(email)) {
            throw new AppServiceException("Email đã có người sử dụng");
        }

        // Create new user
        User user = new User(name, password, phoneNumber, email, gender, dateOfBirth);

        return userRepository.save(user);
    }

    public User update(
        int id,
        String newPhoneNumber,
        String newEmail,
        String newName,
        String newDateOfBirth,
        String newGender
    ) {
        

        User existingUser = readById(id);
        if (existingUser == null) {
            throw new AppServiceException("Người dùng không tồn tại");
        }

        validateUpdate(newPhoneNumber, newEmail, newName, newDateOfBirth, newGender);

        existingUser.setPhoneNumber(newPhoneNumber);
        existingUser.setEmail(newEmail);
        existingUser.setName(newName);
        existingUser.setDateOfBirth(newDateOfBirth);
        existingUser.setGender(newGender);
        

        return userRepository.save(existingUser);
    }

    public User changePassword(int id, String oldPassword, String newPassword) {
        Validator.validateId(id, "ID không hợp lệ");
        Validator.validatePassword(
            newPassword, 
            "Mật khẩu mới không được để trống",
            "Mật khẩu mới không hợp lệ");

        User user = readById(id);
        if (user == null) {
            throw new AppServiceException("Người dùng không tồn tại");
        }
        validateConfirmPassword(oldPassword, newPassword);
        user.setPassword(newPassword);
        return userRepository.save(user);
    }

    public void delete(int id) {
        Validator.validateId(id, "ID không hợp lệ");
        User user = readById(id);
        if (user == null) {
            throw new AppServiceException("Người dùng không tồn tại");
        }
        userRepository.delete(user);
    }

    public void disable(int id) {
        Validator.validateId(id, "ID không hợp lệ");
        User user = readById(id);
        if (user == null) {
            throw new AppServiceException("Người dùng không tồn tại");
        }
        user.setIsActive(false);
        userRepository.save(user);
    }

    public List<User> getAllUsers() {
        List<User> users = userRepository.findAll();
        return users;
    }

    private void validateConfirmPassword(String password, String confirmPassword) {
        if (confirmPassword == null || confirmPassword.trim().isEmpty()) {
            throw new AppServiceException("Mật khẩu xác nhận không được để trống");
        }
        if (!confirmPassword.equals(password)) {
            throw new AppServiceException("Mật khẩu xác nhận không khớp");
        }
    }


    /* Validation phase */
    private void validateRegister(
        String phoneNumber,
        String email,
        String password,
        String confirmPassword,
        String name,
        String dateOfBirth,
        String gender
    ) {
        // Validate all fields
        Validator.validatePhoneNumber(phoneNumber);
        Validator.validateEmail(email);
        Validator.validatePassword(password);
        Validator.validateName(name);
        Validator.validateString(dateOfBirth, "Ngày sinh không được để trống");
        Validator.validateString(gender, "Bạn chưa chọn giới tính");

        validateConfirmPassword(password, confirmPassword);
    }

    private void validateUpdate(
        String phoneNumber,
        String email,
        String name,
        String dateOfBirth,
        String gender
    ) {
        // Validate all fields
        Validator.validatePhoneNumber(phoneNumber);
        Validator.validateEmail(email);
        Validator.validateName(name);
        Validator.validateString(dateOfBirth, "Ngày sinh không được để trống");
        Validator.validateString(gender, "Bạn chưa chọn giới tính");
    }
}
