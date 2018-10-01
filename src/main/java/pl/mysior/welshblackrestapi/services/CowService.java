package pl.mysior.welshblackrestapi.services;

        import pl.mysior.welshblackrestapi.model.Cow;

        import java.util.List;
public interface CowService {
    Cow save(Cow cow);

    List<Cow> findAll();

    Cow findByNumber(String number);

    Cow deleteByNumber(String number);

    List<Cow> findAllChildren(String motherNumber);
}
