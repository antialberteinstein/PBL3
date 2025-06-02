package dut.gianguhohi.shoppiefood.services;

import java.util.*;

import org.springframework.stereotype.Service;
import dut.gianguhohi.shoppiefood.models.misc.Address;

@Service
public class RoutingService {
    
    // Các Map lưu trữ ma trận khoảng cách và danh sách phường xã
    private static final Map<String, double[][]> DISTANCE_MATRICES = new HashMap<>();
    private static final Map<String, List<String>> CITY_WARDS = new HashMap<>();
    
    // Các điểm trung chuyển khi đi từ tỉnh này sang tỉnh khác
    private static final String HUE_TRANSIT_POINT = "Xã Chân Mây - Lăng Cô";
    private static final String DANANG_TRANSIT_POINT = "Hải Vân";
    
    // Khoảng cách giữa điểm trung chuyển của hai tỉnh (đèo Hải Vân)
    private static final double INTER_CITY_DISTANCE = 25.0; // km
    
    // Khởi tạo dữ liệu khoảng cách
    static {
        initializeHueData();
        initializeDaNangData();
    }
    
    /**
     * Tìm tuyến đường ngắn nhất giữa hai địa chỉ, có thể ở cùng hoặc khác thành phố
     * 
     * @param a Địa chỉ xuất phát
     * @param b Địa chỉ đích
     * @return Khoảng cách ngắn nhất tính bằng km, hoặc -1 nếu không tìm thấy đường đi
     */
    public double route(Address a, Address b) {
        if (a == null || b == null) {
            throw new IllegalArgumentException("Địa chỉ không được phép là null");
        }
        
        String cityA = a.getCity();
        String cityB = b.getCity();
        String wardA = a.getWard();
        String wardB = b.getWard();
        
        // Kiểm tra nếu cả hai địa chỉ cùng thành phố
        if (cityA.equals(cityB)) {
            // Nếu cùng phường/xã thì trả về 1km
            if (wardA.equals(wardB)) {
                return 1.0;
            }
            // Gọi hàm route để tính khoảng cách trong cùng thành phố
            return route(cityA, wardA, wardB);
        }
        
        // Nếu khác thành phố, tính khoảng cách qua điểm trung chuyển
        double distanceA = 0;
        double distanceB = 0;
        
        // Xác định điểm trung chuyển cho mỗi thành phố
        if (cityA.equals("Huế")) {
            distanceA = wardA.equals(HUE_TRANSIT_POINT) ? 0 : route("Huế", wardA, HUE_TRANSIT_POINT);
            distanceB = wardB.equals(DANANG_TRANSIT_POINT) ? 0 : route("Đà Nẵng", DANANG_TRANSIT_POINT, wardB);
        } else if (cityA.equals("Đà Nẵng")) {
            distanceA = wardA.equals(DANANG_TRANSIT_POINT) ? 0 : route("Đà Nẵng", wardA, DANANG_TRANSIT_POINT);
            distanceB = wardB.equals(HUE_TRANSIT_POINT) ? 0 : route("Huế", HUE_TRANSIT_POINT, wardB);
        } else {
            throw new IllegalArgumentException("Thành phố không được hỗ trợ: " + cityA);
        }
        
        // Nếu không tìm được đường đi qua điểm trung chuyển
        if (distanceA < 0 || distanceB < 0) {
            return -1;
        }
        
        // Tính tổng khoảng cách: từ điểm A đến điểm trung chuyển + khoảng cách giữa hai điểm trung chuyển + từ điểm trung chuyển đến điểm B
        return distanceA + INTER_CITY_DISTANCE + distanceB;
    }
    
