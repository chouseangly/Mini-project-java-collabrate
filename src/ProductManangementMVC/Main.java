package ProductManangementMVC;

import ProductManangementMVC.Model.ProjectDAOImpl;

public class Main {
    public static void main(String[] args) {
        ProjectDAOImpl dao = new ProjectDAOImpl();
        System.out.println("product List:"   +   dao.getAllProduct());

    }
}
