package backend.resource.productprices;

import backend.business.local.ProductPriceLocal;
import backend.data.entities.ProductPrice;
import jakarta.ejb.EJB;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Response;

@Path("/productprices")
public class ProductPriceResource {
    @EJB
    ProductPriceLocal productPriceLocal;

    @GET
    public Response getProductPrices() {
        return Response.ok(productPriceLocal.findAllProductPrices()).build();
    }

    @Path("/{id}")
    @GET
    public Response getProductPrice(@PathParam("id") long id) {
        return Response.ok(productPriceLocal.findProductPrice(id)).build();
    }

//    @POST
//    public Response addProductPrice(ProductPrice productPrice) {
//        productPriceLocal.addProductPrice(productPrice);
//        return Response.ok().build();
//    }

    @POST
    public Response addProductPrice(String json) {
        productPriceLocal.addProductPriceByJson(json);
        return Response.ok().build();
    }

    @PUT
    @Path("/update/{id}")
    public Response updateProductPrice(@PathParam("id") Long id, ProductPrice productPrice) {
        productPriceLocal.updateProductPrice(id, productPrice);
        return Response.ok().build();
    }

    @DELETE
    public Response deleteProductPrice(ProductPrice productPrice) {
        productPriceLocal.deleteProductPrice(productPrice);
        return Response.ok().build();
    }

    @Path("/product/{productId}")
    @GET
    public Response getProductPricesByProductId(@PathParam("productId") long productId) {
        return Response.ok(productPriceLocal.findProductPricesByProductId(productId)).build();
    }

    @Path("/latest/{productId}")
    @GET
    public Response getLatestPriceByProductId(@PathParam("productId") long productId) {
        return Response.ok(productPriceLocal.findLatestPriceByProductId(productId)).build();
    }

    @Path("/deleteAll/{productId}")
    @DELETE
    public Response deleteAllByProductId(@PathParam("productId") Long productId) {
        productPriceLocal.deleteAllByProductId(productId);
        return Response.ok().build();
    }

    @GET
    @Path("/old/{productId}")
    public Response getOldPriceByProductId(@PathParam("productId") long productId) {
        return Response.ok(productPriceLocal.findOldPriceByProductId(productId)).build();
    }
}
