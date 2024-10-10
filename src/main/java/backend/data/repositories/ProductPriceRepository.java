package backend.data.repositories;

import backend.data.entities.Product;
import backend.data.entities.ProductPrice;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.json.Json;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

import java.io.StringReader;
import java.time.LocalDate;
import java.util.List;

public class ProductPriceRepository {
    @PersistenceContext(unitName = "price_pu")
    private EntityManager em;

    public void save(ProductPrice productPrice) {
        em.persist(productPrice);
    }

    public void saveByJson(String json) {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());

        try {
            JsonNode node = mapper.readTree(json);
            ProductPrice productPrice = new ProductPrice();
            productPrice.setValue(node.get("value").asDouble());
            String applyDateStr = node.get("applyDate").asText();
            LocalDate applyDate = LocalDate.parse(applyDateStr);
            productPrice.setApplyDate(applyDate);
            Product product = em.find(Product.class, node.get("product").get("id").asLong());
            productPrice.setProduct(product);
            Byte state = Integer.parseInt(node.get("state").asText()) == 1 ? (byte) 1 : (byte) 0;
            productPrice.setState(state);

            save(productPrice);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
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

    public void deleteAllByProductId(Long productId) {
        em.createQuery("DELETE FROM ProductPrice pp WHERE pp.product.id = :productId")
                .setParameter("productId", productId)
                .executeUpdate();
    }

    public void close() {
        em.close();
    }
}
