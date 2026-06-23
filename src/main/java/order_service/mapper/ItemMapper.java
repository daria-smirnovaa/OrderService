package order_service.mapper;

import order_service.dto.ItemDto;
import order_service.dto.OrderItemRequestDto;
import order_service.dto.OrderRequestDto;
import order_service.dto.OrderResponseDto;
import order_service.entity.Item;
import order_service.entity.Order;
import order_service.entity.OrderItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;

import java.util.Collections;
import java.util.List;

@Mapper(componentModel = "spring", unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface ItemMapper {

    ItemDto toDto(Item item);

    Item toEntity(ItemDto itemDto);
}
