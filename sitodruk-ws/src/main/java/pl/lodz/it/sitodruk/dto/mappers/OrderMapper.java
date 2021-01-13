package pl.lodz.it.sitodruk.dto.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;
import pl.lodz.it.sitodruk.dto.OrderDTO;
import pl.lodz.it.sitodruk.model.moz.OrderEntity;
import pl.lodz.it.sitodruk.model.moz.OrderStatusEntity;

import java.util.Collection;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface OrderMapper {

    OrderMapper INSTANCE = Mappers.getMapper(OrderMapper.class);
    OrderDTO toOrderDTO(OrderEntity orderEntity);
    Collection<OrderDTO> toOrderDTOCollection(Collection<OrderEntity> productEntities);
    default String toStringOrderStatus(OrderStatusEntity orderStatusEntity){
       return orderStatusEntity.getStatusName();
    }
    default OrderStatusEntity toOrderStatusEntity(String orderStatusDto){
        OrderStatusEntity orderStatusEntity = new OrderStatusEntity();
        orderStatusEntity.setStatusName(orderStatusDto);
        return orderStatusEntity;
    }

}
