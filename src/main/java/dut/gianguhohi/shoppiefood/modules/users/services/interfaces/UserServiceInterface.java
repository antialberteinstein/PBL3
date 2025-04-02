package dut.gianguhohi.shoppiefood.modules.users.services.interfaces;
import dut.gianguhohi.shoppiefood.modules.users.dtos.LoginResponse;
import dut.gianguhohi.shoppiefood.modules.users.dtos.RegisterResponse;
import dut.gianguhohi.shoppiefood.modules.users.dtos.CustomerRegisterRequest;
import dut.gianguhohi.shoppiefood.modules.users.dtos.LoginRequest;


public interface UserServiceInterface {
    
    LoginResponse login(LoginRequest loginRequest);

    RegisterResponse register(CustomerRegisterRequest registerRequest);
}
