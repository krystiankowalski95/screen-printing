package pl.lodz.it.sitodruk.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import pl.lodz.it.sitodruk.dto.AddressDTO;
import pl.lodz.it.sitodruk.exceptions.BaseException;
import pl.lodz.it.sitodruk.repositories.mok.AddressRepository;
import pl.lodz.it.sitodruk.service.AddressService;

@Service
@Transactional(propagation = Propagation.REQUIRES_NEW , transactionManager = "mokTransactionManager")
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
