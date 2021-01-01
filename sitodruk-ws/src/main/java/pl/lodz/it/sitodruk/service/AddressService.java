package pl.lodz.it.sitodruk.service;

import pl.lodz.it.sitodruk.dto.AddressDTO;
import pl.lodz.it.sitodruk.exceptions.BaseException;

import java.util.Collection;

public interface AddressService {
    void createAddress(AddressDTO addressDTO) throws BaseException;
}
