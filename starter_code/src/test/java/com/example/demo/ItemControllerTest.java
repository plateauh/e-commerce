package com.example.demo;

import com.example.demo.controllers.ItemController;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.ItemRepository;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * @author Najed Alseghair at 1/30/2024
 */
public class ItemControllerTest {

    private ItemController itemController;
    private ItemRepository itemRepository = mock(ItemRepository.class);

    @Before
    public void setUp() {
        itemController = new ItemController();
        TestUtils.injectObjects(itemController, "itemRepository", itemRepository);

        when(itemRepository.findById(1L))
                .thenReturn(Optional.of(TestUtils.getMockItem(1L, "testitem")));

        when(itemRepository.findByName("testitem"))
                .thenReturn(Collections.singletonList(TestUtils.getMockItem(1L, "testitem")));
    }

    /*
        find item By ID
     */

    @Test
    public void findItemByIdHappyPath() {
        ResponseEntity<Item> response = itemController.getItemById(1L);
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());

        Item responseBody = response.getBody();
        assertNotNull(responseBody);
        assertEquals("testitem", responseBody.getName());
    }

    @Test
    public void findItemByIdSadPath() {
        ResponseEntity<Item> response = itemController.getItemById(0L);
        assertNotNull(response);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    /*
        find item By name
     */

    @Test
    public void findUserByUsernameHappyPath() {
        ResponseEntity<List<Item>> response = itemController.getItemsByName("testitem");
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());

        List<Item> responseBody = response.getBody();
        assertNotNull(responseBody);
        assertEquals("testitem", responseBody.get(0).getName());
    }

    @Test
    public void findUserByUsernameSadPathWrongUsername() {
        ResponseEntity<List<Item>> response = itemController.getItemsByName("testitem1");
        assertNotNull(response);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }
}
