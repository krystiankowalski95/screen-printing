package pl.lodz.it.sitodruk.exceptions;

public class ApplicationOptimisticLockException extends BaseException{

    private static final String message = "optimistic.lock";


    public ApplicationOptimisticLockException(){
        super(message);
    }
}
