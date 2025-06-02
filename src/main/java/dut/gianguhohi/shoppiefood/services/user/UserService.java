package dut.gianguhohi.shoppiefood.services.user;

import dut.gianguhohi.shoppiefood.models.Users.User;
import dut.gianguhohi.shoppiefood.repositories.Users.UserRepository;
import dut.gianguhohi.shoppiefood.utils.AppServiceException;
import dut.gianguhohi.shoppiefood.utils.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;

import java.util.List;

@Transactional
@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    // ==========================
    // == User Queries         ==
    // ==========================
    public User readByPhoneNumber(String phoneNumber) {
        return userRepository.findByPhoneNumber(phoneNumber);
    }

    public User readByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public User readById(int id) {
        return userRepository.findByUserId(id);
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    // ==========================
    // == User CRUD/Auth       ==
    // ==========================
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

        if (userRepository.existsByPhoneNumber(phoneNumber)) {
            throw new AppServiceException("Số điện thoại đã có người sử dụng");
        }
        if (userRepository.existsByEmail(email)) {
            throw new AppServiceException("Email đã có người sử dụng");
        }

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

    // ==========================
    // == Validation Methods   ==
    // ==========================
    private void validateRegister(
        String phoneNumber,
        String email,
        String password,
        String confirmPassword,
        String name,
        String dateOfBirth,
        String gender
    ) {
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
        Validator.validatePhoneNumber(phoneNumber);
        Validator.validateEmail(email);
        Validator.validateName(name);
        Validator.validateString(dateOfBirth, "Ngày sinh không được để trống");
        Validator.validateString(gender, "Bạn chưa chọn giới tính");
    }

    private void validateConfirmPassword(String password, String confirmPassword) {
        if (confirmPassword == null || confirmPassword.trim().isEmpty()) {
            throw new AppServiceException("Mật khẩu xác nhận không được để trống");
        }
        if (!confirmPassword.equals(password)) {
            throw new AppServiceException("Mật khẩu xác nhận không khớp");
        }
    }
}