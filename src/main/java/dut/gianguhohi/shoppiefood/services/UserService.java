package dut.gianguhohi.shoppiefood.services;

import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import dut.gianguhohi.shoppiefood.models.Users.User;
import dut.gianguhohi.shoppiefood.repositories.Users.UserRepository;
import jakarta.validation.Valid;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

@Service
@Transactional
public class UserService extends BaseService<User, Integer> {
    
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    protected UserRepository getRepository() {
        return userRepository;
    }

    public Optional<User> findByPhoneNumber(String phoneNumber) {
        return Optional.ofNullable(userRepository.findByPhoneNumber(phoneNumber));
    }

    public Optional<User> findByEmail(String email) {
        return Optional.ofNullable(userRepository.findByEmail(email));
    }

    public User login(String loginString, String password) {
        User user = findByPhoneNumber(loginString)
            .orElseGet(() -> findByEmail(loginString).orElse(null));

        if (user == null || !passwordEncoder.matches(password, user.getPassword())) {
            throw new LoginRelatedException("Invalid phone number, email or password");
        }
        return user;
    }

    public User register(@Valid User user, BindingResult result) {
        validateEntity(user);
        
        if (result.hasErrors()) {
            String errorMessage = result.getFieldErrors().stream()
                .map(FieldError::getDefaultMessage)
                .findFirst()
                .orElse("Validation failed");
            throw new RegisterRelatedException(errorMessage);
        }

        if (userRepository.existsByPhoneNumber(user.getPhoneNumber())) {
            throw new UserExistsException("Phone number already in use");
        }

        if (userRepository.existsByEmail(user.getEmail())) {
            throw new UserExistsException("Email already in use");
        }

        // Encode password before saving
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return save(user);
    }

    public User update(User user) {
        validateEntity(user);
        User existingUser = findById(user.getUserId())
            .orElseThrow(() -> new UpdateRelatedException("User not found"));
        
        // Don't update password here
        user.setPassword(existingUser.getPassword());
        return save(user);
    }

    public User changePassword(int id, String oldPassword, String newPassword) {
        User user = findById(id)
            .orElseThrow(() -> new UpdateRelatedException("User not found"));

        if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
            throw new UpdateRelatedException("Current password is incorrect");
        }

        user.setPassword(passwordEncoder.encode(newPassword));
        return save(user);
    }

    public void deactivate(int id) {
        User user = findById(id)
            .orElseThrow(() -> new UpdateRelatedException("User not found"));
        user.setActive(false);
        save(user);
    }

    public void activate(int id) {
        User user = findById(id)
            .orElseThrow(() -> new UpdateRelatedException("User not found"));
        user.setActive(true);
        save(user);
    }

    // Exception classes
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
