package dut.gianguhohi.shoppiefood.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import dut.gianguhohi.shoppiefood.models.Users.User;
import dut.gianguhohi.shoppiefood.repositories.Users.UserRepository;
import jakarta.validation.Valid;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

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
        User user = readByPhoneNumber(loginString);
        if (user == null) {
            user = readByEmail(loginString);
        }
        if (user == null || !user.getPassword().equals(password)) {
            throw new LoginRelatedException("Sai số diện thoại, email hoặc mật khẩu");
        }
        return user;
    }

    public User register(@Valid User user, BindingResult result) {
        if (result.hasErrors()) {
            for (FieldError error : result.getFieldErrors()) {
                throw new RegisterRelatedException(error.getDefaultMessage());
            }
        }

        if (userRepository.existsByPhoneNumber(user.getPhoneNumber())) {
            throw new UserExistsException("Số điện thoại đã có người sử dụng");
        }

        if (userRepository.existsByEmail(user.getEmail())) {
            throw new UserExistsException("Email đã có người sử dụng");
        }

        return userRepository.save(user);
    }

    public User update(User user) {
        User existingUser = readById(user.getUserId());
        if (existingUser == null) {
            throw new UpdateRelatedException("Người dùng không tồn tại");
        }
        return userRepository.save(user);
    }

    public User changePassword(int id, String oldPassword, String newPassword) {
        User user = readById(id);
        if (user == null) {
            throw new UpdateRelatedException("Người dùng không tồn tại");
        }
        if (!user.getPassword().equals(oldPassword)) {
            throw new UpdateRelatedException("Mật khẩu cũ không đúng");
        }
        user.setPassword(newPassword);
        return userRepository.save(user);
    }

    
    public void delete(int id) {
        User user = readById(id);
        if (user == null) {
            throw new DeleteRelatedException("Người dùng không tồn tại");
        }
        userRepository.delete(user);
    }

    public void disable(int id) {
        User user = readById(id);
        if (user == null) {
            throw new DeleteRelatedException("Người dùng không tồn tại");
        }
        userRepository.save(user);
    }

    public List<User> getAllUsers() {
        List<User> users = userRepository.findAll();
        return users;
    }


    public static class UserRelatedException extends RuntimeException {
        public UserRelatedException(String message) {
            super(message);
        }
    }

    public static class UpdateRelatedException extends UserRelatedException {
        public UpdateRelatedException(String message) {
            super(message);
        }
    }

    public static class DeleteRelatedException extends UserRelatedException {
        public DeleteRelatedException(String message) {
            super(message);
        }
    }

    public static class LoginRelatedException extends UserRelatedException {
        public LoginRelatedException(String message) {
            super(message);
        }
    }

    public static class RegisterRelatedException extends UserRelatedException {
        public RegisterRelatedException(String message) {
            super(message);
        }
    }

    public static class UserExistsException extends RegisterRelatedException {
        public UserExistsException(String message) {
            super(message);
        }
    }
}
