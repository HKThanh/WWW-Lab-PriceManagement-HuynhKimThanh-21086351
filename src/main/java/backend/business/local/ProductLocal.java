package backend.business.local;

import backend.data.entities.Product;
import jakarta.ejb.Local;

import java.util.List;

@Local
public interface ProductLocal {
    void addProduct(Product product);
    void updateProduct(Long id, Product product);
    void deleteProduct(Product product);
    Product findProduct(long productId);
    List<Product> findAllProducts();
    Product findLatestProduct();
    void deleteProductById(Long productId);
}
