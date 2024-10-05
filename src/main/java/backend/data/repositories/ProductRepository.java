package backend.data.repositories;

import backend.data.entities.Product;
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

    public void update(Product product) {
        em.merge(product);
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

    public void close() {
        em.close();
    }
}
