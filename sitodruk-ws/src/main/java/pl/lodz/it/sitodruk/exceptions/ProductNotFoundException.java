package pl.lodz.it.sitodruk.exceptions;

public class ProductNotFoundException extends BaseException {
    private final static String msg = "product.not.found";

    public ProductNotFoundException(){
        super(msg);
    }
}
