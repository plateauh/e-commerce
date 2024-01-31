package com.example.demo;

import com.example.demo.controllers.CartController;
import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.ItemRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.ModifyCartRequest;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * @author Najed Alseghair at 1/30/2024
 */
public class CartControllerTest {
    private CartController cartController;
    private UserRepository userRepository = mock(UserRepository.class);
    private CartRepository cartRepository = mock(CartRepository.class);
    private ItemRepository itemRepository = mock(ItemRepository.class);

    @Before
    public void setUp() {
        cartController = new CartController();
        TestUtils.injectObjects(cartController, "userRepository", userRepository);
        TestUtils.injectObjects(cartController, "cartRepository", cartRepository);
        TestUtils.injectObjects(cartController, "itemRepository", itemRepository);

        when(userRepository.findByUsername("test"))
                .thenReturn(TestUtils.getMockUser(0L, "test"));

        when(itemRepository.findById(1L))
                .thenReturn(Optional.of(TestUtils.getMockItem(1L, "testitem")));
    }

    /*
        Add to cart
     */
    
    @Test
    public void testAddToCartHappyPath() {
        ModifyCartRequest request = new ModifyCartRequest();
        request.setUsername("test");
        request.setItemId(1L);
        request.setQuantity(1);

        ResponseEntity<Cart> response = cartController.addTocart(request);
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void testAddToCartSadPathUserNotFound() {
        ModifyCartRequest request = new ModifyCartRequest();
        request.setUsername("test1");
        request.setItemId(1L);
        request.setQuantity(1);

        ResponseEntity<Cart> response = cartController.addTocart(request);
        assertNotNull(response);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void testAddToCartSadPathItemNotFound() {
        ModifyCartRequest request = new ModifyCartRequest();
        request.setUsername("test");
        request.setItemId(2L);
        request.setQuantity(1);

        ResponseEntity<Cart> response = cartController.addTocart(request);
        assertNotNull(response);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    /*
        Remove from cart
     */
    @Test
    public void testRemoveFromCartHappyPath() {
        when(userRepository.findByUsername("test"))
                .thenReturn(TestUtils.getMockUser(0L, "test", true));

        ModifyCartRequest request = new ModifyCartRequest();
        request.setUsername("test");
        request.setItemId(1L);
        request.setQuantity(1);

        ResponseEntity<Cart> response = cartController.removeFromcart(request);
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void testRemoveFromCartSadPathUserNotFound() {
        when(userRepository.findByUsername("test"))
                .thenReturn(TestUtils.getMockUser(0L, "test", true));

        ModifyCartRequest request = new ModifyCartRequest();
        request.setUsername("test1");
        request.setItemId(1L);
        request.setQuantity(1);

        ResponseEntity<Cart> response = cartController.removeFromcart(request);
        assertNotNull(response);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void testRemoveFromCartSadPathItemNotFound() {
        when(userRepository.findByUsername("test"))
                .thenReturn(TestUtils.getMockUser(0L, "test", true));

        ModifyCartRequest request = new ModifyCartRequest();
        request.setUsername("test");
        request.setItemId(0L);
        request.setQuantity(1);

        ResponseEntity<Cart> response = cartController.removeFromcart(request);
        assertNotNull(response);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }
}
