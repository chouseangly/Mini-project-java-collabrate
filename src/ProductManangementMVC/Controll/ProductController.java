package ProductManangementMVC.Controll;

import ProductManangementMVC.Model.Product;
import ProductManangementMVC.Model.ProjectDAO;
import ProductManangementMVC.View.ProductView;

import java.util.List;

public class ProductController {
  private final ProjectDAO productDAO;
  private final ProductView productView;
  public ProductController(ProjectDAO projectDAO, ProductView productView) {
      this.productDAO = projectDAO;
      this.productView = productView;
  }
  public void AddProduct(Product product) {
      this.productDAO.addProduct(product);
      productView.showMessage("Product Added");

  }

  public void showProducts()
  {
      List<Product> products = productDAO.getAllProduct();
      productView.displayProducts(products);
  }

}
