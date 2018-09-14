package pl.mysior.welshblackrestapi.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.mysior.welshblackrestapi.controller.util.HeaderUtil;
import pl.mysior.welshblackrestapi.model.Cow;
import pl.mysior.welshblackrestapi.model.Deworming;
import pl.mysior.welshblackrestapi.services.DewormingService;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

@RestController
@RequestMapping(path = "/cows")
public class DewormingController {

    private static final String OPERATION = "Deworming";

    @Autowired
    private DewormingService dewormingService;

    @PostMapping(path = "/dewormings")
    public ResponseEntity<Cow> addDeworming(@Valid @RequestBody Deworming deworming) throws URISyntaxException {
        if (deworming.getCowNumber() == "" || deworming.getCowNumber() == null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(OPERATION, "null", "Lack of cow number")).body(null);
        } else {
            Cow saved = dewormingService.save(deworming);
            if (saved == null) {
                return ResponseEntity.notFound().headers(HeaderUtil.createFailureAlert(OPERATION, "Not found", "Cow does not exist")).build();
            } else {
                return ResponseEntity.created(new URI("/" + saved.getNumber() + "/bloodtests"))
                        .headers(HeaderUtil.createEntityCreationAlert(OPERATION, saved.getNumber()))
                        .body(saved);
            }
        }
    }
}
