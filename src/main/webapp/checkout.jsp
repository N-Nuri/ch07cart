<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <title>Murach's Java Servlets and JSP</title>
    <link rel="stylesheet" href="styles/main.css" type="text/css"/>
</head>
<body>
    <c:choose>
        <c:when test="${not empty vnpayError}">
            <h1>Payment failed</h1>
            <p style="color:red;">${vnpayError}</p>
            <p><a href="cart">Back to cart</a> to try again, or <a href="index.html">continue shopping</a>.</p>
        </c:when>
        <c:otherwise>
            <h1>Thank you for your order!</h1>
            <c:if test="${not empty vnpTxnRef}">
                <p>Payment via VNPay was successful.<br>
                    Order code: <strong>${vnpTxnRef}</strong><br>
                    <c:if test="${not empty vnpTransactionNo}">
                        VNPay transaction no: <strong>${vnpTransactionNo}</strong><br>
                    </c:if>
                    <c:if test="${not empty vnpBankCode}">
                        Bank: <strong>${vnpBankCode}</strong><br>
                    </c:if>
                </p>
            </c:if>
            <p>Here is a summary of what you ordered:</p>

            <table>
        <tr>
            <th>Quantity</th>
            <th>Description</th>
            <th>Amount</th>
        </tr>
        <c:forEach var="item" items="${cart.items}">
            <tr>
                <td>${item.quantity}</td>
                <td>${item.product.description}</td>
                <td><fmt:formatNumber value="${item.amount}" type="currency"/></td>
            </tr>
        </c:forEach>
            </table>

            <p><strong>Total: <fmt:formatNumber value="${cart.total}" type="currency"/></strong></p>

            <p><a href="index.html">Continue Shopping</a></p>
        </c:otherwise>
    </c:choose>
</body>
</html>
