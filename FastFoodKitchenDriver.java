package fastfoodkitchendriver;

import java.io.IOException;
import java.util.Scanner;



/**
 * FastFoodKitchenDriver codee
 */
public class FastFoodKitchenDriver {

    public static void main(String[] args) {

        try {
            // Initialize kitchen with orders from a file
            FastFoodKitchen kitchen = new FastFoodKitchen("Order.csv");
            try (Scanner sc = new Scanner(System.in)) {
                while (kitchen.getNumOrdersPending() != 0) {
                    System.out.println("Please select from the following menu of options, by typing a number:");
                    System.out.println("\t 1. Order food");
                    System.out.println("\t 2. Cancel last order");
                    System.out.println("\t 3. Show number of orders currently pending");
                    System.out.println("\t 4. Complete order");
                    System.out.println("\t 5. Check on order");
                    System.out.println("\t 6. Cancel order");
                    System.out.println("\t 7. Exit");
                    
                    int num = sc.nextInt();
                    switch (num) {
                        case 1 -> {
                            System.out.println("How many hamburgers do you want?");
                            int ham = sc.nextInt();
                            System.out.println("How many cheeseburgers do you want?");
                            int cheese = sc.nextInt();
                            System.out.println("How many veggieburgers do you want?");
                            int veggie = sc.nextInt();
                            System.out.println("How many sodas do you want?");
                            int sodas = sc.nextInt();
                            System.out.println("Is your order to go? (Y/N)");
                            char letter = sc.next().charAt(0);
                            boolean TOGO = letter == 'Y' || letter == 'y';
                            int orderNum = kitchen.addOrder(ham, cheese, veggie, sodas, TOGO);
                            System.out.println("Thank you. Your order number is " + orderNum);
                            System.out.println();
                        }
                        case 2 -> {
                            boolean ready = kitchen.cancelLastOrder();
                            if (ready) {
                                System.out.println("Thank you. The last order has been canceled");
                            } else {
                                System.out.println("Sorry. There are no orders to cancel.");
                            }
                            System.out.println();
                        }
                        case 3 -> System.out.println("There are " + kitchen.getNumOrdersPending() + " pending orders");
                        case 4 -> {
                            System.out.println("Enter order number to complete?");
                            int order = sc.nextInt();
                            kitchen.completeSpecificOrder(order);
                            System.out.println("Your order is ready. Thank you!");
                        }
                        case 5 -> {
                            System.out.println("What is your order number?");
                            int order = sc.nextInt();
                            boolean ready = kitchen.isOrderDone(order);
                            if (ready) {
                                System.out.println("Sorry, no order with this number was found.");
                            } else {
                                System.out.println("No, it's not ready, but it should be up soon. Sorry for the wait.");
                            }
                            System.out.println();
                        }
                        case 6 -> {
                            System.out.println("What is your order number?");
                            int order = sc.nextInt();
                            boolean cancel = kitchen.cancelOrder(order);
                            if (cancel) {
                                System.out.println("Your order has been successfully cancelled ");
                            } else {
                                System.out.println("Sorry, we can’t find your order number in the system");
                            }
                            System.out.println();
                        }
                        case 7 -> {
                            // Generate the end-of-day report and updated orders file before exiting
                            kitchen.generateEndOfDayReport("endOfDayReport.txt");
                            kitchen.createUpdatedOrdersCSV("UpdatedOrders.csv");
                            System.out.println("End of day report and updated orders file have been generated.");
                            System.exit(0);
                        }
                        default -> System.out.println("Sorry, but you need to enter a 1, 2, 3, 4, 5, 6, or a 7");
                    }
                }
                // Close the scanner
            }
        } catch (IOException e) {
            System.out.println("An error occurred: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
