package com.example.sb_online_shop;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.example.sb_online_shop.domain.Customer;
import com.example.sb_online_shop.domain.CustomerRepository;
import com.example.sb_online_shop.domain.Ithem;
import com.example.sb_online_shop.domain.IthemRepository;
import com.example.sb_online_shop.domain.Order;
import com.example.sb_online_shop.domain.OrderRepository;
import com.example.sb_online_shop.domain.Product;
import com.example.sb_online_shop.domain.ProductRepository;

@SpringBootApplication
public class SbOnlineApplication implements CommandLineRunner {
    private static final Logger logger = LoggerFactory.getLogger(SbOnlineApplication.class);
    private final CustomerRepository crepository;
    private final OrderRepository orepository;
    private final ProductRepository prepository;
    private final IthemRepository irepository;

    public SbOnlineApplication(CustomerRepository cRepository, OrderRepository oRepository, ProductRepository pRepository, IthemRepository iRepository) {
        this.crepository = cRepository;
        this.orepository = oRepository;
        this.prepository = pRepository;
        this.irepository = iRepository;
    }

    public static void main(String[] args) {
        SpringApplication.run(SbOnlineApplication.class, args);
        logger.info("Online Shop started at http://localhost:8080/");
    }

    @Override
    public void run(String... args) throws Exception {
        Customer c1 = new Customer("Bugs Bunny", "New York", 59000);
        Customer c2 = new Customer("Daffy Duck", "Los Angeles", 37000);
        Customer c3 = new Customer("Porky Pig", "Miami", 28000);
        crepository.saveAll(Arrays.asList(c1, c2, c3));

        Product p1 = new Product("Phone", 200.0);
        Product p2 = new Product("TV", 300.0);
        Product p3 = new Product("Shoes", 400.0);
        Product p4 = new Product("Shirt", 90.0);
        prepository.saveAll(Arrays.asList(p1, p2, p3, p4));

        Ithem ithem1 = new Ithem(p1, 1);  // Phone (200.0$)
        Ithem ithem2 = new Ithem(p2, 1);  // TV (300.0$)
        Ithem ithem3 = new Ithem(p1, 1);  // Phone (200.0$)
        Ithem ithem4 = new Ithem(p3, 1);  // Shoes (400.0$)
        Ithem ithem5 = new Ithem(p3, 1);  // Shoes (300.0$)
        Ithem ithem6 = new Ithem(p4, 1);  // Shirt (90.0$)
        irepository.saveAll(Arrays.asList(ithem1, ithem2, ithem3, ithem4, ithem5, ithem6));

        Order o1 = new Order(200, c1, Arrays.asList(ithem1));  // Phone (200.0$)
        Order o2 = new Order(700, c1, Arrays.asList(ithem2, ithem3, ithem5));  // TV (300.0$), Phone (200.0$), Shoes (300.0$)
        Order o3 = new Order(400, c2, Arrays.asList(ithem4));  // Shoes (400.0$)
        Order o4 = new Order(140, c3, Arrays.asList(ithem6));  // Shirt (90.0$)
        orepository.saveAll(Arrays.asList(o1, o2, o3, o4));

        System.out.println("----- All Orders ------");

        double total = StreamSupport.stream(orepository.findAll().spliterator(), false)
                .mapToDouble(Order::getTotal)
                .sum();
        System.out.println("All Orders: " + total + "$");

        for (Customer customer : crepository.findAll()) {
            System.out.println("customer " + customer.getFullname() + " :");

            List<Order> orders = StreamSupport.stream(orepository.findAll().spliterator(), false)
                    .filter(order -> order.getCustomer().equals(customer))
                    .collect(Collectors.toList());

            int orderNumber = 1;
            for (Order order : orders) {
                System.out.print("order " + orderNumber + " :" + order.getTotal() + "$ : ");
                String items = order.getItems().stream()
                                   .map(ithem -> ithem.getProduct().getName() + " (" + ithem.getProduct().getPrice() + "$)")
                                   .collect(Collectors.joining(", "));
                System.out.println("[" + items + "]");
                orderNumber++;
            }
            System.out.println(" ");
        }
        System.out.println("-----------");
    }
}
