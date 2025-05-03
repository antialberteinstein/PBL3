package dut.gianguhohi.shoppiefood.services.Users;

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
    

    public User create(@Valid User user, BindingResult result) {
        if (result.hasErrors()) {
            for (FieldError error : result.getFieldErrors()) {
                throw new UserRelatedException(error.getDefaultMessage());
            }
        }

        if (userRepository.existsByPhoneNumber(user.getPhoneNumber())) {
            throw new UserRelatedException("Số điện thoại đã có người sử dụng");
        }

        if (userRepository.existsByEmail(user.getEmail())) {
            throw new UserRelatedException("Email đã có người sử dụng");
        }

        return userRepository.save(user);
    }

    public User readByPhoneNumber(String phoneNumber) {
        User user = userRepository.findByPhoneNumber(phoneNumber);
        if (user == null) {
            throw new UserRelatedException("Số điện thoại không tồn tại");
        }
        return user;
    }

    public User readByEmail(String email) {
        User user = userRepository.findByEmail(email);
        if (user == null) {
            throw new UserRelatedException("Email không tồn tại");
        }
        return user;
    }



    public static class UserRelatedException extends RuntimeException {
        public UserRelatedException(String message) {
            super(message);
        }
    }
}
