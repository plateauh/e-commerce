package com.example.demo;

import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.UserOrder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Najed Alseghair at 1/30/2024
 */
public class TestUtils {
    public static final Logger log = LoggerFactory.getLogger(TestUtils.class);
    public static void injectObjects(Object target, String fieldName, Object toInject) {
        boolean wasPrivate = false;
        try {
            Field field = target.getClass().getDeclaredField(fieldName);
            if (!field.isAccessible()) {
                field.setAccessible(true);
                wasPrivate = true;
            }
            field.set(target, toInject);
            if (wasPrivate) {
                field.setAccessible(false);
            }
        } catch (NoSuchFieldException e) {
            log.error("NoSuchFieldException occurred while getting field {}", fieldName);
        } catch (IllegalAccessException e) {
            log.error("IllegalAccessException occurred while injecting {} to {}", target, toInject);
        }
    }

    public static User getMockUser(Long id, String username) {
        User user = new User();
        user.setId(id);
        user.setUsername(username);
        user.setPassword("testtest");
        user.setCart(new Cart());
        return user;
    }

    public static Item getMockItem(Long id, String name) {
        Item item = new Item();
        item.setId(id);
        item.setName(name);
        item.setPrice(BigDecimal.valueOf(5.0));
        item.setDescription("I'm a good item");
        return item;
    }

    public static User getMockUser(Long id, String username, boolean withCart) {
        User user = getMockUser(id, username);
        Cart cart = user.getCart();
        cart.setId(id);
        cart.setItems(Collections.singletonList(getMockItem(0L, "testitem")));
        cart.setUser(getMockUser(0L, "test"));
        cart.setTotal(BigDecimal.valueOf(5.0));
        return user;
    }

    public static List<UserOrder> getMockUserOrders(String username) {
        User user = getMockUser(1L, username, true);
        UserOrder userOrder = new UserOrder();
        userOrder.setId(0L);
        userOrder.setUser(user);
        userOrder.setItems(user.getCart().getItems());
        userOrder.setTotal(user.getCart().getTotal());
        return Collections.singletonList(userOrder);
    }

}
