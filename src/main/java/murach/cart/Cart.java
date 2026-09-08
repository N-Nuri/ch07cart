package murach.cart;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

// gio hang - luu trong session, dung chung cho moi tab cua cung 1 nguoi dung
public class Cart implements Serializable {
    private List<CartItem> items = new ArrayList<>();

    public void addItem(Product product) {
        for (CartItem item : items) {
            if (item.getProduct().getCode().equals(product.getCode())) {
                item.setQuantity(item.getQuantity() + 1);
                return;
            }
        }
        items.add(new CartItem(product, 1));
    }

    public void updateQuantity(String productCode, int quantity) {
        for (CartItem item : items) {
            if (item.getProduct().getCode().equals(productCode)) {
                item.setQuantity(quantity);
                return;
            }
        }
    }

    // neu so luong dang > 1 thi tru di 1, chi xoa han dong khi so luong ve 1
    public void removeItem(String productCode) {
        for (int i = 0; i < items.size(); i++) {
            CartItem item = items.get(i);
            if (item.getProduct().getCode().equals(productCode)) {
                if (item.getQuantity() > 1) {
                    item.setQuantity(item.getQuantity() - 1);
                } else {
                    items.remove(i);
                }
                return;
            }
        }
    }

    // xoa han dong, khong quan tam quantity - dung khi nguoi dung tu go so luong = 0 roi bam Update
    public void deleteItem(String productCode) {
        items.removeIf(item -> item.getProduct().getCode().equals(productCode));
    }

    public List<CartItem> getItems() {
        return items;
    }
}
