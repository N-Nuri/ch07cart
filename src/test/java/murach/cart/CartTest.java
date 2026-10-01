package murach.cart;

import junit.framework.TestCase;

public class CartTest extends TestCase {

    public void testRemoveItemDecrementsWhenQuantityAboveOne() {
        Cart cart = new Cart();
        Product cd = new Product("8601", "86 (the band)", 14.95);
        cart.addItem(cd);
        cart.addItem(cd); // quantity = 2

        cart.removeItem("8601");

        assertEquals(1, cart.getItems().size());
        assertEquals(1, cart.getItems().get(0).getQuantity());
        // sau khi tru: quantity phai con 1, khong duoc bien mat khoi gio hang
    }

    public void testRemoveItemDeletesRowWhenQuantityIsOne() {
        Cart cart = new Cart();
        Product cd = new Product("jr01", "Joe Rut", 14.95);
        cart.addItem(cd); // quantity = 1

        cart.removeItem("jr01");

        assertEquals(0, cart.getItems().size());
        // quantity dang la 1 -> remove phai xoa han dong, khong con item nao
    }

    public void testDeleteItemAlwaysRemovesRegardlessOfQuantity() {
        Cart cart = new Cart();
        Product cd = new Product("8601", "86 (the band)", 14.95);
        cart.addItem(cd);
        cart.addItem(cd);
        cart.addItem(cd); // quantity = 3

        cart.deleteItem("8601");

        assertEquals(0, cart.getItems().size());
        // dung khi go so luong = 0 roi bam Update - phai mat han, khong duoc chi tru 1
    }

    public void testGetTotalSumsAllItemAmounts() {
        Cart cart = new Cart();
        Product a = new Product("8601", "86 (the band)", 14.95);
        Product b = new Product("pf01", "Paddlefoot", 12.95);
        cart.addItem(a);
        cart.addItem(b);
        cart.addItem(b); // pf01 quantity = 2

        // 14.95 + (12.95 * 2) = 40.85
        assertEquals(40.85, cart.getTotal(), 0.001);
    }

    public void testRemoveItemDoesNotAffectOtherProducts() {
        Cart cart = new Cart();
        Product a = new Product("8601", "86 (the band)", 14.95);
        Product b = new Product("pf01", "Paddlefoot", 12.95);
        cart.addItem(a);
        cart.addItem(b);

        cart.removeItem("8601");

        assertEquals(1, cart.getItems().size());
        assertEquals("pf01", cart.getItems().get(0).getProduct().getCode());
    }
}
