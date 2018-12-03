package pl.mysior.welshblackrestapi.services;

import pl.mysior.welshblackrestapi.exception.CowNotFoundException;
import pl.mysior.welshblackrestapi.model.Cow;
import pl.mysior.welshblackrestapi.services.DTO.CowDTO;

import java.util.List;
public interface CowService {
    void save(CowDTO cowDTO);

    void update(CowDTO cowDTO) throws CowNotFoundException;

    List<Cow> findAll();

    CowDTO findByNumber(String number) throws CowNotFoundException;

    Cow deleteByNumber(String number);

    List<Cow> findAllChildren(String motherNumber);
}
