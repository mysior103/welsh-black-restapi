package pl.mysior.welshblackrestapi.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.mysior.welshblackrestapi.controller.util.HeaderUtil;
import pl.mysior.welshblackrestapi.model.Cow;
import pl.mysior.welshblackrestapi.model.Deworming;
import pl.mysior.welshblackrestapi.services.CowActionService;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

@RestController
@RequestMapping(path = "/cows")
public class DewormingController {

    private static final String OPERATION = "Deworming";

    @Autowired
    private CowActionService<Deworming> dewormingService;

    @PostMapping(path = "/dewormings")
    public ResponseEntity<Cow> addDeworming(@Valid @RequestBody Deworming deworming) throws URISyntaxException {
        if (deworming.getCowNumber() == "" || deworming.getCowNumber() == null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(OPERATION, "null", "Lack of cow number")).body(null);
        } else {
            Cow saved = dewormingService.save(deworming);
            if (saved == null) {
                return ResponseEntity.notFound().headers(HeaderUtil.createFailureAlert(OPERATION, "Not found", "Cow does not exist")).build();
            } else {
                return ResponseEntity.created(new URI("/" + saved.getNumber() + "/dewormings"))
                        .headers(HeaderUtil.createEntityCreationAlert(OPERATION, saved.getNumber()))
                        .body(saved);
            }
        }
    }

    @GetMapping(path = "/dewormings")
    public List<Deworming> getAllDewormings() {
        return dewormingService.findAll();
    }

    @GetMapping(path = "/{cowNumber}/dewormings")
    public List<Deworming> getDewormings(@PathVariable String cowNumber){
        return dewormingService.findByCow(cowNumber);
    }

    @GetMapping(path = "/dewormings/last")
    public List<Deworming> getLastDewormings() {
        return dewormingService.findLast();
    }


}
