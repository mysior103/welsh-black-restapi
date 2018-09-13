package pl.mysior.welshblackrestapi;

import pl.mysior.welshblackrestapi.model.Cow;

public class TestObjectFactory {
    public static Cow Cow(String number){
        return new Cow(number,null,null,null,null,"M","Czarny",true);
    }
}
