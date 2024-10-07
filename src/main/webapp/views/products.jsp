<%@ page import="java.util.List" %>
<%@ page import="frontend.dtos.ProductDTO" %><%--
  Created by IntelliJ IDEA.
  User: Thanh
  Date: 10/5/2024
  Time: 9:47 AM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Products</title>
    <style>
        h1 {
            font-size: 3em;
            font-weight: 700;
            text-align: center;
        }
        table {
            width: 100%;
            border-collapse: collapse;
        }

        table, th, td {
            border: 1px solid black;
        }

        th, td {
            padding: 10px;
            text-align: left;
        }

        th {
            background-color: #f2f2f2;
        }

        .error {
            font-size: 1.5em;
            font-weight: 700;
            color: red;
        }
    </style>
</head>
<body>
    <h1>WELCOME,</h1>
    <h1>List Products</h1>
    <table>
        <tr>
            <th>Id</th>
            <th>Name</th>
            <th>Description</th>
            <th>imgPath</th>
            <th>Price</th>
        </tr>
        <%
            List<ProductDTO> products = (List<ProductDTO>) request.getAttribute("products");
            if (products == null) {
                out.println("<tr><td colspan='5' class=\"error\">No products found</td></tr>");
            } else {
                for (ProductDTO product : products) {
        %>
            <tr>
                <td><%= product.getId() %></td>
                <td><%= product.getName() %></td>
                <td><%= product.getDescription() %></td>
                <td><%= product.getImgPath() %></td>
                <td><%= product.getPrice() %></td>
            </tr>
        <%
                }
            }
        %>
</body>
</html>
