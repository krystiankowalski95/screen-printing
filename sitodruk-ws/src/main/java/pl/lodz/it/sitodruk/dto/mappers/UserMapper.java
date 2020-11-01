package pl.lodz.it.sitodruk.dto.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;
import pl.lodz.it.sitodruk.dto.UserDTO;
import pl.lodz.it.sitodruk.model.UserAccessLevelEntity;
import pl.lodz.it.sitodruk.model.UserEntity;

import java.util.ArrayList;
import java.util.Collection;
import java.util.stream.Collectors;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE,componentModel = "spring")
public interface UserMapper {

    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    UserDTO toUserDTO(UserEntity userEntity);
    UserEntity createNewUser(UserDTO userDTO);
    Collection<UserDTO> toUserDTOCollection(Collection<UserEntity> userEntities);

    default Collection<String> toAccessLevelStringCollection(Collection<UserAccessLevelEntity> accessLevelCollection) {
        return accessLevelCollection.stream()
                .map(UserAccessLevelEntity::getAccessLevelName)
                .collect(Collectors.toList());
    }
    default Collection<UserAccessLevelEntity> toUserAccessLevelCollection(Collection<String> userAccessLevelStringCollection) {
        return new ArrayList<>();
    }

}
