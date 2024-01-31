package com.example.demo;

import com.example.demo.controllers.OrderController;
import com.example.demo.model.persistence.UserOrder;
import com.example.demo.model.persistence.repositories.OrderRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * @author Najed Alseghair at 1/31/2024
 */
public class OrderControllerTest {
    private OrderController orderController;
    private UserRepository userRepository = mock(UserRepository.class);
    private OrderRepository orderRepository = mock(OrderRepository.class);

    @Before
    public void setUp() {
        orderController = new OrderController();
        TestUtils.injectObjects(orderController, "userRepository", userRepository);
        TestUtils.injectObjects(orderController, "orderRepository", orderRepository);

        when(userRepository.findByUsername("test"))
                .thenReturn(TestUtils.getMockUser(0L, "test", true));

        when(orderRepository.findByUser(TestUtils.getMockUser(0L, "test", true)))
                .thenReturn(TestUtils.getMockUserOrders("test"));
    }

    /*
        Submit order
     */
    @Test
    public void submitOrderHappyPath() {
        ResponseEntity<UserOrder> response = orderController.submit("test");
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void submitOrderSadPathUserNotFound() {
        ResponseEntity<UserOrder> response = orderController.submit("test1");
        assertNotNull(response);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    /*
        get Orders For User
     */
    @Test
    public void getOrdersForUserHappyPath() {
        ResponseEntity<List<UserOrder>> response = orderController.getOrdersForUser("test");
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());

        List<UserOrder> responseBody = response.getBody();
        assertNotNull(responseBody);
    }

    @Test
    public void getOrdersForUserSadPathUserNotFound() {
        ResponseEntity<List<UserOrder>> response = orderController.getOrdersForUser("test1");
        assertNotNull(response);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }
}
