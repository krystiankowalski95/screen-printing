package pl.lodz.it.sitodruk.exceptions;

public class TokenExpiredException extends BaseException{

    private static final String message = "token.expired";


    public TokenExpiredException(){
        super(message);
    }
}
