package dut.gianguhohi.shoppiefood.modules.users.dtos;

public class RegisterResponse {
    private final String token;
    private final UserDTO user;

    public RegisterResponse(String token, UserDTO user) {
        this.token = token;
        this.user = user;
    }

    public String getToken() {
        return token;
    }

    public UserDTO getUser() {
        return user;
    }
}
