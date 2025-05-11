package dut.gianguhohi.shoppiefood.services.Users;

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

    /**
     * Authenticates a user based on their phone number or email and password.
     *
     * @param loginString The user's phone number or email.
     * @param password The user's password.
     * @return The authenticated User object.
     * @throws LoginRelatedException if the phone number, email, or password is incorrect.
     */
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

    /**
     * Registers a new user in the system.
     *
     * @param user The User object containing user details to be registered.
     * @param result The BindingResult object that holds the result of the validation and binding.
     * @return The registered User object.
     * @throws RegisterRelatedException if there are validation errors or if the phone number or email already exists.
     */
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

    // Temporary delete method for testing purposes
    // In a real application, you would want to implement a proper delete method that handles related data and constraints.
    // Maybe need a deactivate method instead of delete
    // to keep the data for future reference.
    public void delete(int id) {
        User user = readById(id);
        if (user == null) {
            throw new UpdateRelatedException("Người dùng không tồn tại");
        }
        userRepository.delete(user);
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
