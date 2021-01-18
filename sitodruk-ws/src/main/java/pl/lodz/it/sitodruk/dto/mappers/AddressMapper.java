package pl.lodz.it.sitodruk.dto.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;
import pl.lodz.it.sitodruk.dto.AddressDTO;
import pl.lodz.it.sitodruk.model.moz.AddressEntity;

import java.util.Collection;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface AddressMapper {

    AddressMapper INSTANCE = Mappers.getMapper(AddressMapper.class);
    AddressDTO toAddressDTO(AddressEntity addressEntity);
    AddressEntity createNewAddress(AddressDTO addressDTO);
    Collection<AddressDTO> toAddressDTOCollection(Collection<AddressEntity> productEntities);
}
