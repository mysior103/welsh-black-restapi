package pl.mysior.welshblackrestapi.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.mysior.welshblackrestapi.controller.util.HeaderUtil;
import pl.mysior.welshblackrestapi.model.Cow;
import pl.mysior.welshblackrestapi.model.Vaccine;
import pl.mysior.welshblackrestapi.services.CowActionService;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

@RestController
@RequestMapping(path = "/cows")
public class VaccineController {


    private static final String OPERATION = "Vaccine";

    @Autowired
    private CowActionService<Vaccine> vaccineService;

    @PostMapping(path = "/vaccines")
    public ResponseEntity<Cow> addVaccine(@Valid @RequestBody Vaccine vaccine) throws URISyntaxException {
        if (vaccine.getCowNumber() == "" || vaccine.getCowNumber() == null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(OPERATION, "null", "Lack of cow number")).body(null);
        } else {
            Cow saved = vaccineService.save(vaccine);
            if (saved == null) {
                return ResponseEntity.notFound().headers(HeaderUtil.createFailureAlert(OPERATION, "Not found", "Cow does not exist")).build();
            } else {
                return ResponseEntity.created(new URI("/" + saved.getNumber() + "/vaccines"))
                        .headers(HeaderUtil.createEntityCreationAlert(OPERATION, saved.getNumber()))
                        .body(saved);
            }
        }
    }

    @GetMapping(path = "/vaccines")
    public List<Vaccine> getAllVaccines() {
        return vaccineService.findAll();
    }

    @GetMapping(path = "/{cowNumber}/vaccines")
    public List<Vaccine> getVaccines(@PathVariable String cowNumber){
        return vaccineService.findByCow(cowNumber);
    }

    @GetMapping(path = "/vaccines/last")
    public List<Vaccine> getLastVaccines() {
        return vaccineService.findLast();
    }
}
