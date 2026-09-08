package murach.cart;

import java.util.LinkedHashMap;
import java.util.Collection;
import java.util.Map;

// danh sach san pham co san (thay cho database that)
public class ProductList {
    private static final Map<String, Product> products = new LinkedHashMap<>();

    static {
        products.put("8601", new Product("8601", "86 (the band) - True Life Songs and Pictures", 14.95));
        products.put("pf01", new Product("pf01", "Paddlefoot - The first CD", 12.95));
        products.put("pf02", new Product("pf02", "Paddlefoot - The second CD", 14.95));
        products.put("jr01", new Product("jr01", "Joe Rut - Genuine Wood Grained Finish", 14.95));
    }

    public static Collection<Product> getAll() {
        return products.values();
    }

    public static Product get(String code) {
        return products.get(code);
    }
}
