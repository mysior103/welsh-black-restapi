package pl.mysior.welshblackrestapi.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.mysior.welshblackrestapi.controller.util.HeaderUtil;
import pl.mysior.welshblackrestapi.model.BloodTest;
import pl.mysior.welshblackrestapi.model.Cow;
import pl.mysior.welshblackrestapi.services.BloodTestService;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

@RestController
@RequestMapping(path = "/cows")
public class BloodTestController {

    private static final String OPERATION = "Bloodtest";

    @Autowired
    private BloodTestService bloodTestservice;

    @PostMapping(path = "/bloodtests")
    public ResponseEntity<Cow> save(@Valid @RequestBody BloodTest bloodTest) throws URISyntaxException {
        if (bloodTest.getCowNumber() == "" || bloodTest.getCowNumber() == null)
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(OPERATION, "null", "Lack of cow number")).body(null);
        else {
            Cow saved = bloodTestservice.save(bloodTest);
            if (saved == null) {
                return ResponseEntity.notFound().headers(HeaderUtil.createFailureAlert(OPERATION, "Not found", "Cow does not exist")).build();
            } else {
                return ResponseEntity.created(new URI("/" + saved.getNumber() + "/bloodtests"))
                        .headers(HeaderUtil.createEntityCreationAlert(OPERATION, saved.getNumber()))
                        .body(saved);
            }
        }
    }

    @GetMapping(path = "/bloodtests")
    public List<BloodTest> getAllBloodTests() {
        return bloodTestservice.findAll();
    }

    @GetMapping(path = "/{cowNumber}/bloodtests")
    public List<BloodTest> getBloodTests(@PathVariable String cowNumber) {
        return bloodTestservice.findByCow(cowNumber);
    }

    @GetMapping(path = "/bloodtests/last")
    public List<BloodTest> getLastBloodTests(){
        return bloodTestservice.findLast();
    }

}
