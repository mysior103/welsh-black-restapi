package pl.mysior.welshblackrestapi.services.Mapper;

import pl.mysior.welshblackrestapi.model.Cow;
import pl.mysior.welshblackrestapi.services.DTO.CowDTO;

public class CowMapper {
    public static Cow toEntity(CowDTO cowDTO) {
        Cow cow = new Cow();
        cow.setNumber(cowDTO.getNumber());
        cow.setName(cowDTO.getName());
        cow.setBirthDate(cowDTO.getBirthDate());
        cow.setMotherNumber(cowDTO.getMotherNumber());
        cow.setFatherNumber(cowDTO.getFatherNumber());
        cow.setSex(cowDTO.getSex());
        cow.setColor(cowDTO.getColor());
        cow.setActive(cowDTO.isActive());
        return cow;
    }

    public static CowDTO toDto(Cow cow) {
        CowDTO cowDTO = new CowDTO();
        cowDTO.setNumber(cow.getNumber());
        cowDTO.setName(cow.getName());
        cowDTO.setBirthDate(cow.getBirthDate());
        cowDTO.setMotherNumber(cow.getMotherNumber());
        cowDTO.setFatherNumber(cow.getFatherNumber());
        cowDTO.setSex(cow.getSex());
        cowDTO.setColor(cow.getColor());
        cowDTO.setActive(cow.isActive());
        return cowDTO;
    }
}
