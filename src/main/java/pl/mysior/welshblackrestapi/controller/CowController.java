package pl.mysior.welshblackrestapi.controller;

import io.swagger.annotations.ApiOperation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.mysior.welshblackrestapi.controller.util.ResponseUtil;
import pl.mysior.welshblackrestapi.model.Cow;
import pl.mysior.welshblackrestapi.services.CowService;


import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(path = "/cows")
public class CowController {
    private static final Logger logger = LogManager.getLogger(CowController.class);
    private final CowService cowService;

    @Autowired
    public CowController(CowService cowService) {
        this.cowService = cowService;
    }

    @PostMapping
    public ResponseEntity<Cow> addCow(@Valid @RequestBody Cow cow) throws URISyntaxException {

        Cow saved = cowService.save(cow);
        HttpHeaders header = new HttpHeaders();
        header.add("Method", "Created");
        logger.info("POST Cow with number" + cow.getNumber() + "has been created");
        return ResponseEntity.created(new URI("/cows/" + saved.getNumber()))
                .headers(header)
                .body(saved);
    }

    @GetMapping()
    public List<Cow> getAllCows() {
        logger.info("GET List of all Cows has been returned");
        return cowService.findAll();
    }

    @ApiOperation(value = "This is a list of all Cows")
    @GetMapping("/{number}")
    public ResponseEntity<Cow> getCow(@PathVariable String number) {
        Cow foundCow = cowService.findByNumber(number);
        logger.info("GET Cow with number" + number + "has been returned");
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(foundCow));
    }

    @PutMapping
    public ResponseEntity<Cow> updateCow(@Valid @RequestBody Cow cow) throws URISyntaxException {
        if (cow.getNumber() != "") {
            Cow foundCow = cowService.findByNumber(cow.getNumber());
            if (foundCow != null) {
                cowService.save(cow);
                logger.info("PUT Cow with number" + cow.getNumber() + "has been changed");
            } else {
                addCow(cow);
                logger.warn("PUT Cow with number" + cow.getNumber() + "has been created");
            }
            HttpHeaders header = new HttpHeaders();
            header.add("Method", "Updated");
            return ResponseEntity.ok()
                    .headers(header)
                    .body(cow);
        } else {
            logger.error("PUT Couldn't find cow with number" + cow.getNumber());
            return ResponseEntity.badRequest().build();
        }
    }
}
