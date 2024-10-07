package backend.data.repositories;

import backend.data.entities.ProductPrice;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

import java.util.List;

public class ProductPriceRepository {
    @PersistenceContext(unitName = "price_pu")
    private EntityManager em;

    public void save(ProductPrice productPrice) {
        em.persist(productPrice);
    }

    public ProductPrice findById(Long id) {
        return em.find(ProductPrice.class, id);
    }

    public List<ProductPrice> findAll() {
        return em.createQuery("SELECT pp FROM ProductPrice pp", ProductPrice.class).getResultList();
    }

    public void update(ProductPrice productPrice) {
        em.merge(productPrice);
    }

    public void delete(ProductPrice productPrice) {
        em.remove(productPrice);
    }

    public void deleteById(Long id) {
        ProductPrice productPrice = findById(id);
        if (productPrice != null) {
            delete(productPrice);
        }
    }

    public void deleteAll() {
        em.createQuery("DELETE FROM ProductPrice").executeUpdate();
    }

    public List<ProductPrice> findByProductId(Long productId) {
        return em.createQuery("SELECT pp FROM ProductPrice pp WHERE pp.product.id = :productId", ProductPrice.class)
                .setParameter("productId", productId)
                .getResultList();
    }

    public ProductPrice findLatestPriceByProductId(Long productId) {
        return em.createQuery("SELECT pp FROM ProductPrice pp WHERE pp.product.id = :productId ORDER BY pp.applyDate DESC", ProductPrice.class)
                .setParameter("productId", productId)
                .setMaxResults(1)
                .getSingleResult();
    }

    public void close() {
        em.close();
    }
}
