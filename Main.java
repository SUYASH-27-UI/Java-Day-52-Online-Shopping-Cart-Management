
import java.util.ArrayList;
import java.util.Scanner;
import java.io.FileWriter;
import java.io.IOException;

class Product {
    int id;
    String name;
    double price;

    Product(int id, String name, double price) {
        this.id = id;
        this.name = name;
        this.price = price;
    }
}

class CartItem {
    Product product;
    int quantity;

    CartItem(Product product, int quantity) {
        this.product = product;
        this.quantity = quantity;
    }

    double getTotal() {
        return product.price * quantity;
    }
}

public class Main {

    static Scanner sc = new Scanner(System.in);
    static ArrayList<Product> products = new ArrayList<>();
    static ArrayList<CartItem> cart = new ArrayList<>();

    static void loadProducts() {
        products.add(new Product(101, "Keyboard", 750));
        products.add(new Product(102, "Mouse", 400));
        products.add(new Product(103, "Monitor", 8500));
        products.add(new Product(104, "Headphone", 1200));
    }

    static void displayProducts() {
        System.out.println("\n--- AVAILABLE PRODUCTS ---");

        for (Product p : products) {
            System.out.printf(
                "ID: %d | %s | Rs. %.2f%n",
                p.id, p.name, p.price
            );
        }
    }

    static void addToCart() {
        displayProducts();

        System.out.print("\nEnter Product ID: ");
        int id = sc.nextInt();

        Product selected = null;

        for (Product p : products) {
            if (p.id == id) {
                selected = p;
                break;
            }
        }

        if (selected == null) {
            System.out.println("Product not found.");
            return;
        }

        System.out.print("Enter Quantity: ");
        int quantity = sc.nextInt();

        if (quantity <= 0) {
            System.out.println("Invalid quantity.");
            return;
        }

        for (CartItem item : cart) {
            if (item.product.id == id) {
                item.quantity += quantity;
                System.out.println("Cart quantity updated.");
                return;
            }
        }

        cart.add(new CartItem(selected, quantity));
        System.out.println("Product added to cart.");
    }

    static void viewCart() {
        if (cart.isEmpty()) {
            System.out.println("\nYour cart is empty.");
            return;
        }

        double grandTotal = 0;

        System.out.println("\n--- SHOPPING CART ---");

        for (CartItem item : cart) {
            double total = item.getTotal();
            grandTotal += total;

            System.out.printf(
                "%s | Quantity: %d | Total: Rs. %.2f%n",
                item.product.name,
                item.quantity,
                total
            );
        }

        System.out.printf("Grand Total: Rs. %.2f%n", grandTotal);
    }

    static void removeFromCart() {
        if (cart.isEmpty()) {
            System.out.println("Your cart is empty.");
            return;
        }

        System.out.print("Enter Product ID to remove: ");
        int id = sc.nextInt();

        for (int i = 0; i < cart.size(); i++) {
            if (cart.get(i).product.id == id) {
                cart.remove(i);
                System.out.println("Product removed from cart.");
                return;
            }
        }

        System.out.println("Product not found in cart.");
    }

    static void checkout() {
        if (cart.isEmpty()) {
            System.out.println("Your cart is empty.");
            return;
        }

        double grandTotal = 0;

        try (FileWriter writer = new FileWriter("orders.txt", true)) {

            writer.write("\n--- NEW ORDER ---\n");

            for (CartItem item : cart) {
                double total = item.getTotal();
                grandTotal += total;

                writer.write(
                    item.product.name + " | Quantity: "
                    + item.quantity + " | Total: Rs. "
                    + String.format("%.2f", total) + "\n"
                );
            }

            writer.write(
                "Grand Total: Rs. "
                + String.format("%.2f", grandTotal) + "\n"
            );

            System.out.printf(
                "Checkout successful! Amount: Rs. %.2f%n",
                grandTotal
            );

            cart.clear();

        } catch (IOException e) {
            System.out.println("Error saving order.");
        }
    }

    public static void main(String[] args) {
        loadProducts();

        while (true) {
            System.out.println("\n===== ONLINE SHOPPING CART =====");
            System.out.println("1. View Products");
            System.out.println("2. Add to Cart");
            System.out.println("3. View Cart");
            System.out.println("4. Remove from Cart");
            System.out.println("5. Checkout");
            System.out.println("6. Exit");

            System.out.print("Enter choice: ");
            int choice = sc.nextInt();

            switch (choice) {
                case 1:
                    displayProducts();
                    break;

                case 2:
                    addToCart();
                    break;

                case 3:
                    viewCart();
                    break;

                case 4:
                    removeFromCart();
                    break;

                case 5:
                    checkout();
                    break;

                case 6:
                    System.out.println("Thank you for shopping!");
                    sc.close();
                    return;

                default:
                    System.out.println("Invalid choice.");
            }
        }
    }
}
