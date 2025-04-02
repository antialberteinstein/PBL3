package dut.gianguhohi.shoppiefood.modules.users.services.impl;


import dut.gianguhohi.shoppiefood.modules.users.services.interfaces.UserServiceInterface;

import org.springframework.stereotype.Service;
import dut.gianguhohi.shoppiefood.modules.users.dtos.LoginResponse;
import dut.gianguhohi.shoppiefood.modules.users.dtos.LoginRequest;
import dut.gianguhohi.shoppiefood.modules.users.dtos.UserDTO;
import dut.gianguhohi.shoppiefood.modules.users.repositories.CustomerRepository;
import dut.gianguhohi.shoppiefood.modules.users.repositories.UserRepository;

import dut.gianguhohi.shoppiefood.modules.users.dtos.RegisterResponse;
import dut.gianguhohi.shoppiefood.modules.users.dtos.CustomerRegisterRequest;

@Service
public class UserService implements UserServiceInterface {

    private final UserRepository userRepository = new UserRepository();
    private final CustomerRepository customerRepository = new CustomerRepository();

    @Override
    public LoginResponse login(LoginRequest loginRequest) {
        try {
            String username = loginRequest.getUsername();
            String password = loginRequest.getPassword();
            int userId = validateUser(username, password);

            if (userId != -1) {
                String token = "happy_day";
                UserDTO user = new UserDTO(userId, username);
                return new LoginResponse(token, user);
            } else {
                throw new InvalidUserNamePasswordException("Invalid username or password");
            }
            
            
        }
        catch (InvalidUserNamePasswordException e) {
            throw e;
        }
        catch (Exception e) {
            throw new RuntimeException("There are some errors in login process");
        }
    }

    @Override
    public RegisterResponse register(CustomerRegisterRequest registerRequest) {
        try {
            String username = registerRequest.getUsername();
            String password = registerRequest.getPassword();

            String name = registerRequest.getName();
            String phoneNumber = registerRequest.getPhoneNumber();
            String dob = registerRequest.getDob();
            int gender = registerRequest.getGender();

            if (userRepository.isUserExist(username)) {
                throw new UserAlreadyExistException("User already exists");
            } else {
                int id = userRepository.createUser(username, password);
                if (id == -1) {
                    throw new RuntimeException("There are some errors in registration process");
                } else {
                    customerRepository.createCustomer(id, name, phoneNumber, dob, gender);
                    
                    String token = "happy_day";
                    UserDTO user = new UserDTO(id, username);

                    return new RegisterResponse(token, user);
                }
            }
        } catch (UserAlreadyExistException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("There are some errors in registration process");
        }
    }

    private int validateUser(String userName, String password) {
        return userRepository.validateUser(userName, password);
    }

    public static class InvalidUserNamePasswordException extends RuntimeException {
        public InvalidUserNamePasswordException(String message) {
            super(message);
        }
    }

    public static class UserAlreadyExistException extends RuntimeException {
        public UserAlreadyExistException(String message) {
            super(message);
        }
    }
}
