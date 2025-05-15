package dut.gianguhohi.shoppiefood.utils;

import java.util.regex.Pattern;

public class Validator {
    public static void validateId(int id, String message) {
        if (id <= 0) {
            throw new AppServiceException(message);
        }
    }

    public static void validateString(String str, String message) {
        if (str == null || str.trim().isEmpty()) {
            throw new AppServiceException(message);
        }
    }

    public static void validateObject(Object obj, String message) {
        if (obj == null) {
            throw new AppServiceException(message);
        }
    }

        // Validation patterns
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9+_.-]+@(.+)$");
    private static final Pattern PHONE_PATTERN = Pattern.compile("^[0-9]{10}$");
    private static final Pattern PASSWORD_PATTERN = Pattern.compile("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$");

    public static void validateEmail(String email) {
        validateEmail(email, "Email không được để trống", "Email không hợp lệ");
    }

    public static void validateEmail(String email, String emptyMessage, String invalidMessage) {
        validateString(email, emptyMessage);
        if (!EMAIL_PATTERN.matcher(email).matches()) {
            throw new AppServiceException(invalidMessage);
        }
    }

    public static void validatePhoneNumber(String phoneNumber) {
        validatePhoneNumber(phoneNumber, "Số điện thoại không được để trống", "Số điện thoại phải có 10 chữ số");
    }

    public static void validatePhoneNumber(String phoneNumber, String emptyMessage, String invalidMessage) {
        validateString(phoneNumber, emptyMessage);
        if (!PHONE_PATTERN.matcher(phoneNumber).matches()) {
            throw new AppServiceException(invalidMessage);
        }
    }

    public static void validatePassword(String password) {
        validatePassword(password, "Mật khẩu không được để trống", "Mật khẩu phải có ít nhất 8 ký tự, bao gồm chữ hoa, chữ thường, số và ký tự đặc biệt");
    }

    public static void validatePassword(String password, String emptyMessage, String invalidMessage) {
        validateString(password, emptyMessage);
        if (!PASSWORD_PATTERN.matcher(password).matches()) {
            throw new AppServiceException(invalidMessage);
        }
    }

    public static void validateName(String name) {
        validateName(name, "Tên không được để trống", "Tên phải từ 2 đến 50 ký tự");
    }

    public static void validateName(String name, String emptyMessage, String invalidLengthMessage) {
        validateString(name, emptyMessage);
        if (name.length() < 2 || name.length() > 50) {
            throw new AppServiceException(invalidLengthMessage);
        }
    }
}
