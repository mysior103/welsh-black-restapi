package pl.mysior.welshblackrestapi.controller;

import io.swagger.annotations.ApiOperation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.mysior.welshblackrestapi.exception.CowNotFoundException;
import pl.mysior.welshblackrestapi.model.Cow;
import pl.mysior.welshblackrestapi.services.CowService;
import pl.mysior.welshblackrestapi.services.DTO.CowDTO;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

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
    public ResponseEntity<Void> addCow(@Valid @RequestBody CowDTO cowDTO) throws URISyntaxException {

        cowService.save(cowDTO);
        HttpHeaders header = new HttpHeaders();
        header.add("Method", "Created");
        logger.info("Cow with number" + cowDTO.getNumber() + "has been created");
        return ResponseEntity.created(new URI("/cows/" + cowDTO.getNumber()))
                .headers(header).build();
    }

    @GetMapping()
    public List<Cow> getAllCows() {
        logger.info("GET List of all Cows has been returned");
        return cowService.findAll();
    }

    @ApiOperation(value = "This is a list of all Cows")
    @GetMapping("/{number}")
    public ResponseEntity<CowDTO> getCow(@PathVariable String number) {
        try {
            CowDTO foundCow = cowService.findByNumber(number);
            logger.info("Found and return cow with number: ", number);
            return ResponseEntity.ok(foundCow);

        } catch (CowNotFoundException e) {
            logger.info("Could not find cow with number: ", number);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

    }

    @PutMapping
    public ResponseEntity<Void> updateCow(@Valid @RequestBody CowDTO cowDTO) throws URISyntaxException {
        if (!cowDTO.getNumber().equals("")) {

            HttpHeaders header = new HttpHeaders();
            header.add("Method", "Updated");
            logger.info("Cow with number" + cowDTO.getNumber() + "has been updated");
            return ResponseEntity.status(HttpStatus.OK)
                    .headers(header).build();
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

    }


    @GetMapping("/{parentNumber}/children")
    public List<Cow> getChildren(@Valid @PathVariable String parentNumber) {
        List<Cow> children = cowService.findAllChildren(parentNumber);
        logger.info("GET List of all children for cow " + parentNumber + " has been generated");
        return children;
    }

    //TODO:
    /*
    Get predicted date of birth
     */
}
