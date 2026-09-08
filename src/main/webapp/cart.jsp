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
    <h1>Your cart</h1>

    <form action="cart" method="post">
        <table>
            <tr>
                <th>Quantity</th>
                <th>Description</th>
                <th>Price</th>
                <th>Amount</th>
                <th></th>
            </tr>
            <c:forEach var="item" items="${cart.items}">
                <tr>
                    <td>
                        <input type="text" size="2" name="quantity${item.product.code}" value="${item.quantity}">
                        <input type="submit" name="update${item.product.code}" value="Update">
                    </td>
                    <td>${item.product.description}</td>
                    <td><fmt:formatNumber value="${item.product.price}" type="currency"/></td>
                    <td><fmt:formatNumber value="${item.amount}" type="currency"/></td>
                    <td><input type="submit" name="remove${item.product.code}" value="Remove Item"></td>
                </tr>
            </c:forEach>
        </table>

        <p><strong>To change the quantity</strong>, enter the new quantity and click on the Update button.</p>

        <input type="submit" name="continue" value="Continue Shopping"><br>
        <input type="submit" name="checkout" value="Checkout">
    </form>
</body>
</html>
