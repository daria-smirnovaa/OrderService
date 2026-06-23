package order_service.service;

import order_service.dto.ItemDto;
import order_service.entity.Item;
import order_service.mapper.ItemMapperImpl;
import order_service.repository.ItemRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ItemServiceTest {

    @Mock
    private ItemRepository itemRepository;

    @Mock
    private ItemMapperImpl itemMapper;

    @InjectMocks
    private ItemService itemService;

    public static final Long WRONG_ITEM_ID = 999L;

    private Item item1;
    private Item item2;
    private ItemDto itemDto1;
    private ItemDto itemDto2;

    @BeforeEach
    void setUp() {
        item1 = Item.builder()
                .id(1L)
                .name("Test Item 1")
                .price(100.0)
                .build();

        item2 = Item.builder()
                .id(2L)
                .name("Test Item 2")
                .price(200.0)
                .build();

        itemDto1 = ItemDto.builder()
                .id(1L)
                .name("Test Item 1")
                .price(100.0)
                .build();

        itemDto2 = ItemDto.builder()
                .id(2L)
                .name("Test Item 2")
                .price(200.0)
                .build();
    }

    @Test
    void testGetItemById() {
        when(itemRepository.findById(1L)).thenReturn(Optional.of(item1));

        Item result = itemService.getItemById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Test Item 1", result.getName());
        verify(itemRepository).findById(1L);
    }

    @Test
    void testGetItemByIdWithWrongId() {
        when(itemRepository.findById(WRONG_ITEM_ID)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> itemService.getItemById(WRONG_ITEM_ID));
        verify(itemRepository).findById(WRONG_ITEM_ID);
    }

    @Test
    void testGetItemByIdWithNullId() {
        assertThrows(EntityNotFoundException.class, () -> itemService.getItemById(null));
        verify(itemRepository, never()).findById(anyLong());
    }

    @Test
    void testGetAllItemsWhenItemsExist() {
        List<Item> items = Arrays.asList(item1, item2);
        when(itemRepository.findAll()).thenReturn(items);
        when(itemMapper.toDto(item1)).thenReturn(itemDto1);
        when(itemMapper.toDto(item2)).thenReturn(itemDto2);

        List<ItemDto> result = itemService.getAllItems();

        assertThat(result).isNotNull();
        assertThat(result).hasSize(2);
        assertThat(result).containsExactly(itemDto1, itemDto2);

        verify(itemRepository, times(1)).findAll();
        verify(itemMapper, times(1)).toDto(item1);
        verify(itemMapper, times(1)).toDto(item2);
        verifyNoMoreInteractions(itemRepository, itemMapper);
    }

    @Test
    void testGetAllItemsWhenNoItemsExist() {
        when(itemRepository.findAll()).thenReturn(Collections.emptyList());

        List<ItemDto> result = itemService.getAllItems();

        assertThat(result).isNotNull();
        assertThat(result).isEmpty();
        verify(itemRepository, times(1)).findAll();
        verify(itemMapper, never()).toDto(any(Item.class));
    }

    @Test
    void testGetAllItemsWhenOnlyOneItemExists() {
        List<Item> items = Collections.singletonList(item1);
        when(itemRepository.findAll()).thenReturn(items);
        when(itemMapper.toDto(item1)).thenReturn(itemDto1);

        List<ItemDto> result = itemService.getAllItems();

        assertThat(result).isNotNull();
        assertThat(result).hasSize(1);
        assertThat(result.getFirst()).isEqualTo(itemDto1);
        verify(itemRepository, times(1)).findAll();
        verify(itemMapper, times(1)).toDto(item1);
    }
}