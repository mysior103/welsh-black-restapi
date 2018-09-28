package pl.mysior.welshblackrestapi.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
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

    private static final Logger logger = LogManager.getLogger(BloodTestController.class);

    private static final String OPERATION = "BloodTest";

    @Autowired
    private BloodTestService bloodTestservice;

    @PostMapping(path = "/bloodtests")
    public ResponseEntity<Cow> addBloodTest(@Valid @RequestBody BloodTest bloodTest) throws URISyntaxException {
        if (bloodTest.getCowNumber() == "" || bloodTest.getCowNumber() == null) {
            logger.error("POST Lack of cow number");
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(OPERATION, "null", "Lack of cow number")).body(null);
        } else {
            Cow saved = bloodTestservice.save(bloodTest);
            if (saved == null) {
                logger.warn("POST Cow with number " + bloodTest.getCowNumber() + " couldn't find");
                return ResponseEntity.notFound().headers(HeaderUtil.createFailureAlert(OPERATION, "Not found", "Cow does not exist")).build();
            } else {
                logger.info("POST Blood test for cow " + bloodTest.getCowNumber() + " has been added");
                return ResponseEntity.created(new URI("/" + saved.getNumber() + "/bloodtests"))
                        .headers(HeaderUtil.createEntityCreationAlert(OPERATION, saved.getNumber()))
                        .body(saved);
            }
        }
    }

    @GetMapping(path = "/bloodtests")
    public List<BloodTest> getAllBloodTests() {
        logger.info("GET List of all blood tests has been generated");
        return bloodTestservice.findAll();
    }

    @GetMapping(path = "/{cowNumber}/bloodtests")
    public List<BloodTest> getBloodTests(@PathVariable String cowNumber) {
        logger.info("GET List of all blood tests for "+cowNumber + " has been generated");
        return bloodTestservice.findByCow(cowNumber);
    }

    @GetMapping(path = "/bloodtests/last")
    public List<BloodTest> getLastBloodTests() {
        logger.info("GET List of latest blood tests has been generated");
        return bloodTestservice.findLast();
    }

}
