package murach.vnpay;

// Cau hinh doc tu bien moi truong, khong hardcode secret trong code/git.
// TMN Code + Hash Secret lay tu tai khoan sandbox tai https://sandbox.vnpayment.vn
public class VNPayConfig {
    public static final String TMN_CODE = System.getenv().getOrDefault("VNPAY_TMN_CODE", "");
    public static final String HASH_SECRET = System.getenv().getOrDefault("VNPAY_HASH_SECRET", "");
    public static final String PAY_URL = System.getenv().getOrDefault(
            "VNPAY_PAY_URL", "https://sandbox.vnpayment.vn/paymentv2/vpcpay.html");

    // URL day du tro ve servlet /vnpay-return cua chinh app nay, vi du:
    // local:  http://localhost:8080/ch07cart/vnpay-return
    // render: https://ch07cart-lh3o.onrender.com/vnpay-return
    public static final String RETURN_URL = System.getenv().getOrDefault("VNPAY_RETURN_URL", "");

    // Gia san pham trong app dang la USD (theo sach Murach), nhung VNPay chi nhan VND.
    // Dung ty gia quy doi tam thoi de co so tien VND hop le khi test sandbox.
    public static final double EXCHANGE_RATE = Double.parseDouble(
            System.getenv().getOrDefault("VNPAY_EXCHANGE_RATE", "25000"));
}
