package backend.data.repositories;

import backend.data.entities.Product;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

import java.util.List;

public class ProductRepository {
    @PersistenceContext(unitName = "price_pu")
    private EntityManager em;

    public void save(Product product) {
        em.persist(product);
    }

    public Product findById(Long id) {
        return em.find(Product.class, id);
    }

    public void update(Long id, Product product) {
        Product existingProduct = findById(id);
        if (existingProduct != null) {
            existingProduct.setName(product.getName());
            existingProduct.setDescription(product.getDescription());
            existingProduct.setImgPath(product.getImgPath());
            em.merge(existingProduct);
        }
    }

    public void delete(Product product) {
        em.remove(product);
    }

    public void deleteById(Long id) {
        Product product = findById(id);
        if (product != null) {
            delete(product);
        }
    }

    public void deleteAll() {
        em.createQuery("DELETE FROM Product").executeUpdate();
    }

    public List<Product> findAll() {
        return em.createQuery("SELECT p FROM Product p", Product.class).getResultList();
    }

    public Product findLatestProduct() {
        return em.createQuery("SELECT p FROM Product p ORDER BY p.id DESC", Product.class).setMaxResults(1).getSingleResult();
    }

    public void close() {
        em.close();
    }
}
