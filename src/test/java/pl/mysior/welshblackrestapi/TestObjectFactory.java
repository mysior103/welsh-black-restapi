package pl.mysior.welshblackrestapi;

import pl.mysior.welshblackrestapi.model.Cow;

public class TestObjectFactory {
    public static Cow Cow(String number){
        return Cow.builder()
                .number(number)
                .name(null)
                .birth_date(null)
                .mother_number(null)
                .father_number(null)
                .sex("M")
                .color("Czarny")
                .active(true)
                .build();
    }
}
