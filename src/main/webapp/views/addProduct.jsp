<%--
  Created by IntelliJ IDEA.
  User: Thanh
  Date: 10/7/2024
  Time: 10:14 AM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Add Product</title>
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

        input[type="text"], input[type="number"] {
            width: 100%;
            padding: 5px;
            margin: 5px 0;
        }

        input[type="submit"] {
            width: 200px;
            font-size: 1.5em;
            padding: 5px 10px;
            background-color: #4CAF50;
            color: white;
            border: none;
            cursor: pointer;
        }
    </style>
</head>
<body>
    <h1>ADDING NEW PRODUCT</h1>
    <form action="controller?action=save_product" method="post">
        <table>
            <tr>
                <td>Name</td>
                <td><input type="text" name="name" required></td>
            </tr>
            <tr>
                <td>Description</td>
                <td><input type="text" name="description" required></td>
            </tr>
            <tr>
                <td>Image Path</td>
                <td><input type="text" name="imgPath" required></td>
            </tr>
            <tr>
                <td>Price</td>
                <td><input type="number" name="price" required></td>
            </tr>
            <tr>
                <td colspan="2" style="text-align: center">
                    <input type="submit" value="Add">
                </td>
            </tr>
        </table>
    </form>
</body>
</html>
