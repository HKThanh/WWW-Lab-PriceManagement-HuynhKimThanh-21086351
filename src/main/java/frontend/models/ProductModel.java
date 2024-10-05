package frontend.models;

import backend.data.entities.Product;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.GenericType;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;

public class ProductModel {
    public static final String BASE_URL = "http://localhost:8080/backend/api";

    public List<Product> getAllProducts() {
        List<Product> products = null;
        try (Client client = ClientBuilder.newClient()) {
            WebTarget target = client.target(BASE_URL).path("products");
            String json = target
                    .request(MediaType.APPLICATION_JSON)
                    .get()
                    .readEntity(String.class);

            ObjectMapper mapper = new ObjectMapper();
            products = mapper.readValue(json, new TypeReference<List<Product>>() {
            });
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        return products;
    }
}
