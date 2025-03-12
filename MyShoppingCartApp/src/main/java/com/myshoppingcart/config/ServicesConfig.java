package com.myshoppingcart.config;

import com.myshoppingcart.persistence.ICompraRepository;
import com.myshoppingcart.persistence.IProductoRepository;
import com.myshoppingcart.persistence.IUsuarioRepository;
import com.myshoppingcart.service.IShoppingCart;
import com.myshoppingcart.service.ShoppingCart;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ServicesConfig {
    @Autowired
    ICompraRepository compraRepository;

    @Autowired
    IProductoRepository productoRepository;

    @Autowired
    IUsuarioRepository usuarioRepository;

    @Bean
    public IShoppingCart getShoppingCartServiceBean(){
        ShoppingCart srv = new ShoppingCart();

        srv.setCompraRepository(compraRepository);
        srv.setProductoRepository(productoRepository);
        srv.setUsuarioRepository(usuarioRepository);

        return srv;
    }
}
