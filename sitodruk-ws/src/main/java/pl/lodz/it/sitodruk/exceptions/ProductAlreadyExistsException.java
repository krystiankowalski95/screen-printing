package pl.lodz.it.sitodruk.exceptions;

public class ProductAlreadyExistsException extends BaseException{
    private final static String msg = "product.already.exists";


    public ProductAlreadyExistsException(){
        super(msg);
    }
}