    /**
     * Tìm tuyến đường ngắn nhất giữa hai phường/xã trong cùng một thành phố
     * 
     * @param city Tên thành phố (ví dụ: "Huế" hoặc "Đà Nẵng")
     * @param startWard Phường/xã xuất phát
     * @param destWard Phường/xã đích
     * @return Khoảng cách ngắn nhất tính bằng km, hoặc -1 nếu không tìm thấy đường đi
     */
    public double route(String city, String startWard, String destWard) {
        // Kiểm tra tính hợp lệ của các tham số
        if (city == null || startWard == null || destWard == null) {
            throw new IllegalArgumentException("Thành phố và phường/xã không được phép là null");
        }
        
        // Kiểm tra xem thành phố có trong dữ liệu hay không
        if (!CITY_WARDS.containsKey(city)) {
            throw new IllegalArgumentException("Thành phố không được hỗ trợ: " + city);
        }
        
        List<String> wards = CITY_WARDS.get(city);
        double[][] distances = DISTANCE_MATRICES.get(city);
        
        // Nếu cùng phường/xã thì trả về 1km
        if (startWard.equals(destWard)) {
            return 1.0;
        }
        
        // Lấy chỉ số của phường/xã xuất phát và đích
        int startIdx = wards.indexOf(startWard);
        int destIdx = wards.indexOf(destWard);
        
        // Kiểm tra xem phường/xã có tồn tại trong thành phố không
        if (startIdx == -1) {
            throw new IllegalArgumentException("Không tìm thấy phường/xã xuất phát trong " + city + ": " + startWard);
        }
        if (destIdx == -1) {
            throw new IllegalArgumentException("Không tìm thấy phường/xã đích trong " + city + ": " + destWard);
        }
        
        // Chạy thuật toán Dijkstra
        return findShortestPath(distances, startIdx, destIdx);
    }
    
    /**
     * Triển khai thuật toán Dijkstra để tìm đường đi ngắn nhất
     * 
     * @param graph Ma trận khoảng cách biểu diễn đồ thị
     * @param start Chỉ số của đỉnh xuất phát
     * @param end Chỉ số của đỉnh đích
     * @return Khoảng cách ngắn nhất từ đỉnh xuất phát đến đỉnh đích
     */
    private double findShortestPath(double[][] graph, int start, int end) {
        int n = graph.length;
        
        // Mảng khoảng cách để lưu khoảng cách ngắn nhất từ đỉnh xuất phát đến mỗi đỉnh
        double[] dist = new double[n];
        
        // Mảng đánh dấu các đỉnh đã được xử lý
        boolean[] visited = new boolean[n];
        
        // Khởi tạo khoảng cách là vô cùng và mảng đã xử lý là false
        Arrays.fill(dist, Double.MAX_VALUE);
        Arrays.fill(visited, false);
        
        // Khoảng cách đến đỉnh xuất phát là 0
        dist[start] = 0;
        
        // Xử lý tất cả các đỉnh
        for (int i = 0; i < n - 1; i++) {
            // Tìm đỉnh có khoảng cách nhỏ nhất chưa được xử lý
            int minVertex = findMinDistVertex(dist, visited);
            
            // Đánh dấu đỉnh đã được xử lý
            visited[minVertex] = true;
            
            // Cập nhật khoảng cách cho các đỉnh kề
            for (int j = 0; j < n; j++) {
                // Cập nhật dist[j] chỉ khi:
                // 1. Có cạnh từ minVertex đến j
                // 2. j chưa được xử lý
                // 3. Đường đi qua minVertex ngắn hơn giá trị hiện tại của dist[j]
                if (graph[minVertex][j] > 0 && !visited[j] && 
                    dist[minVertex] != Double.MAX_VALUE && 
                    dist[minVertex] + graph[minVertex][j] < dist[j]) {
                    dist[j] = dist[minVertex] + graph[minVertex][j];
                }
            }
        }
        
        // Trả về khoảng cách ngắn nhất đã tính toán đến đỉnh đích
        return dist[end] == Double.MAX_VALUE ? -1 : dist[end];
    }
    
    /**
     * Tìm đỉnh có khoảng cách nhỏ nhất chưa được xử lý
     * 
     * @param dist Mảng khoảng cách
     * @param visited Mảng đánh dấu các đỉnh đã xử lý
     * @return Chỉ số của đỉnh có khoảng cách nhỏ nhất
     */
    private int findMinDistVertex(double[] dist, boolean[] visited) {
        double min = Double.MAX_VALUE;
        int minIndex = -1;
        
        for (int i = 0; i < dist.length; i++) {
            if (!visited[i] && dist[i] <= min) {
                min = dist[i];
                minIndex = i;
            }
        }
        
        return minIndex;
    }
    
