package ProductManangementMVC.Model;
import org.nocrala.tools.texttablefmt.BorderStyle;
import org.nocrala.tools.texttablefmt.CellStyle;
import org.nocrala.tools.texttablefmt.ShownBorders;
import org.nocrala.tools.texttablefmt.Table;
import java.io.*;
import java.sql.*;
import java.sql.Date;
import java.time.LocalDate;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;


public class ProductDAOImpl implements ProductDAO {
    private List<Product> tempProductList = new ArrayList<>();
    private List<Product> productToUpdate = new ArrayList<>();
    Validate validate = new Validate();
    public final String GREEN = "\u001B[32m";
    public final String RESET = "\u001B[0m";
    public final String RED = "\u001B[31m";


    public int getSetRow() {
        String query = "SELECT * FROM setrow";
        try (Connection con = Connect.getConnection();
             Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            throw new RuntimeException(RED+"Failed to retrieve rows per page from database."+RESET,e);
        }
        return 1;
    }


    public void setRow(int row) {
        String query = "UPDATE setrow SET row = ?";
        try (Connection con = Connect.getConnection();
             PreparedStatement ps = con.prepareStatement(query)) {
            ps.setInt(1, row);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(RED+"Failed to update rows per page in database."+RESET, e);
        }
    }

    @Override
    public List<Product> getAllProduct() {
        List<Product> products = new ArrayList<>();
        String query = "SELECT * FROM stockmanagement order by id";
        try (Connection conn = Connect.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                products.add(new Product(
                        rs.getInt(1),
                        rs.getString(2),
                        rs.getDouble(3),
                        rs.getInt(4),
                        rs.getDate(5) != null ? rs.getDate(5).toLocalDate() : LocalDate.now()
                ));
            }
        } catch (SQLException e) {
            throw new RuntimeException(RED+"Failed to retrieve products from database."+RESET, e);
        }
        return products;
    }

    @Override
    public Product getStockItemByID(int ID) {
        String query = "SELECT * FROM stockmanagement WHERE id = ?";
        try (Connection con = Connect.getConnection();
             PreparedStatement ps = con.prepareStatement(query)) {
            ps.setInt(1, ID);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Product(
                            rs.getInt("id"),
                            rs.getString("name"),
                            rs.getDouble("unit_price"),
                            rs.getInt("stock_quantity"),
                            rs.getDate("imported_date").toLocalDate()
                    );
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(RED+"Failed to retrieve product by ID from database."+RESET, e);
        }
        return null;
    }

    @Override
    public List<Product> addProductToDB(List<Product> products) {
        String insertQuery = "INSERT INTO stockmanagement(name,unitprice , qty, import_date) VALUES (?, ?, ?, ?)";

        try (Connection con = Connect.getConnection();
             PreparedStatement ps = con.prepareStatement(insertQuery)) {


            for (Product product : products) {
                ps.setString(1, product.getName());
                ps.setDouble(2, product.getUnitPrice());
                ps.setInt(3, product.getQty());
                ps.setDate(4, Date.valueOf(product.getDate()));
                ps.executeUpdate();
            }


            int[] result = ps.executeBatch();
            products.clear();

        } catch (SQLException e) {
            System.err.println(RED+"Error inserting products: "+ e.getMessage()+RESET );
            e.printStackTrace();

            return new ArrayList<>();
        }

        return getAllProductsFromDB();
    }

    public List<Product> getAllProductsFromDB() {

        String selectQuery = "SELECT * FROM stockmanagement order by id";

        try (Connection con = Connect.getConnection();
             PreparedStatement ps = con.prepareStatement(selectQuery);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                int id = rs.getInt(1);
                String name = rs.getString(2);
                double newUnitePrice = rs.getDouble(3);
                int qty = rs.getInt(4);
                LocalDate importDate = rs.getDate(5).toLocalDate();

                Product product = new Product(id, name, newUnitePrice, qty, importDate);
                productToUpdate.add(product);
            }
        } catch (SQLException e) {
            System.err.println(RED+"Error fetching products: "+ e.getMessage()+RESET);
            e.printStackTrace();
        }

        return productToUpdate;
    }


    @Override
    public void tempProductList(Product product) {
        tempProductList.add(product);
    }

    @Override
    public List<Product> getTempProductList() {
        return tempProductList;
    }


    public void read() {
        Scanner scanner = new Scanner(System.in);
        int id;
        do {
            System.out.print("Enter product ID: ");
            String input = scanner.nextLine();
            if (!input.matches(validate.INT)) {
                System.out.println("Invalid product ID");
            } else {
                id = Integer.parseInt(input);
                break;
            }
        } while (true);


        String query = "SELECT * FROM stockmanagement WHERE id = ?";

        Table table = new Table(5, BorderStyle.UNICODE_BOX_DOUBLE_BORDER, ShownBorders.ALL);
        CellStyle align = new CellStyle(CellStyle.HorizontalAlign.center);

        table.setColumnWidth(0, 20, 100);
        table.setColumnWidth(1, 20, 100);
        table.setColumnWidth(2, 20, 100);
        table.setColumnWidth(3, 20, 100);
        table.setColumnWidth(4, 20, 100);

        table.addCell("ID", align);
        table.addCell("Name", align);
        table.addCell("Unit Price", align);
        table.addCell("Qty", align);
        table.addCell("Import Date", align);

        try (Connection conn = Connect.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                table.addCell(String.valueOf(rs.getInt("id")), align);
                table.addCell(rs.getString("name"), align);
                table.addCell(String.valueOf(rs.getDouble("unitprice")), align);
                table.addCell(String.valueOf(rs.getInt("qty")), align);
                table.addCell(String.valueOf(rs.getDate("import_date").toLocalDate()), align);

                System.out.println(table.render());
                System.out.println(GREEN+"Product found"+RESET);
            } else {
                System.out.println(RED+"Product with ID " + id + " not found."+RESET);
            }
        } catch (SQLException e) {
            System.err.println(RED+"Error reading product from database:"+RESET);
            e.printStackTrace();
        }
    }

    private List<Product> productsToUpdate = new ArrayList<>();

    @Override
    public Product updateProductInTempList() {
        Scanner scanner = new Scanner(System.in);
        int id;
        do{
            System.out.print("Enter product ID to update: ");
           id = Integer.parseInt(scanner.nextLine());
           if(!Pattern.matches(validate.INT,String.valueOf(id))){
               System.out.println(RED+"Invalid product ID"+RESET);
           }
        }while (!Pattern.matches(validate.INT,String.valueOf(id)));



        productToUpdate = getAllProductsFromDB();


        Table table = new Table(5, BorderStyle.UNICODE_BOX_DOUBLE_BORDER, ShownBorders.ALL);
        CellStyle align = new CellStyle(CellStyle.HorizontalAlign.center);

        table.setColumnWidth(0, 20, 100);
        table.setColumnWidth(1, 20, 100);
        table.setColumnWidth(2, 20, 100);
        table.setColumnWidth(3, 20, 100);
        table.setColumnWidth(4, 20, 100);

        table.addCell("ID", align);
        table.addCell("Name", align);
        table.addCell("Unit Price", align);
        table.addCell("Qty", align);
        table.addCell("Import Date", align);
        for (Product product : productToUpdate) {
            if (product.getId() == id) {
                table.addCell(String.valueOf(product.getId()), align);
                table.addCell(product.getName());
                table.addCell(String.valueOf(product.getUnitPrice()), align);
                table.addCell(String.valueOf(product.getQty()), align);
                table.addCell(String.valueOf(product.getDate()), align);
                System.out.println(table.render());

                Product updatedProduct = new Product(product.getId(), product.getName(),
                        product.getUnitPrice(), product.getQty(), product.getDate());


                String newName;
                do {
                    System.out.print("Enter new product name:");
                    newName = scanner.nextLine().trim();
                    if (!newName.isEmpty()) {
                        updatedProduct.setName(newName);}
                    if (!Pattern.matches(validate.PRODUCT_NAME_REGEX, newName)) {
                        System.out.println(RED+"Invalid product name."+RESET);
                    }

                } while (!Pattern.compile(validate.PRODUCT_NAME_REGEX).matcher(newName).matches());

                double newUnitePrice;
                do {
                    System.out.print("Enter new product newUnitePrice: ");
                    String input = scanner.nextLine().trim();

                    try {
                        newUnitePrice = Double.parseDouble(input);

                        if (!input.matches(validate.PRODUCT_UNIT_PRICE)) {
                            System.out.println(RED+"Invalid product unit price. Must be positive with up to 2 decimal places (e.g., 9.99)"+RESET);
                            continue;
                        }

                        if (newUnitePrice != 0) {
                            updatedProduct.setUnitPrice(newUnitePrice);
                        } else {
                            System.out.println(RED+"Price cannot be zero!"+RESET);
                            continue;
                        }

                        break; 

                    } catch (NumberFormatException e) {
                        System.out.println("Please enter a valid number!");
                    }
                } while (true);

                int newQty;
                do {
                    System.out.print("Enter new product quantity: ");
                    String input = scanner.nextLine().trim();

                    try {
                        newQty = Integer.parseInt(input);

                        if (!input.matches(validate.PRODUCT_STOCK_QTY)) {
                            System.out.println(RED+"Invalid quantity! Must be a positive whole number (e.g., 10)"+RESET);
                            continue;
                        }

                        if (newQty != 0) {
                            updatedProduct.setQty(newQty);
                        } else {
                            System.out.println(RED+"Quantity cannot be zero!"+RESET);
                            continue;
                        }

                        break;

                    } catch (NumberFormatException e) {
                        System.out.println(RED+"Please enter a valid whole number!"+RESET);
                    }
                } while (true);

                updatedProduct.setDate(LocalDate.now());


                productsToUpdate.add(updatedProduct);
                return updatedProduct;
            }
        }

        System.out.println(RED+"Product with ID " + id + " not found."+RESET);
        return null;
    }

    public void updateProductIntoDB() {
        if (productsToUpdate.isEmpty()) {
            System.out.println(RED+"No products to update."+RESET);
            return;
        }

        String query = "UPDATE stockmanagement SET name = ?, unitprice = ?, qty = ?, import_date = ? WHERE id = ? order by id ASC";
        int totalUpdated = 0;

        try (Connection connection = Connect.getConnection();
             PreparedStatement stmt = connection.prepareStatement(query)) {

            for (Product product : productsToUpdate) {

                stmt.setString(1, product.getName());
                stmt.setDouble(2, product.getUnitPrice());
                stmt.setInt(3, product.getQty());
                stmt.setDate(4, Date.valueOf(product.getDate()));
                stmt.setInt(5, product.getId());


                stmt.addBatch();
            }


            int[] rowsAffected = stmt.executeBatch();
            totalUpdated = Arrays.stream(rowsAffected).sum();

            if (totalUpdated > 0) {
                System.out.println(totalUpdated +GREEN+ " products updated successfully in the database."+RESET);

                productToUpdate = getAllProductsFromDB();

                productsToUpdate.clear();
            } else {
                System.out.println(RED+"No products were updated."+RESET);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println(RED+"Error updating products in database."+RESET);
        }
    }


    @Override
    public boolean deleteProductFromDB(int id) {
        String query = "DELETE FROM stockmanagement WHERE id = ?";

        try (Connection conn = Connect.getConnection()) {
            conn.setAutoCommit(false);

            try (PreparedStatement ps = conn.prepareStatement(query)) {
                ps.setInt(1, id);
                int rowsAffected = ps.executeUpdate();

                if (rowsAffected > 0) {
                    List<Product> productsToUpdate = getAllProductsFromDB();


                    Map<Integer, Product> uniqueProducts = productsToUpdate.stream()
                            .collect(Collectors.toMap(
                                    Product::getId,
                                    p -> p,
                                    (existing, replacement) -> existing
                            ));

                    Table table = new Table(5, BorderStyle.UNICODE_BOX_DOUBLE_BORDER, ShownBorders.ALL);
                    CellStyle align = new CellStyle(CellStyle.HorizontalAlign.center);

                    table.setColumnWidth(0, 20, 100);
                    table.setColumnWidth(1, 20, 100);
                    table.setColumnWidth(2, 20, 100);
                    table.setColumnWidth(3, 20, 100);
                    table.setColumnWidth(4, 20, 100);

                    table.addCell("ID", align);
                    table.addCell("Name", align);
                    table.addCell("Unit Price", align);
                    table.addCell("Qty", align);
                    table.addCell("Import Date", align);

                    // Show only unique products (if duplicates exist)
                    for (Product product : uniqueProducts.values()) {
                        table.addCell(String.valueOf(product.getId()), align);
                        table.addCell(product.getName(), align);
                        table.addCell(String.valueOf(product.getUnitPrice()), align);
                        table.addCell(String.valueOf(product.getQty()), align);
                        table.addCell(String.valueOf(product.getDate()), align);
                    }

                    System.out.println(table.render());

                    String delete;
                    do{
                        System.out.print(GREEN+"Do you want to delete the product from the database? (Yes/No): "+RESET);
                        delete = new Scanner(System.in).nextLine().toUpperCase().trim();
                        if(!Pattern.matches(validate.ARE_YOU_SURE, delete))
                        {
                            System.out.println(RED+"Please enter a valid whole String!"+RESET);
                        }
                    }while(!Pattern.compile(validate.ARE_YOU_SURE, Pattern.DOTALL).matcher(delete).matches());

                    if (delete.equals("YES") || delete.equals("Y")) {
                        conn.commit();
                        return true;
                    } else if (delete.equals("NO") || delete.equals("N")) {
                        conn.rollback();
                        return false;
                    }
                } else {
                    System.out.println(RED+"No product found with ID: "+RESET + id);
                    return false;
                }
            } catch (SQLException e) {
                conn.rollback();
                e.printStackTrace();
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
        return false;
    }


    @Override
    public List<Product> searchProductsByName(String name) {
        List<Product> searchResults = new ArrayList<>();

        String query = "SELECT * FROM stockmanagement WHERE LOWER(name) LIKE LOWER(?)";

        try (Connection conn = Connect.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {


            ps.setString(1, "%" + name + "%");

            try (ResultSet rs = ps.executeQuery()) {

                while (rs.next()) {
                    searchResults.add(new Product(
                            rs.getInt("id"),
                            rs.getString("name"),
                            rs.getDouble("unitprice"),
                            rs.getInt("qty"),
                            rs.getDate("import_date").toLocalDate()

                    ));
                }
                System.out.println(RED+"Products found with name "+RESET + name + ":");

            }

        } catch (SQLException e) {
            System.err.println(RED+"Error searching products:"+RESET);
            System.err.println(RED+"SQL State: " + e.getSQLState()+RESET);
            System.err.println(RED+"Error Code: " + e.getErrorCode()+RESET);
            System.err.println(RED+"Message: " + e.getMessage()+RESET);
        }
        return searchResults;
    }


    public void clearTempProducts() {
        tempProductList.clear();
    }


    public void unsaveInsertProducts() {
        Table table = new Table(5, BorderStyle.UNICODE_BOX_DOUBLE_BORDER, ShownBorders.ALL);
        CellStyle align = new CellStyle(CellStyle.HorizontalAlign.center);

        table.setColumnWidth(0, 20, 100);
        table.setColumnWidth(1, 20, 100);
        table.setColumnWidth(2, 20, 100);
        table.setColumnWidth(3, 20, 100);
        table.setColumnWidth(4, 20, 100);

        table.addCell("ID", align);
        table.addCell("Name", align);
        table.addCell("Unit Price", align);
        table.addCell("Qty", align);
        table.addCell("Import Date", align);
        for (Product product : tempProductList) {
            table.addCell(String.valueOf(product.getId()), align);
            table.addCell(product.getName(), align);
            table.addCell(String.valueOf(product.getUnitPrice()), align);
            table.addCell(String.valueOf(product.getQty()), align);
            table.addCell(String.valueOf(product.getDate()), align);
        }
        System.out.println(table.render());
        if(productsToUpdate.isEmpty()) {
            System.out.println(RED+"No products to inserted."+RESET);
        }
        else{
            System.out.println(productsToUpdate.size() + GREEN+" products inserted"+RESET);
        }
    }

    public void unsaveUpdateProducts() {
        Table table = new Table(5, BorderStyle.UNICODE_BOX_DOUBLE_BORDER, ShownBorders.ALL);
        CellStyle align = new CellStyle(CellStyle.HorizontalAlign.center);

        table.setColumnWidth(0, 20, 100);
        table.setColumnWidth(1, 20, 100);
        table.setColumnWidth(2, 20, 100);
        table.setColumnWidth(3, 20, 100);
        table.setColumnWidth(4, 20, 100);

        table.addCell("ID", align);
        table.addCell("Name", align);
        table.addCell("Unit Price", align);
        table.addCell("Qty", align);
        table.addCell("Import Date", align);
        for (Product product : productsToUpdate) {
            table.addCell(String.valueOf(product.getId()), align);
            table.addCell(product.getName(), align);
            table.addCell(String.valueOf(product.getUnitPrice()), align);
            table.addCell(String.valueOf(product.getQty()), align);
            table.addCell(String.valueOf(product.getDate()), align);
        }
        System.out.println(table.render());
        if(productsToUpdate.isEmpty()) {
            System.out.println(RED+"No products to update."+RESET);
        }
        else{
            System.out.println(productsToUpdate.size() +GREEN+ " products updated."+RESET);
        }
    }

    @Override
    public void backupToCSV() {
       String fileName;
       do{
           System.out.print("Enter file name to backup to csv:");
            fileName = new Scanner(System.in).nextLine().trim();
            if(!Pattern.matches(validate.FILE, fileName)){
                System.out.println(RED+"Invalid file name"+RESET);
            }

       }while(!Pattern.compile(validate.FILE).matcher(fileName).matches());
        LocalDate importDate = LocalDate.now();

        String filePath = "D:\\Mini_java_project\\StockManagemet\\src\\File\\"
                + fileName + importDate +   ".csv";

        File backupFile = new File(filePath);


        File parentDir = backupFile.getParentFile();
        if (!parentDir.exists()) {
            if (!parentDir.mkdirs()) {
                System.err.println(RED+"Failed to create backup directory structure!"+RESET);
                return;
            }
        }

        String query = "SELECT id, name, unitprice, qty, import_date FROM stockmanagement ORDER BY id";

        try (Connection conn = Connect.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query);
             PrintWriter writer = new PrintWriter(new FileWriter(backupFile))) {


            if (conn.isClosed()) {
                throw new SQLException(RED+"Database connection is closed"+RESET);
            }


            writer.println("id,name,unit_price,quantity,import_date");


            int recordCount = 0;
            while (rs.next()) {
                writer.printf("%d,%s,%.2f,%d,%s%n",
                        rs.getInt("id"),
                        escapeCsv(rs.getString("name")),
                        rs.getDouble("unitprice"),
                        rs.getInt("qty"),
                        rs.getDate("import_date") != null ? rs.getDate("import_date").toString() : ""
                );
                recordCount++;
            }


            writer.flush();
            if (writer.checkError()) {
                throw new IOException(GREEN+"Error writing to backup file"+RESET);
            }

            System.out.printf(GREEN+"Backup successful! %d records saved to:%n%s%n"+RESET,
                    recordCount, backupFile.getAbsolutePath());

        } catch (SQLException e) {
            System.err.println(RED+"Database error during backup:"+RESET);
            e.printStackTrace();

            if (backupFile.exists() && !backupFile.delete()) {
                System.err.println(RED+"Warning: Could not delete incomplete backup file"+RESET);
            }
        } catch (IOException e) {
            System.err.println(RED+"File error during backup:"+RESET);
            e.printStackTrace();

            if (backupFile.exists() && !backupFile.delete()) {
                System.err.println(RED+"Warning: Could not delete corrupted backup file"+RESET);
            }
        }
    }

    private String escapeCsv(String input) {
        if (input == null || input.isEmpty()) {
            return "";
        }


        boolean needsQuotes = input.contains(",")
                || input.contains("\"")
                || input.contains("\n")
                || input.contains("\r");

        // Escape quotes by doubling them
        String escaped = input.replace("\"", "\"\"");

        return needsQuotes ? "\"" + escaped + "\"" : escaped;
    }





    @Override
    public void restoreFromCSV() {
        System.out.print("Enter file name to restore from csv:");
        String fileName = new Scanner(System.in).nextLine().trim();
        String filePath = "D:\\Mini_java_project\\StockManagemet\\src\\File\\"
                + fileName + ".csv";
        File csvFile = new File(filePath);

        if (!csvFile.exists()) {
            System.err.println(RED + "Backup file not found: " + RESET + filePath);
            return;
        }

        try (Connection conn = Connect.getConnection()) {
            conn.setAutoCommit(false);

            try (Statement stmt = conn.createStatement()) {
                // Truncate the table
                stmt.executeUpdate("TRUNCATE TABLE stockmanagement");
                System.out.println(GREEN + "Existing data cleared from database." + RESET);

                String insertSQL = "INSERT INTO stockmanagement (id, name, unitprice, qty, import_date) VALUES (?, ?, ?, ?, ?)";

                try (PreparedStatement pstmt = conn.prepareStatement(insertSQL);
                     BufferedReader reader = new BufferedReader(new FileReader(csvFile))) {

                    reader.readLine();

                    String line;
                    int recordCount = 0;
                    int maxId = 0;

                    while ((line = reader.readLine()) != null) {
                        String[] values = line.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)", -1);

                        int id = Integer.parseInt(values[0]);
                        String name = values[1].replaceAll("^\"|\"$", "").replace("\"\"", "\"");
                        double newUnitePrice = Double.parseDouble(values[2]);
                        int qty = Integer.parseInt(values[3]);
                        Date importDate = !values[4].isEmpty() ? Date.valueOf(values[4]) : null;


                        if (id > maxId) {
                            maxId = id;
                        }

                        pstmt.setInt(1, id);
                        pstmt.setString(2, name);
                        pstmt.setDouble(3, newUnitePrice);
                        pstmt.setInt(4, qty);
                        if (importDate != null) {
                            pstmt.setDate(5, importDate);
                        } else {
                            pstmt.setNull(5, Types.DATE);
                        }

                        pstmt.addBatch();
                        recordCount++;

                        if (recordCount % 100 == 0) {
                            pstmt.executeBatch();
                        }
                    }

                    pstmt.executeBatch();


                    String resetSequenceSQL = "";
                    String dbProduct = conn.getMetaData().getDatabaseProductName();

                    if (dbProduct.equals("PostgreSQL")) {
                        resetSequenceSQL = "ALTER SEQUENCE stockmanagement_id_seq RESTART WITH " + (maxId + 1);
                    } else if (dbProduct.equals("MySQL")) {
                        resetSequenceSQL = "ALTER TABLE stockmanagement AUTO_INCREMENT = " + (maxId + 1);
                    } else if (dbProduct.equals("Oracle")) {
                        resetSequenceSQL = "DROP SEQUENCE stockmanagement_seq;" +
                                "CREATE SEQUENCE stockmanagement_seq START WITH " + (maxId + 1);
                    } else if (dbProduct.equals("Microsoft SQL Server")) {
                        resetSequenceSQL = "DBCC CHECKIDENT ('stockmanagement', RESEED, " + maxId + ")";
                    }

                    if (!resetSequenceSQL.isEmpty()) {
                        stmt.executeUpdate(resetSequenceSQL);
                        System.out.println(GREEN + "ID sequence reset to " + (maxId + 1) + RESET);
                    }

                    conn.commit();

                    System.out.printf(GREEN + "Restore successful! %d records loaded from:%n%s%n" + RESET,
                            recordCount, filePath);

                } catch (Exception e) {
                    conn.rollback();
                    System.err.println(RED + "Error during restore - transaction rolled back:" + RESET);
                    e.printStackTrace();
                }
            }
        } catch (SQLException e) {
            System.err.println(RED + "Database connection error:" + RESET);
            e.printStackTrace();
        }
    }

}




