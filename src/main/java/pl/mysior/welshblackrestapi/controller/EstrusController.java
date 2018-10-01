package pl.mysior.welshblackrestapi.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.mysior.welshblackrestapi.controller.util.HeaderUtil;
import pl.mysior.welshblackrestapi.model.Cow;
import pl.mysior.welshblackrestapi.model.Estrus;
import pl.mysior.welshblackrestapi.services.CowActionService;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

@RestController
@RequestMapping(path = "/cows")
public class EstrusController {


    private static final String OPERATION = "Estrus";

    @Autowired
    private CowActionService<Estrus> estrusService;

    @PostMapping(path = "/estruses")
    public ResponseEntity<Cow> addEstruses(@Valid @RequestBody Estrus estrus) throws URISyntaxException {
        if (estrus.getCowNumber() == "" || estrus.getCowNumber() == null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(OPERATION, "null", "Lack of cow number")).body(null);
        } else {
            Cow saved = estrusService.save(estrus);
            if (saved == null) {
                return ResponseEntity.notFound().headers(HeaderUtil.createFailureAlert(OPERATION, "Not found", "Cow does not exist")).build();
            } else {
                return ResponseEntity.created(new URI("/" + saved.getNumber() + "/estruses"))
                        .headers(HeaderUtil.createEntityCreationAlert(OPERATION, saved.getNumber()))
                        .body(saved);
            }
        }
    }

    @GetMapping(path = "/estruses")
    public List<Estrus> getAllEstruses() {
        return estrusService.findAll();
    }

    @GetMapping(path = "/{cowNumber}/estruses")
    public List<Estrus> getEstruses(@PathVariable String cowNumber){
        return estrusService.findByCow(cowNumber);
    }

    @GetMapping(path = "/estruses/last")
    public List<Estrus> getLastEstrus() {
        return estrusService.findLast();
    }

}
