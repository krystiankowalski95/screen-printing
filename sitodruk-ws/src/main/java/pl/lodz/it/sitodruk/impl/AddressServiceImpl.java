package pl.lodz.it.sitodruk.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.lodz.it.sitodruk.dto.AddressDTO;
import pl.lodz.it.sitodruk.exceptions.BaseException;
import pl.lodz.it.sitodruk.repositories.AddressRepository;
import pl.lodz.it.sitodruk.service.AddressService;

@Service
public class AddressServiceImpl implements AddressService {

    @Autowired
    private AddressRepository addressRepository;

    @Override
    public void createAddress(AddressDTO addressDTO) throws BaseException {

    }

    @Override
    public void modifyAddress(AddressDTO addressDTO) throws BaseException {

    }

    @Override
    public AddressDTO findAddressByUsername(String username) throws BaseException {
        return null;
    }
}