    /**
     * Khởi tạo ma trận khoảng cách và danh sách phường/xã cho Huế
     */
    private static void initializeHueData() {
        // Danh sách các phường/xã của Huế
        List<String> hueWards = Arrays.asList(
            "Phường Phong Điền", "Phường Phong Thái", "Phường Phong Dinh", "Phường Phong Phú",
            "Phường Phong Quảng", "Xã Đan Điền", "Xã Quảng Điền", "Phường Hương Trà", "Phường Kim Trà",
            "Xã Bình Điền", "Phường Kim Long", "Phường Hương An", "Phường Phú Xuân", "Phường Thuận An",
            "Phường Hóa Châu", "Phường Dương Nỗ", "Phường Mỹ Thượng", "Phường Vỹ Dạ", "Phường Thuận Hóa",
            "Phường An Cựu", "Phường Thủy Xuân", "Xã Phú Vinh", "Xã Phú Hồ", "Xã Phú Vang", "Phường Thanh Thủy",
            "Phường Hương Thủy", "Phường Phú Bài", "Xã Vinh Lộc", "Xã Lộc Sơn", "Xã Lộc An", "Xã Phú Lộc",
            "Xã Chân Mây - Lăng Cô", "Xã Long Quảng", "Xã Nam Đông", "Xã Khe Tre", "Xã A Lưới 1", 
            "Xã A Lưới 2", "Xã A Lưới 3", "Xã A Lưới 4", "Xã A Lưới 5"
        );
        
        // Số lượng phường/xã
        int n = hueWards.size();
        
        // Tạo ma trận khoảng cách (khởi tạo với tất cả các giá trị là 0)
        double[][] hueDistances = new double[n][n];
        
        // Điền ma trận với khoảng cách (đây chỉ là dữ liệu mẫu - trong ứng dụng thực tế, bạn sẽ sử dụng khoảng cách thực)
        Random random = new Random(42); // Seed cố định để đảm bảo tính tái tạo
        
        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                // Tạo khoảng cách giữa 1.0 và 10.0 km cho các khu vực đô thị
                double distance;
                
                // Các khu vực miền núi và vùng xa có khoảng cách lớn hơn
                String ward1 = hueWards.get(i).toLowerCase();
                String ward2 = hueWards.get(j).toLowerCase();
                
                // Kiểm tra xem phường/xã có ở vùng núi hoặc xa không
                boolean isRemote = ward1.contains("a lưới") || ward2.contains("a lưới") || 
                                   ward1.contains("nam đông") || ward2.contains("nam đông") ||
                                   ward1.contains("khe tre") || ward2.contains("khe tre") ||
                                   ward1.contains("chân mây") || ward2.contains("chân mây") ||
                                   ward1.contains("lăng cô") || ward2.contains("lăng cô");
                
                if (isRemote) {
                    // Khoảng cách cho các vùng xa là từ 8.0 đến 20.0 km
                    distance = 8.0 + random.nextDouble() * 12.0;
                } else {
                    // Khoảng cách cho các khu vực đô thị là từ 1.0 đến 7.0 km
                    distance = 1.0 + random.nextDouble() * 6.0;
                }
                
                // Làm cho đồ thị đối xứng
                hueDistances[i][j] = distance;
                hueDistances[j][i] = distance;
            }
        }
        
        // Lưu trữ dữ liệu
        CITY_WARDS.put("Huế", hueWards);
        DISTANCE_MATRICES.put("Huế", hueDistances);
    }
    
    /**
     * Khởi tạo ma trận khoảng cách và danh sách phường/xã cho Đà Nẵng
     */
    private static void initializeDaNangData() {
        // Danh sách các phường/xã của Đà Nẵng
        List<String> danangWards = Arrays.asList(
            "Hải Châu", "Hòa Cường", "Thanh Khê", "An Khê", "An Hải", "Sơn Trà", "Ngũ Hành Sơn", 
            "Hòa Khánh", "Liên Chiểu", "Hải Vân", "Cẩm Lệ", "Hòa Xuân", "Hòa Vang", "Hòa Tiến", 
            "Bà Nà", "Đặc khu Hoàng Sa", "Núi Thành", "Tam Mỹ", "Tam Anh", "Đức Phú", "Tam Xuân", 
            "Tam Hải", "Tam Kỳ", "Quảng Phú", "Hương Trà", "Bàn Thạch", "Tây Hồ", "Chiên Đàn", 
            "Phú Ninh", "Tiên Phước", "Sơn Cẩm Hà", "Thạnh Bình", "Lãnh Ngọc", "Trà Liên", "Trà Giáp", 
            "Trà Tân", "Trà Đốc", "Trà My", "Nam Trà My", "Trà Tập", "Trà Vân", "Trà Linh", "Trà Leng", 
            "Thăng Bình", "Thăng An", "Thăng Trường", "Thăng Điền", "Thăng Phú", "Đồng Dương", "Xuân Phú", 
            "Quế Sơn Trung", "Quế Sơn", "Nông Sơn", "Quế Phước", "Duy Nghĩa", "Nam Phước", "Duy Xuyên", 
            "Thu Bồn", "Điện Bàn", "Điện Bàn Đông", "Anh Thắng", "Điện Bàn Bắc", "Điện Bàn Tây", "Gò Nổi", 
            "Hội An", "Hội An Đông", "Hội An Tây", "Tân Hiệp", "Đại Lộc", "Hà Nha", "Thượng Đức", "Vu Gia", 
            "Phú Thuận", "Thạnh Mỹ", "Bến Giằng", "Nam Giang", "Đắc Pring", "La Dêê", "La Êê", "Sông Vàng", 
            "Sông Kôn", "Đông Giang", "Bến Hiên", "A Vương", "Tây Giang", "Hùng Sơn", "Hiệp Đức", "Việt An", 
            "Phước Trà", "Khâm Đức", "Phước Năng", "Phước Chánh", "Phước Thành", "Phước Hiệp"
        );
        
        // Số lượng phường/xã
        int n = danangWards.size();
        
        // Tạo ma trận khoảng cách (khởi tạo với tất cả các giá trị là 0)
        double[][] danangDistances = new double[n][n];
        
        // Điền ma trận với khoảng cách (đây chỉ là dữ liệu mẫu - trong ứng dụng thực tế, bạn sẽ sử dụng khoảng cách thực)
        Random random = new Random(43); // Seed khác để đảm bảo tính tái tạo
        
        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                // Tạo khoảng cách giữa 1.0 và 10.0 km cho các khu vực đô thị
                double distance;
                
                // Các khu vực miền núi và vùng xa có khoảng cách lớn hơn
                String ward1 = danangWards.get(i).toLowerCase();
                String ward2 = danangWards.get(j).toLowerCase();
                
                // Kiểm tra xem phường/xã có ở vùng núi hoặc xa không
                boolean isRemote = ward1.contains("hoàng sa") || ward2.contains("hoàng sa") ||
                                  ward1.contains("bà nà") || ward2.contains("bà nà") ||
                                  ward1.contains("hải vân") || ward2.contains("hải vân") ||
                                  ward1.contains("sơn trà") || ward2.contains("sơn trà") ||
                                  ward1.contains("ngũ hành sơn") || ward2.contains("ngũ hành sơn") ||
                                  ward1.contains("trà") || ward2.contains("trà") ||
                                  ward1.contains("nam giang") || ward2.contains("nam giang") ||
                                  ward1.contains("đông giang") || ward2.contains("đông giang") ||
                                  ward1.contains("tây giang") || ward2.contains("tây giang") ||
                                  ward1.contains("phước") && !ward1.contains("nam phước") || 
                                  ward2.contains("phước") && !ward2.contains("nam phước");
                
                if (isRemote) {
                    // Khoảng cách cho các vùng xa là từ 8.0 đến 20.0 km
                    distance = 8.0 + random.nextDouble() * 12.0;
                } else {
                    // Khoảng cách cho các khu vực đô thị là từ 1.0 đến 7.0 km
                    distance = 1.0 + random.nextDouble() * 6.0;
                }
                
                // Làm cho đồ thị đối xứng
                danangDistances[i][j] = distance;
                danangDistances[j][i] = distance;
            }
        }
        
        // Lưu trữ dữ liệu
        CITY_WARDS.put("Đà Nẵng", danangWards);
        DISTANCE_MATRICES.put("Đà Nẵng", danangDistances);
    }
    
    /**
     * Lấy ma trận khoảng cách cho một thành phố
     * @param city Tên thành phố
     * @return Ma trận khoảng cách
     */
    public double[][] getDistanceMatrix(String city) {
        return DISTANCE_MATRICES.get(city);
    }
    
    /**
     * Lấy danh sách phường/xã cho một thành phố
     * @param city Tên thành phố
     * @return Danh sách phường/xã
     */
    public List<String> getWards(String city) {
        return CITY_WARDS.get(city);
    }
}