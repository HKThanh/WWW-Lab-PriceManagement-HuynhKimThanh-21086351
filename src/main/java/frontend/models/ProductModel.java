package frontend.models;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import frontend.dtos.ProductDTO;
import jakarta.json.Json;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class ProductModel {
    public static final String BASE_URL = "http://localhost:8080/WWW_Lab_PriceManagement_HuynhKimThanh_21086351-1.0-SNAPSHOT/api/";

    public List<ProductDTO> getAllProducts() {
        List<ProductDTO> products = null;
        try (Client client = ClientBuilder.newClient()) {
            String jsonProduct = getRequest("products");

            ObjectMapper mapper = new ObjectMapper();
            mapper.registerModule(new JavaTimeModule());

            products = mapper.readValue(jsonProduct, new TypeReference<List<ProductDTO>>() {});

            for (ProductDTO product : products) {
                String jsonProductPrice = getRequest("productprices/latest/" + product.getId());

                JsonNode rootNode = mapper.readTree(jsonProductPrice);
                JsonNode productPrice = rootNode.get("value");
                product.setPrice(productPrice.asDouble());
            }
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        return products;
    }

    public void addProduct(ProductDTO product) {
        try (Client client = ClientBuilder.newClient()) {
            WebTarget target = client.target(BASE_URL).path("products");

            ObjectMapper mapper = new ObjectMapper();
            mapper.registerModule(new JavaTimeModule());

            String json = mapper.writeValueAsString(product);

            JsonNode rootNode = mapper.readTree(json);
            String id = rootNode.get("id").asText();
            String name = rootNode.get("name").asText();
            String description = rootNode.get("description").asText();
            String imgPath = rootNode.get("imgPath").asText();
            String jsonProduct = "{\"id\":\"" + id
                    + "\",\"name\":\"" + name
                    + "\",\"description\":\"" + description
                    + "\",\"imgPath\":\"" + imgPath + "\"}";

            try (Response productRes = target.request(MediaType.APPLICATION_JSON)
                    .post(Entity.json(jsonProduct))) {
                double value = product.getPrice();
                String jsonNewestProduct = getLastestProduct();

                String applyDate = LocalDateTime.now().toString();

                String note = "Initial price";
                Byte state = 1;

                WebTarget priceTarget = client.target(BASE_URL).path("productprices");
                String jsonProductPrice = String.format(
                        "{\"applyDate\":\"%s\",\"value\":%f,\"note\":\"%s\",\"state\":%d,\"product\":%s}",
                        applyDate, value, note, state, jsonNewestProduct
                );
                priceTarget.request(MediaType.APPLICATION_JSON)
                        .post(Entity.json(jsonProductPrice));
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public String getLastestProduct () {
        String jsonProduct = null;
        try {
            String json = getRequest("products/latest");

            ObjectMapper mapper = new ObjectMapper();
            mapper.registerModule(new JavaTimeModule());

            JsonNode rootNode = mapper.readTree(json);

            String id = rootNode.get("id").asText();
            String name = rootNode.get("name").asText();
            String description = rootNode.get("description").asText();
            String imgPath = rootNode.get("imgPath").asText();
            jsonProduct = "{\"id\":\"" + id
                    + "\",\"name\":\"" + name
                    + "\",\"description\":\"" + description
                    + "\",\"imgPath\":\"" + imgPath + "\"}";
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        return jsonProduct;
    }

    public String getRequest(String path) {
        String json = null;
        try (Client client = ClientBuilder.newClient()) {
            WebTarget target = client.target(BASE_URL).path(path);

            json = target.request(MediaType.APPLICATION_JSON)
                    .get()
                    .readEntity(String.class);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return json;
    }

    public void deleteProduct(Long id) {
        try (Client client = ClientBuilder.newClient()) {
            WebTarget targetProductPrices = client.target(BASE_URL).path("productprices/deleteAll/" + id);
            Response responseProductPrices = targetProductPrices.request(MediaType.APPLICATION_JSON).delete();

            if (responseProductPrices.getStatus() != 200) {
                throw new RuntimeException("Failed to delete product prices: HTTP error code : " + responseProductPrices.getStatus());
            }

            WebTarget targetProduct = client.target(BASE_URL).path("products/delete/" + id);
            Response responseProduct = targetProduct.request(MediaType.APPLICATION_JSON).delete();

            if (responseProduct.getStatus() != 200) {
                throw new RuntimeException("Failed to delete product: HTTP error code : " + responseProduct.getStatus());
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void updateProduct(String id, ProductDTO product) {
        try (Client client = ClientBuilder.newClient()) {
            WebTarget target = client.target(BASE_URL).path("products/update/" + id);

            ObjectMapper mapper = new ObjectMapper();
            mapper.registerModule(new JavaTimeModule());

            String json = mapper.writeValueAsString(product);
            JsonNode rootNode = mapper.readTree(json);
            String jsonProduct = "{\"id\":\"" + id
                    + "\",\"name\":\"" + rootNode.get("name").asText()
                    + "\",\"description\":\"" + rootNode.get("description").asText()
                    + "\",\"imgPath\":\"" + rootNode.get("imgPath").asText() + "\"}";

            target.request(MediaType.APPLICATION_JSON)
                    .post(Entity.json(jsonProduct));

            ProductDTO oldProduct = getProductById(id);
            double oldValue = oldProduct.getPrice();
            if (product.getPrice() != oldValue) {
                updateProductPrice(id, client, jsonProduct, product, oldValue, mapper);
            }
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public ProductDTO getProductById(String id) {
        ProductDTO product = null;
        try (Client client = ClientBuilder.newClient()) {
            String json = getRequest("products/" + id);

            ObjectMapper mapper = new ObjectMapper();
            mapper.registerModule(new JavaTimeModule());

            String jsonPrice = getRequest("productprices/latest/" + id);
            JsonNode rootNode = mapper.readTree(jsonPrice);
            JsonNode productPrice = rootNode.get("value");

            product = mapper.readValue(json, ProductDTO.class);
            product.setPrice(productPrice.asDouble());

        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        return product;
    }

    public void updateProductPrice(String productId, Client client, String jsonProduct, ProductDTO product, double oldValue, ObjectMapper mapper) throws JsonProcessingException {
        WebTarget targetOldPrice = client.target(BASE_URL).path("productprices/old/" + productId);
        String jsonOldPrice = targetOldPrice.request(MediaType.APPLICATION_JSON)
                .get()
                .readEntity(String.class);

        JsonNode oldPriceNode = mapper.readTree(jsonOldPrice);
        String oldPriceState = oldPriceNode.get("state").asText();

        if (oldPriceState.equals("1")) {
            String applyDate = oldPriceNode.get("applyDate").asText();
            String note = "Update price state";
            Byte state = 0;

            WebTarget priceTarget = client.target(BASE_URL).path("productprices/update/" + oldPriceNode.get("id").asText());
            String jsonProductPrice = String.format(
                    "{\"applyDate\":\"%s\",\"value\":%f,\"note\":\"%s\",\"state\":%d,\"product\":%s}",
                    applyDate, oldValue, note, state, jsonProduct
            );
            priceTarget.request(MediaType.APPLICATION_JSON)
                    .post(Entity.json(jsonProductPrice));
        }

        String applyDate = LocalDateTime.now().toString();

        String note = "Update price";
        Byte state = 1;

        WebTarget priceTarget = client.target(BASE_URL).path("productprices");
        String jsonProductPrice = String.format(
                "{\"applyDate\":\"%s\",\"value\":%f,\"note\":\"%s\",\"state\":%d,\"product\":%s}",
                applyDate, product.getPrice(), note, state, jsonProduct
        );
        priceTarget.request(MediaType.APPLICATION_JSON)
                .post(Entity.json(jsonProductPrice));
    }
}
