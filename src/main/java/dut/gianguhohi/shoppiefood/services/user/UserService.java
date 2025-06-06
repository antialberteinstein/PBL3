package dut.gianguhohi.shoppiefood.services.user;

import dut.gianguhohi.shoppiefood.models.Users.User;
import dut.gianguhohi.shoppiefood.repositories.Users.UserRepository;
import dut.gianguhohi.shoppiefood.utils.AppServiceException;
import dut.gianguhohi.shoppiefood.utils.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;

import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Transactional
@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;

    // ==========================
    // == User Queries         ==
    // ==========================
    
    /**
     * Tìm người dùng theo số điện thoại
     * @param phoneNumber Số điện thoại cần tìm
     * @return User nếu tìm thấy, null nếu không tìm thấy
     */
    public User readByPhoneNumber(String phoneNumber) {
        return userRepository.findByPhoneNumber(phoneNumber);
    }

    /**
     * Tìm người dùng theo email
     * @param email Email cần tìm
     * @return User nếu tìm thấy, null nếu không tìm thấy
     */
    public User readByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    /**
     * Tìm người dùng theo ID
     * @param id ID người dùng cần tìm
     * @return User nếu tìm thấy
     * @throws ResponseStatusException nếu không tìm thấy
     */
    public User readById(int id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, 
                        "Không tìm thấy người dùng với ID: " + id)
                );
    }

    /**
     * Trả về tất cả người dùng trong hệ thống
     * @return Danh sách người dùng
     */
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    // ==========================
    // == User CRUD/Auth       ==
    // ==========================
    
    /**
     * Đăng ký người dùng mới
     */
    public User register(
        String phoneNumber,
        String email,
        String password,
        String confirmPassword,
        String name,
        String dateOfBirth,
        String gender
    ) {
        validateRegister(phoneNumber, email, password, confirmPassword, name, dateOfBirth, gender);

        if (userRepository.existsByPhoneNumber(phoneNumber)) {
            throw new AppServiceException("Số điện thoại đã có người sử dụng");
        }
        if (userRepository.existsByEmail(email)) {
            throw new AppServiceException("Email đã có người sử dụng");
        }

        // Mã hóa mật khẩu trước khi lưu vào DB
        String encodedPassword = passwordEncoder.encode(password);
        User user = new User(name, encodedPassword, phoneNumber, email, gender, dateOfBirth);
        return userRepository.save(user);
    }

    /**
     * Cập nhật thông tin người dùng
     */
    public User update(
        int id,
        String newPhoneNumber,
        String newEmail,
        String newName,
        String newDateOfBirth,
        String newGender
    ) {
        User existingUser = readById(id);
        validateUpdate(newPhoneNumber, newEmail, newName, newDateOfBirth, newGender);

        // Kiểm tra email và số điện thoại đã tồn tại chưa
        User phoneUser = userRepository.findByPhoneNumber(newPhoneNumber);
        if (phoneUser != null && phoneUser.getUserId() != id) {
            throw new ResponseStatusException(
                HttpStatus.BAD_REQUEST, 
                "Số điện thoại đã được sử dụng bởi tài khoản khác"
            );
        }

        User emailUser = userRepository.findByEmail(newEmail);
        if (emailUser != null && emailUser.getUserId() != id) {
            throw new ResponseStatusException(
                HttpStatus.BAD_REQUEST, 
                "Email đã được sử dụng bởi tài khoản khác"
            );
        }

        existingUser.setPhoneNumber(newPhoneNumber);
        existingUser.setEmail(newEmail);
        existingUser.setName(newName);
        existingUser.setDateOfBirth(newDateOfBirth);
        existingUser.setGender(newGender);

        return userRepository.save(existingUser);
    }

    /**
     * Xóa người dùng
     */
    public void delete(int id) {
        Validator.validateId(id, "ID không hợp lệ");
        User user = readById(id);
        userRepository.delete(user);
    }

    /**
     * Vô hiệu hóa tài khoản người dùng
     */
    public void disable(int id) {
        Validator.validateId(id, "ID không hợp lệ");
        User user = readById(id);
        user.setIsActive(false);
        userRepository.save(user);
    }

    // ==========================
    // == Validation Methods   ==
    // ==========================
    
    /**
     * Kiểm tra dữ liệu đăng ký
     */
    private void validateRegister(
        String phoneNumber,
        String email,
        String password,
        String confirmPassword,
        String name,
        String dateOfBirth,
        String gender
    ) {
        Validator.validatePhoneNumber(phoneNumber);
        Validator.validateEmail(email);
        Validator.validatePassword(password);
        Validator.validateName(name);
        Validator.validateString(dateOfBirth, "Ngày sinh không được để trống");
        Validator.validateString(gender, "Bạn chưa chọn giới tính");
        validateConfirmPassword(password, confirmPassword);
    }

    /**
     * Kiểm tra dữ liệu cập nhật
     */
    private void validateUpdate(
        String phoneNumber,
        String email,
        String name,
        String dateOfBirth,
        String gender
    ) {
        Validator.validatePhoneNumber(phoneNumber);
        Validator.validateEmail(email);
        Validator.validateName(name);
        Validator.validateString(dateOfBirth, "Ngày sinh không được để trống");
        Validator.validateString(gender, "Bạn chưa chọn giới tính");
    }

    /**
     * Kiểm tra mật khẩu xác nhận
     */
    private void validateConfirmPassword(String password, String confirmPassword) {
        if (confirmPassword == null || confirmPassword.trim().isEmpty()) {
            throw new AppServiceException("Mật khẩu xác nhận không được để trống");
        }
        if (!confirmPassword.equals(password)) {
            throw new AppServiceException("Mật khẩu xác nhận không khớp");
        }
    }

    /**
     * Cập nhật thông tin cá nhân từ giao diện profile
     * @param userId ID người dùng cần cập nhật
     * @param name Tên mới
     * @param email Email mới
     * @param phoneNumber Số điện thoại mới
     * @param dateOfBirth Ngày sinh mới
     * @return User đã được cập nhật
     * @throws ResponseStatusException nếu có lỗi
     */
    public User updateUserProfile(Integer userId, String name, String email, String phoneNumber, String dateOfBirth) {
        if (userId == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "ID người dùng không được null");
        }

        User user = readById(userId);

        // Cập nhật tên nếu có
        if (name != null && !name.isEmpty()) {
            user.setName(name);
        }

        // Cập nhật email nếu có và chưa được sử dụng
        if (email != null && !email.isEmpty()) {
            User existingUserWithEmail = userRepository.findByEmail(email);
            if (existingUserWithEmail != null && !Objects.equals(existingUserWithEmail.getUserId(), userId)) {
                throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, 
                    "Email đã được sử dụng bởi tài khoản khác"
                );
            }
            user.setEmail(email);
        }
        
        // Cập nhật số điện thoại nếu có và chưa được sử dụng
        if (phoneNumber != null && !phoneNumber.isEmpty()) {
            User existingUserWithPhone = userRepository.findByPhoneNumber(phoneNumber);
            if (existingUserWithPhone != null && !Objects.equals(existingUserWithPhone.getUserId(), userId)) {
                throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Số điện thoại đã được sử dụng bởi tài khoản khác"
                );
            }
            user.setPhoneNumber(phoneNumber);
        }
        
        // Cập nhật ngày sinh nếu có
        if (dateOfBirth != null && !dateOfBirth.isEmpty()) {
            try {
                // Thử nhiều format ngày tháng
                Date birthdayDate = parseDate(dateOfBirth);
                if (birthdayDate == null) {
                    throw new Exception("Không thể chuyển đổi định dạng ngày");
                }
                
                // Lưu với định dạng chuẩn
                SimpleDateFormat outputFormat = new SimpleDateFormat("dd/MM/yyyy");
                user.setDateOfBirth(outputFormat.format(birthdayDate));
            } catch (Exception e) {
                throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Định dạng ngày sinh không hợp lệ"
                );
            }
        }
        
        return userRepository.save(user);
    }

    /**
     * Cập nhật ảnh đại diện người dùng
     * @param userId ID người dùng
     * @param avatar File ảnh đại diện mới
     * @return Đường dẫn đến ảnh đại diện mới
     * @throws IOException nếu có lỗi khi xử lý file
     * @throws ResponseStatusException nếu có lỗi khác
     */
    public String updateUserAvatar(Integer userId, MultipartFile avatar) throws IOException {
        if (userId == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "ID người dùng không được null");
        }

        User user = readById(userId);
        
        // Kiểm tra file có tồn tại không
        if (avatar == null || avatar.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "File ảnh không được để trống");
        }
        
        // Kiểm tra file có phải là ảnh không
        String contentType = avatar.getContentType();
        if (contentType == null || !contentType.startsWith("image/")) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "File không phải là ảnh");
        }
        
        // Kiểm tra kích thước file
        if (avatar.getSize() > 5 * 1024 * 1024) { // 5MB
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Kích thước ảnh không được vượt quá 5MB");
        }
        
        // Tạo tên file duy nhất
        String fileName = System.currentTimeMillis() + "_" + avatar.getOriginalFilename();
        String uploadDir = "uploads/avatars";
        Path uploadPath = Paths.get(uploadDir);
        
        // Tạo thư mục nếu chưa tồn tại
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }
        
        // Lưu file
        Path filePath = uploadPath.resolve(fileName);
        Files.copy(avatar.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
        
        // Cập nhật URL ảnh đại diện
        String avatarUrl = "/uploads/avatars/" + fileName;
        user.setAvatarUrl(avatarUrl);
        userRepository.save(user);
        
        return avatarUrl;
    }

    /**
     * Đổi mật khẩu người dùng
     * @param userId ID người dùng
     * @param password Mật khẩu hiện tại
     * @param newPassword Mật khẩu mới
     * @param confirmPassword Xác nhận mật khẩu mới
     * @throws ResponseStatusException nếu có lỗi
     */
    public void changeUserPassword(Integer userId, String password, String newPassword, String confirmPassword) {
        if (userId == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "ID người dùng không được null");
        }

        User user = readById(userId);
        
        // Kiểm tra đầu vào
        if (password == null || password.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Mật khẩu hiện tại không được để trống");
        }
        
        if (newPassword == null || newPassword.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Mật khẩu mới không được để trống");
        }
        
        if (confirmPassword == null || confirmPassword.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Xác nhận mật khẩu không được để trống");
        }
        
        // Kiểm tra mật khẩu hiện tại
        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Mật khẩu hiện tại không đúng");
        }
        
        // Kiểm tra mật khẩu mới và xác nhận mật khẩu
        if (!newPassword.equals(confirmPassword)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Mật khẩu mới và xác nhận mật khẩu không khớp");
        }
        
        // Kiểm tra mật khẩu mới có giống mật khẩu cũ không
        if (passwordEncoder.matches(newPassword, user.getPassword())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Mật khẩu mới không được trùng với mật khẩu cũ");
        }
        
        // Kiểm tra độ mạnh của mật khẩu
        if (newPassword.length() < 8) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Mật khẩu mới phải có ít nhất 8 ký tự");
        }
        
        // Cập nhật mật khẩu
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }
    
    /**
     * Hỗ trợ parse ngày tháng với nhiều định dạng
     * @param dateStr Chuỗi ngày tháng
     * @return Date nếu parse thành công, null nếu không parse được
     */
    private Date parseDate(String dateStr) {
        // Các định dạng ngày tháng phổ biến
        String[] dateFormats = {
            "yyyy-MM-dd", "dd/MM/yyyy", "MM/dd/yyyy", 
            "dd-MM-yyyy", "MM-dd-yyyy", "yyyy/MM/dd"
        };
        
        for (String format : dateFormats) {
            try {
                SimpleDateFormat dateFormat = new SimpleDateFormat(format);
                dateFormat.setLenient(false);
                return dateFormat.parse(dateStr);
            } catch (ParseException e) {
                // Thử format tiếp theo
            }
        }
        
        return null; // Không parse được với bất kỳ format nào
    }
}