package backend.business;

import backend.business.local.ProductPriceLocal;
import backend.data.entities.ProductPrice;
import backend.data.repositories.ProductPriceRepository;
import jakarta.ejb.Stateless;
import jakarta.inject.Inject;

import java.util.List;

@Stateless
public class ProductPriceBean implements ProductPriceLocal {
    @Inject
    private ProductPriceRepository productPriceRepository;

    @Override
    public void addProductPrice(ProductPrice productPrice) {
        productPriceRepository.save(productPrice);
    }

    @Override
    public void updateProductPrice(ProductPrice productPrice) {
        productPriceRepository.update(productPrice);
    }

    @Override
    public void deleteProductPrice(ProductPrice productPrice) {
        productPriceRepository.delete(productPrice);
    }

    @Override
    public ProductPrice findProductPrice(long productPriceId) {
        return productPriceRepository.findById(productPriceId);
    }

    @Override
    public List<ProductPrice> findAllProductPrices() {
        return productPriceRepository.findAll();
    }

    @Override
    public List<ProductPrice> findProductPricesByProductId(long productId) {
        return productPriceRepository.findByProductId(productId);
    }

    @Override
    public ProductPrice findLatestPriceByProductId(long productId) {
        return productPriceRepository.findLatestPriceByProductId(productId);
    }
}
