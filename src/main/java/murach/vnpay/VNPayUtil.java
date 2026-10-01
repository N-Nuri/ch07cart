package murach.vnpay;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.TreeMap;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import jakarta.servlet.http.HttpServletRequest;

// Cac ham tien ich theo dung quy dinh ky/xac thuc du lieu cua VNPay
// (xem tai lieu tich hop tai https://sandbox.vnpayment.vn/apis/docs/huong-dan-tich-hop/)
public class VNPayUtil {
    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
    private static final ZoneId VN_ZONE = ZoneId.of("Etc/GMT-7");

    public static String now() {
        return ZonedDateTime.now(VN_ZONE).format(DATE_FORMAT);
    }

    public static String plusMinutes(String createDate, int minutes) {
        LocalDateTime time = LocalDateTime.parse(createDate, DATE_FORMAT);
        return time.plusMinutes(minutes).format(DATE_FORMAT);
    }

    // sinh ma don hang duy nhat (dung lam vnp_TxnRef)
    public static String generateTxnRef() {
        return String.valueOf(System.currentTimeMillis());
    }

    public static String getIpAddress(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null || ip.isEmpty()) {
            ip = request.getRemoteAddr();
        } else {
            // header co the chua nhieu IP cach nhau boi dau phay, lay IP dau tien
            ip = ip.split(",")[0].trim();
        }
        return ip;
    }

    // tao chuoi hash data va query string tu danh sach tham so, theo dung thu tu alphabet cua ten truong
    public static String hmacSHA512(String key, String data) {
        try {
            Mac hmac512 = Mac.getInstance("HmacSHA512");
            SecretKeySpec secretKey = new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), "HmacSHA512");
            hmac512.init(secretKey);
            byte[] result = hmac512.doFinal(data.getBytes(StandardCharsets.UTF_8));
            StringBuilder sb = new StringBuilder();
            for (byte b : result) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException | InvalidKeyException e) {
            throw new RuntimeException("Unable to compute VNPay HMAC-SHA512 signature", e);
        }
    }

    // ghep cac tham so (da sap xep theo ten) thanh chuoi "key1=value1&key2=value2..." co URL-encode gia tri
    public static String buildHashData(Map<String, String> params) {
        TreeMap<String, String> sorted = new TreeMap<>(params);
        StringBuilder hashData = new StringBuilder();
        boolean first = true;
        for (Map.Entry<String, String> entry : sorted.entrySet()) {
            String value = entry.getValue();
            if (value == null || value.isEmpty()) {
                continue;
            }
            if (!first) {
                hashData.append('&');
            }
            hashData.append(entry.getKey()).append('=')
                    .append(URLEncoder.encode(value, StandardCharsets.UTF_8));
            first = false;
        }
        return hashData.toString();
    }
}
