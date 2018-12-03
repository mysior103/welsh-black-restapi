package pl.mysior.welshblackrestapi.exception;

public class CowNotFoundException extends Exception {

    public CowNotFoundException(String number) {
        super("Could not find cow with numer: " + number);
    }
}
