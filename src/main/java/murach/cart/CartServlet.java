package murach.cart;

import java.io.IOException;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/cart")
public class CartServlet extends HttpServlet {

    // GET: nguoi dung bam link "Add To Cart" -> cart?productCode=8601 (URL rewriting)
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String productCode = request.getParameter("productCode");
        if (productCode != null) {
            Product product = ProductList.get(productCode);
            if (product != null) {
                getCart(request).addItem(product);
            }
        }

        showCart(request, response);
    }

    // POST: bam Update / Remove Item / Continue Shopping tren trang cart
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        if (request.getParameter("continue") != null) {
            response.sendRedirect(request.getContextPath() + "/index.html");
            return;
        }

        Cart cart = getCart(request);
        for (Product product : ProductList.getAll()) {
            String code = product.getCode();

            if (request.getParameter("update" + code) != null) {
                String qtyParam = request.getParameter("quantity" + code);
                try {
                    int quantity = Integer.parseInt(qtyParam);
                    if (quantity > 0) {
                        cart.updateQuantity(code, quantity);
                    } else {
                        cart.removeItem(code);
                    }
                } catch (NumberFormatException e) {
                    // gia tri nhap khong hop le -> bo qua, giu nguyen so luong cu
                }
            } else if (request.getParameter("remove" + code) != null) {
                cart.removeItem(code);
            }
        }

        showCart(request, response);
    }

    private void showCart(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setAttribute("cart", getCart(request));
        RequestDispatcher dispatcher = request.getRequestDispatcher("/cart.jsp");
        dispatcher.forward(request, response);
    }

    private Cart getCart(HttpServletRequest request) {
        HttpSession session = request.getSession();
        Cart cart = (Cart) session.getAttribute("cart");
        if (cart == null) {
            cart = new Cart();
            session.setAttribute("cart", cart);
        }
        return cart;
    }
}
