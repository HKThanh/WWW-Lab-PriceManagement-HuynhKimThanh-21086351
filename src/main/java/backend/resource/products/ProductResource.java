package backend.resource.products;

import backend.business.local.ProductLocal;
import backend.data.entities.Product;
import jakarta.ejb.EJB;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Response;

@Path("/products")
public class ProductResource {
    @EJB
    private ProductLocal productLocal;

    @GET
    public Response getProducts() {
        return Response.ok(productLocal.findAllProducts()).build();
    }

    @GET
    @Path("/{id}")
    public Response getProduct(@PathParam("id") Long id) {
        return Response.ok(productLocal.findProduct(id)).build();
    }

    @GET
    @Path("/latest")
    public Response getLatestProduct() {
        return Response.ok(productLocal.findLatestProduct()).build();
    }

    @POST
    public Response addProduct(Product product) {
        productLocal.addProduct(product);
        return Response.ok().build();
    }

    @POST
    @Path("update/{id}")
    public Response updateProduct(@PathParam("id") Long id, Product product) {
        productLocal.updateProduct(id, product);
        return Response.ok().build();
    }

    @DELETE
    @Path("/delete/{id}")
    public Response deleteProduct(@PathParam("id") Long id) {
        productLocal.deleteProductById(id);
        return Response.ok().build();
    }
}
