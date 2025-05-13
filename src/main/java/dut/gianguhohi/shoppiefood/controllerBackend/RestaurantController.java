package dut.gianguhohi.shoppiefood.controllerBackend;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import jakarta.servlet.http.HttpSession;
import dut.gianguhohi.shoppiefood.models.Users.User;

@Controller
public class RestaurantController {

    @PostMapping("/restaurant/enter")
    @ResponseBody
    public String enter(HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            throw new RuntimeException("User not logged in");
        }
        session.setAttribute("role", "restaurant");
        return "success";
    }
}