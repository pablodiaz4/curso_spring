package com.myshoppingcart.service;

import com.myshoppingcart.exception.ProductNotFoundException;
import com.myshoppingcart.model.Compra;
import com.myshoppingcart.model.Producto;
import com.myshoppingcart.persistence.ICompraRepository;
import com.myshoppingcart.persistence.IProductoRepository;
import com.myshoppingcart.persistence.IUsuarioRepository;
import lombok.Setter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Iterator;

@Setter
//@Service
public class ShoppingCart implements IShoppingCart {

    //@Autowired
    ICompraRepository compraRepository;

    //@Autowired
    IProductoRepository productoRepository;

    //@Autowired
    IUsuarioRepository usuarioRepository;

    private ArrayList<Producto> items;
    private ICompraRepository repoCompras;

    public ShoppingCart() /*throws Exception*/ {
        items = new ArrayList<>();
    }

    @Override
    public double getBalance() {
        double balance = 0.00;
        for (Iterator i = items.iterator(); i.hasNext(); ) {
            Producto item = (Producto) i.next();
            balance += item.getPrecio();
        }
        return balance;
    }

    @Override
    public void addItem(Producto item) {
        items.add(item);
    }

    @Override
    public void removeItem(Producto item)
            throws ProductNotFoundException {
        if (!items.remove(item)) {
            throw new ProductNotFoundException();
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    @Override
    public void empty() {
        items.clear();
    }

    @Override
    public void comprar() {
        for (Producto item : items) {
            System.out.println("prod:" + item);
            try {
                Compra compra = new Compra(null, 1, item.getMid(), 1, LocalDate.now());
                repoCompras.insertCompra(compra);
            } catch (Exception e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            }
        }
    }

}