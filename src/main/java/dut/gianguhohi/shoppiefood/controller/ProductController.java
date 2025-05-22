package dut.gianguhohi.shoppiefood.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import jakarta.servlet.http.HttpSession;

import org.springframework.web.bind.annotation.GetMapping;

import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.Map;

@Controller
public class ProductController {
    @GetMapping("/product/info")
    public String getProductInfo(Model model, HttpSession session) {
        try {
            // Thêm role vào session
            session.setAttribute("role", "user");
            
            // Tạo dữ liệu mẫu cho sản phẩm
            Map<String, Object> product = new HashMap<>();
            product.put("id", "123");
            product.put("name", "Bún măng gà");
            product.put("price", 100000);
            product.put("description", "Bún măng gà với nước dùng ngọt thanh từ xương gà hầm cùng măng tươi, thịt gà mềm, ngọt, thấm vị.");
            product.put("imageUrl", "/images/Food/bunmangga.png");
            
            // Tạo thông tin nhà hàng
            Map<String, Object> restaurant = new HashMap<>();
            restaurant.put("id", "rest123");
            restaurant.put("name", "Quán ăn Phong Phú");
            restaurant.put("address", "179 Trần Cao Vân, Quận Thanh Khê, TP.Đà Nẵng");
            product.put("restaurant", restaurant);
            
            // Tạo danh sách feedback
            List<String> feedbacks = new ArrayList<>();
            feedbacks.add("Rất ngon, sẽ ủng hộ lần sau.");
            feedbacks.add("Đóng gói kỹ, giao nhanh.");
            feedbacks.add("Phần ăn nhiều, thịt gà mềm, nước dùng ngọt.");
            
            // Tạo danh sách sản phẩm liên quan
            List<Map<String, Object>> relatedProducts = new ArrayList<>();
            
            Map<String, Object> product1 = new HashMap<>();
            product1.put("id", "456");
            product1.put("name", "Bún đậu mắm tôm");
            product1.put("price", 30000);
            product1.put("imageUrl", "/images/Food/bundau.png");
            relatedProducts.add(product1);
            
            Map<String, Object> product2 = new HashMap<>();
            product2.put("id", "789");
            product2.put("name", "Bún bò Huế");
            product2.put("price", 45000);
            product2.put("imageUrl", "/images/Food/bunbo.png");
            relatedProducts.add(product2);
            
            // Thêm dữ liệu vào model để truyền sang view
            model.addAttribute("product", product);
            model.addAttribute("feedbacks", feedbacks);
            model.addAttribute("relatedProducts", relatedProducts);
            
            return "product/info";
        } catch (Exception e) {
            e.printStackTrace(); // Log lỗi để dễ dàng debug
            model.addAttribute("errorMessage", "Đã xảy ra lỗi khi hiển thị thông tin sản phẩm.");
            return "error/general";
        }
    }
}
