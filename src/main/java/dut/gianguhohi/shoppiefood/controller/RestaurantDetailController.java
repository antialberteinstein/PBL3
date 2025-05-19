package dut.gianguhohi.shoppiefood.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import jakarta.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class RestaurantDetailController {

    @GetMapping("/restaurant/{id}")
    public String getRestaurantDetail(@PathVariable String id, Model model, HttpSession session) {
        try {
            // Đặt role là "restaurant" để hiển thị sidebar đúng
            session.setAttribute("role", "restaurant");
            
            // Tạo dữ liệu mẫu cho nhà hàng
            Map<String, Object> restaurant = new HashMap<>();
            restaurant.put("id", id);
            restaurant.put("name", "Nhà hàng ngon nhất Đà Nẵng");
            restaurant.put("description", "Nhà hàng phục vụ các món ăn đặc sản với hương vị truyền thống. Thực đơn đa dạng từ các món khai vị đến món tráng miệng, đáp ứng mọi nhu cầu của thực khách.");
            restaurant.put("imageUrl", "/images/Avatar/restaurant.png");
            
            // Tạo danh sách chi nhánh mẫu
            List<Map<String, Object>> branches = new ArrayList<>();
            
            // Chi nhánh 1
            Map<String, Object> branch1 = new HashMap<>();
            branch1.put("branchId", 1);
            branch1.put("branchName", "Chi nhánh Trung tâm");
            
            Map<String, Object> address1 = new HashMap<>();
            address1.put("addressLine1", "123 Lê Duẩn");
            address1.put("addressLine2", "Tầng 5, Tòa nhà Landmark");
            address1.put("ward", "Phường Bến Nghé");
            address1.put("district", "Quận 1");
            address1.put("city", "TP. Hồ Chí Minh");
            
            branch1.put("address", address1);
            branches.add(branch1);
            
            // Chi nhánh 2
            Map<String, Object> branch2 = new HashMap<>();
            branch2.put("branchId", 2);
            branch2.put("branchName", "Chi nhánh Quận 3");
            
            Map<String, Object> address2 = new HashMap<>();
            address2.put("addressLine1", "456 Nguyễn Đình Chiểu");
            address2.put("addressLine2", "");
            address2.put("ward", "Phường 4");
            address2.put("district", "Quận 3");
            address2.put("city", "TP. Hồ Chí Minh");
            
            branch2.put("address", address2);
            branches.add(branch2);
            
            // Chi nhánh 3
            Map<String, Object> branch3 = new HashMap<>();
            branch3.put("branchId", 3);
            branch3.put("branchName", "Chi nhánh Quận 7");
            
            Map<String, Object> address3 = new HashMap<>();
            address3.put("addressLine1", "789 Nguyễn Hữu Thọ");
            address3.put("addressLine2", "Khu Công nghệ cao");
            address3.put("ward", "Phường Tân Phong");
            address3.put("district", "Quận 7");
            address3.put("city", "TP. Hồ Chí Minh");
            
            branch3.put("address", address3);
            branches.add(branch3);
            
            // Thêm dữ liệu vào model
            model.addAttribute("restaurant", restaurant);
            model.addAttribute("branches", branches);
            
            return "restaurant/profile";
        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("errorMessage", "Đã xảy ra lỗi khi hiển thị thông tin nhà hàng.");
            return "error/general";
        }
    }
}
