package pl.lodz.it.sitodruk.service;

import pl.lodz.it.sitodruk.dto.AddressDTO;
import pl.lodz.it.sitodruk.exceptions.BaseException;

public interface AddressService {
    void createAddress(AddressDTO addressDTO) throws BaseException;
    void modifyAddress(AddressDTO addressDTO) throws BaseException;
    AddressDTO findAddressByUsername(String username) throws BaseException;
}
