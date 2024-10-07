package frontend.controllers;

import backend.data.entities.Product;
import frontend.dtos.ProductDTO;
import frontend.models.ProductModel;
import jakarta.inject.Inject;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

@WebServlet(name = "Controller", urlPatterns = "/controller")
public class Controller extends HttpServlet {
    @Inject
    private ProductModel productModel;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = req.getParameter("action");


        switch (action.toLowerCase()) {
            case "list_products":
                List<ProductDTO> products = productModel.getAllProducts();
                req.setAttribute("products", products);
                req.getRequestDispatcher("views/products.jsp").forward(req, resp);
                break;

            case "add_product":
                req.getRequestDispatcher("views/addProduct.jsp").forward(req, resp);
                break;

            default:
                break;
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = req.getParameter("action");

        switch (action.toLowerCase()) {
            case "save_product":
                ProductDTO product = new ProductDTO();
                product.setName(req.getParameter("name"));
                product.setDescription(req.getParameter("description"));
                product.setImgPath(req.getParameter("imgPath"));
                product.setPrice(Double.parseDouble(req.getParameter("price")));

                productModel.addProduct(product);
                resp.sendRedirect("controller?action=list_products");
                break;

            default:
                break;
        }
    }
}
