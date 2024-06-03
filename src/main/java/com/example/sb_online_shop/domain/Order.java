package com.example.sb_online_shop.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import java.util.List;

@Entity(name = "Orders")
public class Order {
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private double total;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "customer_id")
    private Customer customer;
    
    @OneToMany
    @JoinColumn(name = "order_id")
    private List<Ithem> items;

    public Order() {}

    public Order(double total, Customer customer, List<Ithem> items) {
        this.total = total;
        this.customer = customer;
        this.items = items;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public List<Ithem> getItems() {
        return items;
    }

    public void setItems(List<Ithem> items) {
        this.items = items;
    }
}
