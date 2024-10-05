package backend.resource.products;

import backend.business.local.ProductLocal;
import backend.data.entities.Product;
import jakarta.ejb.EJB;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
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

    @POST
    public Response addProduct(Product product) {
        productLocal.addProduct(product);
        return Response.ok().build();
    }
}
