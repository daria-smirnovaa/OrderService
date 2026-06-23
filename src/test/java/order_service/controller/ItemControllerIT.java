package order_service.controller;

import order_service.dto.ItemDto;
import order_service.entity.Item;
import order_service.repository.ItemRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class ItemControllerIT extends BaseIT {

    @Autowired
    private ItemRepository itemRepository;

    @BeforeEach
    void setUp() {
        itemRepository.deleteAll();
    }

    @Test
    void getAllItems_ShouldReturnAllItems_WhenItemsExist() throws Exception {
        Item item1 = Item.builder()
                .name("Item 1")
                .price(100.0)
                .build();

        Item item2 = Item.builder()
                .name("Item 2")
                .price(200.0)
                .build();

        itemRepository.saveAll(Arrays.asList(item1, item2));

        String response = mockMvc.perform(get("/items")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].name").value("Item 1"))
                .andExpect(jsonPath("$[0].price").value(100.0))
                .andExpect(jsonPath("$[1].name").value("Item 2"))
                .andExpect(jsonPath("$[1].price").value(200.0))
                .andReturn()
                .getResponse()
                .getContentAsString();

        List<ItemDto> items = Arrays.asList(objectMapper.readValue(response, ItemDto[].class));
        assertThat(items).hasSize(2);
        assertThat(items).extracting(ItemDto::getName).containsExactly("Item 1", "Item 2");
    }

    @Test
    void getAllItems_ShouldReturnEmptyArray_WhenNoItemsExist() throws Exception {
        String response = mockMvc.perform(get("/items")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(0))
                .andReturn()
                .getResponse()
                .getContentAsString();

        List<ItemDto> items = Arrays.asList(objectMapper.readValue(response, ItemDto[].class));
        assertThat(items).isEmpty();
    }

    @Test
    void getAllItems_ShouldReturnCorrectHttpStatus() throws Exception {
        mockMvc.perform(get("/items"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }
}