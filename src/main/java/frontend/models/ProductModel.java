package frontend.models;

import backend.data.entities.ProductPrice;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import frontend.dtos.ProductDTO;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;

public class ProductModel {
    public static final String BASE_URL = "http://localhost:8080/WWW_Lab_PriceManagement_HuynhKimThanh_21086351-1.0-SNAPSHOT/api/";

    public List<ProductDTO> getAllProducts() {
        List<ProductDTO> products = null;
        try (Client client = ClientBuilder.newClient()) {
            WebTarget target = client.target(BASE_URL).path("products");

            String jsonProduct = target.request(MediaType.APPLICATION_JSON)
                    .get()
                    .readEntity(String.class);

            ObjectMapper mapper = new ObjectMapper();
            mapper.registerModule(new JavaTimeModule());

            products = mapper.readValue(jsonProduct, new TypeReference<List<ProductDTO>>() {});

            for (ProductDTO product : products) {
                WebTarget priceTarget = client.target(BASE_URL).path("productprices/latest/" + product.getId());
                String jsonProductPrice = priceTarget.request(MediaType.APPLICATION_JSON)
                        .get()
                        .readEntity(String.class);

                ProductPrice productPrice = mapper.readValue(jsonProductPrice, ProductPrice.class);
                product.setPrice(productPrice.getValue());
            }
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        return products;
    }
}
