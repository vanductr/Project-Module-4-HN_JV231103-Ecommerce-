package rikkei.academy.validator;

public class DataValidator {

    // Phương thức kiểm tra tính hợp lệ của mật khẩu
    public static boolean isValidPassword(String password) {
        // Kiểm tra xem mật khẩu có đủ dài (lớn hơn 8 kí tự) không
        if (password.length() < 8) {
            return false;
        }

        // Kiểm tra xem mật khẩu có chứa ít nhất một chữ in hoa không
        boolean containsUpperCase = false;
        for (char c : password.toCharArray()) {
            if (Character.isUpperCase(c)) {
                containsUpperCase = true;
                break;
            }
        }
        if (!containsUpperCase) {
            return false;
        }

        // Kiểm tra xem mật khẩu có chứa ít nhất một số không
        boolean containsDigit = false;
        for (char c : password.toCharArray()) {
            if (Character.isDigit(c)) {
                containsDigit = true;
                break;
            }
        }
        if (!containsDigit) {
            return false;
        }

        return true; // Mật khẩu hợp lệ nếu vượt qua tất cả các kiểm tra
    }
}
