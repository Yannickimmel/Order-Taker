package fastfoodkitchendriver;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
/*
 * Combines the code from FastFoodKitchenDriver.java and FastFoodKitchen.java
 */
public class FastFoodKitchen {

    @SuppressWarnings({ "rawtypes", "unchecked" })
    private ArrayList<BurgerOrder> orderList = new ArrayList();

    private static int nextOrderNum = 1;


/**
 * Get the value of nextOrderNum
 * @return the value of nextOrderNum
 */
    public static int getNextOrderNum() {
        return nextOrderNum;
    }
/**
 * Increment the value of nextOrderNum
 * 
 */
    private void incrementNextOrderNum() {
        nextOrderNum++;
    }
/**
 * Add an order
 * @param ham 
 * @param cheese
 * @param veggie
 * @param soda
 * @param toGo
 * @return the value of nextOrderNum
 */
    public int addOrder(int ham, int cheese, int veggie, int soda, boolean toGo) {
        int orderNum = getNextOrderNum();
        orderList.add(new BurgerOrder(ham, cheese, veggie, soda, toGo, orderNum));
        incrementNextOrderNum();
        orderCallOut(orderList.get(orderList.size() - 1));
        return orderNum;

    }

    // Modified isOrderDone method
    public boolean isOrderDone(int orderID) {
        for (BurgerOrder order : orderList) {
            if (order.getOrderNum() == orderID) {
                return order.isCompleted(); // Checks completion status of the order
            }
        }
        return false;
    }
/**
 * Cancel an order
 * @param orderID
 * @return the value of nextOrderNum
 */
    public boolean cancelOrder(int orderID) {
        for (int i = 0; i < orderList.size(); i++) {
            if (orderList.get(i).getOrderNum() == orderID) {
                orderList.remove(i);
                return true;
            }
        }
        return false;
    }
/**
 * Get the number of pending orders
 * @return the number of pending orders
 */
    public int getNumOrdersPending() {
        return orderList.size();
    }
/**
 * Cancel the last order
 * @return the value of nextOrderNum
 */
    public boolean cancelLastOrder() {

        if (!orderList.isEmpty()) { // same as  if (orderList.size() > 0) 
            orderList.remove(orderList.size() - 1);
            return true;
        }

        return false;
    }
/**
 * Call out the order
 * @param order 
 */
    private void orderCallOut(BurgerOrder order) {
        if (order.getNumHamburger() > 0) {
            System.out.println("You have " + order.getNumHamburger() + " hamburgers");
        }
        if (order.getNumCheeseburgers() > 0) {
            System.out.println("You have " + order.getNumCheeseburgers() + " cheeseburgers");
        }
        if (order.getNumVeggieburgers() > 0) {
            System.out.println("You have " + order.getNumVeggieburgers() + " veggieburgers");
        }
        if (order.getNumSodas() > 0) {
            System.out.println("You have " + order.getNumSodas() + " sodas");
        }
    }
/**
 * Complete an order
 * @param orderID 
 */

    public void completeSpecificOrder(int orderID) {
        for (BurgerOrder order : orderList) {
            if (order.getOrderNum() == orderID) {
                System.out.println("Order number " + orderID + " is done!");
                if (order.isOrderToGo()) {
                    orderCallOut(order); // Make sure this is the correct order
                }
                order.completeOrder();
                return;
            }
        }   
        System.out.println("Order number " + orderID + " not found.");
    }

/**
 * Complete the next order
 */
    public void completeNextOrder() {
        int nextOrder = orderList.get(0).getOrderNum();
        completeSpecificOrder(nextOrder);

    }

/**
 * Get the order list
 * @return the order list
 */
    public ArrayList<BurgerOrder> getOrderList() {
        return orderList;
    }
/** 
 * Get the index of an order
 * @param num
 * @return the index
 */
    public int findOrderSeq(int num) {
        for (int j = 0; j < orderList.size(); j++) {
            if (orderList.get(j).getOrderNum() == num ) {
                return j;
            }
        }
        return -1;
    }
/**
 * Get the index of an order
 * @param orderID
 * @return the index
 */
    public int findOrderBin(int orderID){
        int left = 0;
        int right = orderList.size()-1;
        while (left <= right){
            int middle = ((left + right)/2);
            if (orderID < orderList.get(middle).getOrderNum()){
                right = middle-1;
            }
            else if(orderID > orderList.get(middle).getOrderNum()){
                left = middle +1;
            }
            else{
                return middle;
            }
        }
        return -1;
        
    }
    /**
     * Sort the list
     * @param orderList
     * @return the sorted list
     */
    public void selectionSort(){
        for (int i = 0; i< orderList.size()-1; i++){
            int minIndex = i;
            for (int k = i+1; k < orderList.size(); k++){
                if (orderList.get(minIndex).getTotalBurgers() > orderList.get(k).getTotalBurgers()){
                    minIndex = k;
                }
            }
            BurgerOrder temp = orderList.get(i);
            orderList.set(i , orderList.get(minIndex));
            orderList.set(minIndex, temp);
        }
    }
/**
 * Sort the list
 * @param orderList
 * @return the sorted list
 */
    public void insertionSort() {
        for (int j = 1; j < orderList.size(); j++) {
            BurgerOrder temp = orderList.get(j);
            int possibleIndex = j;
            while (possibleIndex > 0 && temp.getTotalBurgers() < orderList.get(possibleIndex - 1).getTotalBurgers()) {
                orderList.set(possibleIndex, orderList.get(possibleIndex - 1));
                possibleIndex--;
            }
            orderList.set(possibleIndex, temp);
        }
    }
    /**
     * Constructor
     * @param ordersFilePath
     * @throws IOException
     */

