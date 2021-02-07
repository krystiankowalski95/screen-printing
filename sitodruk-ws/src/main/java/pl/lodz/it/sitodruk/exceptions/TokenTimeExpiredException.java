package pl.lodz.it.sitodruk.exceptions;

public class TokenTimeExpiredException extends BaseException{

    private static final String message = "token.time.expired";


    public TokenTimeExpiredException(){
        super(message);
    }
}
