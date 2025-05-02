package dut.gianguhohi.shoppiefood.utils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.Map;
import dut.gianguhohi.shoppiefood.models.misc.Address;

public class AddressManager {
    
    private static final String ADDRESS_FILE = "addresses.json";
    private static final ObjectMapper objectMapper = new ObjectMapper();
    private static Map<String, List<String>> cityToWards = new HashMap<>();


    private static void initializeCityToWards() {
        List<String> daNangWards = new ArrayList<>();

        daNangWards.add("Hải Châu");
        daNangWards.add("Hòa Cường");
        daNangWards.add("Thanh Khê");
        daNangWards.add("An Khê");
        daNangWards.add("An Hải");
        daNangWards.add("Sơn Trà");
        daNangWards.add("Ngũ Hành Sơn");
        daNangWards.add("Hòa Khánh");
        daNangWards.add("Liên Chiểu");
        daNangWards.add("Hải Vân");
        daNangWards.add("Cẩm Lệ");
        daNangWards.add("Hòa Xuân");
        daNangWards.add("Hòa Vang");
        daNangWards.add("Hòa Tiến");
        daNangWards.add("Bà Nà");
        daNangWards.add("Đặc khu Hoàng Sa");
        
        daNangWards.add("Núi Thành");
        daNangWards.add("Tam Mỹ");
        daNangWards.add("Tam Anh");
        daNangWards.add("Đức Phú");
        daNangWards.add("Tam Xuân");
        daNangWards.add("Tam Hải");
        daNangWards.add("Tam Kỳ");
        daNangWards.add("Quảng Phú");
        daNangWards.add("Hương Trà");
        daNangWards.add("Bàn Thạch");
        daNangWards.add("Tây Hồ");
        daNangWards.add("Chiên Đàn");
        daNangWards.add("Phú Ninh");
        daNangWards.add("Tiên Phước");
        daNangWards.add("Sơn Cẩm Hà");
        daNangWards.add("Thạnh Bình");
        daNangWards.add("Lãnh Ngọc");
        daNangWards.add("Trà Liên");
        daNangWards.add("Trà Giáp");
        daNangWards.add("Trà Tân");
        daNangWards.add("Trà Đốc");
        daNangWards.add("Trà My");
        daNangWards.add("Nam Trà My");
        daNangWards.add("Trà Tập");
        daNangWards.add("Trà Vân");
        daNangWards.add("Trà Linh");
        daNangWards.add("Trà Leng");
        daNangWards.add("Thăng Bình");
        daNangWards.add("Thăng An");
        daNangWards.add("Thăng Trường");
        daNangWards.add("Thăng Điền");
        daNangWards.add("Thăng Phú");
        daNangWards.add("Đồng Dương");
        daNangWards.add("Xuân Phú");
        daNangWards.add("Quế Sơn Trung");
        daNangWards.add("Quế Sơn");
        daNangWards.add("Nông Sơn");
        daNangWards.add("Quế Phước");
        daNangWards.add("Duy Nghĩa");
        daNangWards.add("Nam Phước");
        daNangWards.add("Duy Xuyên");
        daNangWards.add("Thu Bồn");
        daNangWards.add("Điện Bàn");
        daNangWards.add("Điện Bàn Đông");
        daNangWards.add("Anh Thắng");
        daNangWards.add("Điện Bàn Bắc");
        daNangWards.add("Điện Bàn Tây");
        daNangWards.add("Gò Nổi");
        daNangWards.add("Hội An");
        daNangWards.add("Hội An Đông");
        daNangWards.add("Hội An Tây");
        daNangWards.add("Tân Hiệp");
        daNangWards.add("Đại Lộc");
        daNangWards.add("Hà Nha");
        daNangWards.add("Thượng Đức");
        daNangWards.add("Vu Gia");
        daNangWards.add("Phú Thuận");
        daNangWards.add("Thạnh Mỹ");
        daNangWards.add("Bến Giằng");
        daNangWards.add("Nam Giang");
        daNangWards.add("Đắc Pring");
        daNangWards.add("La Dêê");
        daNangWards.add("La Êê");
        daNangWards.add("Sông Vàng");
        daNangWards.add("Sông Kôn");
        daNangWards.add("Đông Giang");
        daNangWards.add("Bến Hiên");
        daNangWards.add("A Vương");
        daNangWards.add("Tây Giang");
        daNangWards.add("Hùng Sơn");
        daNangWards.add("Hiệp Đức");
        daNangWards.add("Việt An");
        daNangWards.add("Phước Trà");
        daNangWards.add("Khâm Đức");
        daNangWards.add("Phước Năng");
        daNangWards.add("Phước Chánh");
        daNangWards.add("Phước Thành");
        daNangWards.add("Phước Hiệp");

        cityToWards.put("Đà Nẵng", daNangWards);


        List<String> hueWards = new ArrayList<>();

        hueWards.add("Phường Phong Điền");
        hueWards.add("Phường Phong Thái");
        hueWards.add("Phường Phong Dinh");
        hueWards.add("Phường Phong Phú");
        hueWards.add("Phường Phong Quảng");
        hueWards.add("Xã Đan Điền");
        hueWards.add("Xã Quảng Điền");
        hueWards.add("Phường Hương Trà");
        hueWards.add("Phường Kim Trà");
        hueWards.add("Xã Bình Điền");
        hueWards.add("Phường Kim Long");
        hueWards.add("Phường Hương An");
        hueWards.add("Phường Phú Xuân");
        hueWards.add("Phường Thuận An");
        hueWards.add("Phường Hóa Châu");
        hueWards.add("Phường Dương Nỗ");
        hueWards.add("Phường Mỹ Thượng");
        hueWards.add("Phường Vỹ Dạ");
        hueWards.add("Phường Thuận Hóa");
        hueWards.add("Phường An Cựu");
        hueWards.add("Phường Thủy Xuân");
        hueWards.add("Xã Phú Vinh");
        hueWards.add("Xã Phú Hồ");
        hueWards.add("Xã Phú Vang");
        hueWards.add("Phường Thanh Thủy");
        hueWards.add("Phường Hương Thủy");
        hueWards.add("Phường Phú Bài");
        hueWards.add("Xã Vinh Lộc");
        hueWards.add("Xã Lộc Sơn");
        hueWards.add("Xã Lộc An");
        hueWards.add("Xã Phú Lộc");
        hueWards.add("Xã Chân Mây - Lăng Cô");
        hueWards.add("Xã Long Quảng");
        hueWards.add("Xã Nam Đông");
        hueWards.add("Xã Khe Tre");
        hueWards.add("Xã A Lưới 1");
        hueWards.add("Xã A Lưới 2");
        hueWards.add("Xã A Lưới 3");
        hueWards.add("Xã A Lưới 4");
        hueWards.add("Xã A Lưới 5");

        cityToWards.put("Huế", hueWards);
    }

    public static void loadAddresses() {
        File file = new File(ADDRESS_FILE);

        try {
            if (!file.exists()) {
                // Create sample address data if file doesn't exist
                initializeCityToWards();
                // Save to file
                saveAddresses();
            } else {
                // Read existing data from file
                Map<String, Object> data = objectMapper.readValue(file, new TypeReference<Map<String, Object>>() {});
                cityToWards = objectMapper.convertValue(data.get("cityToWards"),
                    new TypeReference<Map<String, List<String>>>() {});
            }
        } catch (IOException e) {
            e.printStackTrace();
            initializeCityToWards();
        }
    }

    private static void saveAddresses() {
        try {
            Map<String, Object> data = new HashMap<>();
            data.put("cityToWards", cityToWards);
            objectMapper.writeValue(new File(ADDRESS_FILE), data);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static List<String> getWardsByCityName(String cityName) {
        return cityToWards.getOrDefault(cityName, new ArrayList<>());
    }
}
