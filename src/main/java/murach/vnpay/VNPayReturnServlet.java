package murach.vnpay;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import murach.cart.Cart;

// VNPay redirect trinh duyet nguoi dung ve day (GET) sau khi thanh toan xong, kem theo cac
// tham so vnp_* da duoc VNPay ky bang vnp_SecureHash. Servlet nay xac thuc lai chu ky truoc
// khi tin bat ky ket qua nao, de tranh nguoi dung tu sua query string gia mao thanh cong.
@WebServlet("/vnpay-return")
public class VNPayReturnServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        Map<String, String> params = new HashMap<>();
        for (Map.Entry<String, String[]> entry : request.getParameterMap().entrySet()) {
            if (entry.getKey().startsWith("vnp_")) {
                params.put(entry.getKey(), entry.getValue()[0]);
            }
        }

        String receivedHash = params.remove("vnp_SecureHash");
        params.remove("vnp_SecureHashType");

        String hashData = VNPayUtil.buildHashData(params);
        String computedHash = VNPayUtil.hmacSHA512(VNPayConfig.HASH_SECRET, hashData);

        HttpSession session = request.getSession();
        Cart cart = (Cart) session.getAttribute("cart");
        String expectedTxnRef = (String) session.getAttribute("vnp_TxnRef");

        boolean signatureValid = receivedHash != null && receivedHash.equalsIgnoreCase(computedHash);
        boolean txnRefMatches = expectedTxnRef != null && expectedTxnRef.equals(params.get("vnp_TxnRef"));
        boolean paymentSuccess = signatureValid && txnRefMatches
                && "00".equals(params.get("vnp_ResponseCode"));

        request.setAttribute("cart", cart);
        request.setAttribute("vnpaySuccess", paymentSuccess);
        request.setAttribute("vnpTxnRef", params.get("vnp_TxnRef"));
        request.setAttribute("vnpTransactionNo", params.get("vnp_TransactionNo"));
        request.setAttribute("vnpBankCode", params.get("vnp_BankCode"));
        request.setAttribute("vnpResponseCode", params.get("vnp_ResponseCode"));

        if (!signatureValid) {
            request.setAttribute("vnpayError", "Chu ky khong hop le, khong the xac thuc ket qua thanh toan.");
        } else if (!txnRefMatches) {
            request.setAttribute("vnpayError", "Ma don hang khong khop voi phien hien tai.");
        } else if (!paymentSuccess) {
            request.setAttribute("vnpayError",
                    "Thanh toan khong thanh cong (ma loi VNPay: " + params.get("vnp_ResponseCode") + ").");
        }

        RequestDispatcher dispatcher = request.getRequestDispatcher("/checkout.jsp");
        dispatcher.forward(request, response);

        if (paymentSuccess) {
            // thanh toan xong -> ket thuc session, lan sau vao lai la gio hang moi hoan toan
            session.invalidate();
        }
    }
}