    public FastFoodKitchen(String ordersFilePath) throws IOException {
        orderList = new ArrayList<>(); 
        nextOrderNum = 1; // Reset the next order number
        loadOrdersFromCSV(ordersFilePath);
    }
    /**
     * Load orders from a CSV file
     * @param filePath
     * @throws IOException
     */
    private void loadOrdersFromCSV(String filePath) throws IOException {
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] orderDetails = line.split(",");
                if (orderDetails.length == 6) {
                    int numHamburgers = Integer.parseInt(orderDetails[1].trim());
                    int numCheeseburgers = Integer.parseInt(orderDetails[2].trim());
                    int numVeggieburgers = Integer.parseInt(orderDetails[3].trim());
                    int numSodas = Integer.parseInt(orderDetails[4].trim());
                    boolean orderToGo = Boolean.parseBoolean(orderDetails[5].trim());
                    int orderNum = Integer.parseInt(orderDetails[0].trim());
                    orderList.add(new BurgerOrder(numHamburgers, numCheeseburgers, numVeggieburgers, numSodas, orderToGo, orderNum));
                }
            }
        }
    }
    
    /**
     * Generate the end-of-day report
     * @param File
     * @throws IOException
     */
    public void generateEndOfDayReport(String File) throws IOException {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(File))) {
            int totalHamburgers = 0, totalCheeseburgers = 0, totalVeggieburgers = 0, totalSodas = 0;
            for (BurgerOrder order : orderList) {
                if (order.isCompleted()) {  // Check if the order is completed
                    bw.write("Order Number: " + order.getOrderNum() + " Completed: " + order.isCompleted());
                    bw.newLine();
                    
                    // Increment totals only for completed orders
                    totalHamburgers += order.getNumHamburger();
                    totalCheeseburgers += order.getNumCheeseburgers();
                    totalVeggieburgers += order.getNumVeggieburgers();
                    totalSodas += order.getNumSodas();
                }
            }       // Write totals
            bw.write("Total Hamburgers Sold: " + totalHamburgers +"\n");
            bw.write("Total Cheeseburgers Sold: " + totalCheeseburgers+"\n");
            bw.write("Total Veggieburgers Sold: " + totalVeggieburgers+"\n");
            bw.write("Total Sodas Sold: " + totalSodas+"\n");
            
        }
    }
    
   /**
    * Create an updated orders CSV
    * @param File
    * @throws IOException
    */
    public void createUpdatedOrdersCSV(String File) throws IOException {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(File))) {
            for (BurgerOrder order : orderList) {
                if (!isOrderDone(order.getOrderNum())) {
                    bw.write(order.getOrderNum() + "," +
                            order.getNumHamburger() + "," +
                            order.getNumCheeseburgers() + "," +
                            order.getNumVeggieburgers() + "," +
                            order.getNumSodas() + "," +
                            order.isOrderToGo());
                    bw.newLine();
                }
            }
        }
    }
    
/**
 * Clear the order history
 */
    public void clearOrderHistory() {
        orderList.clear();
        nextOrderNum = 1; 
    }
    
}
