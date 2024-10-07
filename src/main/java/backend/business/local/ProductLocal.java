package backend.business.local;

import backend.data.entities.Product;
import jakarta.ejb.Local;

import java.util.List;

@Local
public interface ProductLocal {
    public void addProduct(Product product);
    public void updateProduct(Product product);
    public void deleteProduct(Product product);
    public Product findProduct(long productId);
    public List<Product> findAllProducts();
    Product findLatestProduct();
}
