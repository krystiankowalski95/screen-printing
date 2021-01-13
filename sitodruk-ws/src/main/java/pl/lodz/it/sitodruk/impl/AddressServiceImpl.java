package pl.lodz.it.sitodruk.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import pl.lodz.it.sitodruk.dto.AddressDTO;
import pl.lodz.it.sitodruk.exceptions.BaseException;
import pl.lodz.it.sitodruk.repositories.moz.AddressRepository;
import pl.lodz.it.sitodruk.repositories.mok.UserRepository;
import pl.lodz.it.sitodruk.service.AddressService;

@Service
@Transactional(isolation = Isolation.READ_COMMITTED,propagation = Propagation.REQUIRES_NEW, transactionManager = "mokTransactionManager")
public class AddressServiceImpl implements AddressService {

    @Autowired
    private AddressRepository addressRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public void createAddress(AddressDTO addressDTO) throws BaseException {
//        if (userRepository.findByUsername(addressDTO.getUsername()).isPresent()) {
//            Collection<AddressEntity> addresses = userRepository.findByUsername(addressDTO.getUsername()).get().getAddresses();
//            for (AddressEntity address : addresses) {
//                if (isAddressEqual(address,addressDTO)){
//                    throw new AddressAlreadyExistsException();
//                }
//            }
//        }else{
//            throw new UserNotFoundException();
//        }
    }
}
