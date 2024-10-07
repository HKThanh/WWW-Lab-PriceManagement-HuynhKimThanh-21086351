package backend.business.local;

import backend.data.entities.ProductPrice;
import jakarta.ejb.Local;

import java.util.List;

@Local
public interface ProductPriceLocal {
    void addProductPrice(ProductPrice productPrice);

    void addProductPriceByJson(String json);

    void updateProductPrice(ProductPrice productPrice);

    void deleteProductPrice(ProductPrice productPrice);

    ProductPrice findProductPrice(long productPriceId);

    List<ProductPrice> findAllProductPrices();

    List<ProductPrice> findProductPricesByProductId(long productId);

    ProductPrice findLatestPriceByProductId(long productId);
}
