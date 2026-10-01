package murach.vnpay;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import murach.cart.Cart;

// Nhan request "Checkout" tu CartServlet (duoc forward sang, khong phai nguoi dung goi truc tiep),
// tao URL thanh toan VNPay da ky, roi redirect trinh duyet sang trang thanh toan cua VNPay.
@WebServlet("/vnpay")
public class VNPayServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();
        Cart cart = (Cart) session.getAttribute("cart");
        if (cart == null || cart.getItems().isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/index.html");
            return;
        }

        // VNPay yeu cau so tien la so nguyen (don vi VND) nhan 100
        double totalVnd = Math.round(cart.getTotal() * VNPayConfig.EXCHANGE_RATE);
        long amount = (long) (totalVnd * 100);

        String txnRef = VNPayUtil.generateTxnRef();
        session.setAttribute("vnp_TxnRef", txnRef);

        String createDate = VNPayUtil.now();
        String expireDate = VNPayUtil.plusMinutes(createDate, 15);

        Map<String, String> params = new HashMap<>();
        params.put("vnp_Version", "2.1.0");
        params.put("vnp_Command", "pay");
        params.put("vnp_TmnCode", VNPayConfig.TMN_CODE);
        params.put("vnp_Amount", String.valueOf(amount));
        params.put("vnp_CurrCode", "VND");
        params.put("vnp_TxnRef", txnRef);
        params.put("vnp_OrderInfo", "Thanh toan don hang " + txnRef);
        params.put("vnp_OrderType", "other");
        params.put("vnp_Locale", "vn");
        params.put("vnp_ReturnUrl", getReturnUrl(request));
        params.put("vnp_IpAddr", VNPayUtil.getIpAddress(request));
        params.put("vnp_CreateDate", createDate);
        params.put("vnp_ExpireDate", expireDate);

        String hashData = VNPayUtil.buildHashData(params);
        String secureHash = VNPayUtil.hmacSHA512(VNPayConfig.HASH_SECRET, hashData);

        String paymentUrl = VNPayConfig.PAY_URL + "?" + buildQueryString(params)
                + "&vnp_SecureHash=" + secureHash;

        response.sendRedirect(paymentUrl);
    }

    private String getReturnUrl(HttpServletRequest request) {
        if (!VNPayConfig.RETURN_URL.isEmpty()) {
            return VNPayConfig.RETURN_URL;
        }
        // chua cau hinh bien VNPAY_RETURN_URL (vi du khi chay local) -> tu suy ra tu chinh request nay
        return request.getScheme() + "://" + request.getServerName()
                + (request.getServerPort() == 80 || request.getServerPort() == 443
                        ? "" : ":" + request.getServerPort())
                + request.getContextPath() + "/vnpay-return";
    }

    private String buildQueryString(Map<String, String> params) {
        StringBuilder query = new StringBuilder();
        boolean first = true;
        for (Map.Entry<String, String> entry : new java.util.TreeMap<>(params).entrySet()) {
            if (!first) {
                query.append('&');
            }
            query.append(URLEncoder.encode(entry.getKey(), StandardCharsets.UTF_8))
                    .append('=')
                    .append(URLEncoder.encode(entry.getValue(), StandardCharsets.UTF_8));
            first = false;
        }
        return query.toString();
    }
}
