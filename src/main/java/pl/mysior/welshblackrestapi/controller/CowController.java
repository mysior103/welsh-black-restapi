package pl.mysior.welshblackrestapi.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.mysior.welshblackrestapi.model.Cow;
import pl.mysior.welshblackrestapi.services.CowService;


import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

@RestController
@RequestMapping(path = "/cows")
public class CowController {

    final CowService cowService;

    @Autowired
    public CowController(CowService cowService) {
        this.cowService = cowService;
    }

    @PostMapping
    public ResponseEntity<Cow> addCow(@Valid @RequestBody Cow cow) throws URISyntaxException {

        Cow saved = cowService.save(cow);
        HttpHeaders header = new HttpHeaders();
        header.add("Method","Created");
        return ResponseEntity.created(new URI("/cows" + saved.getNumber()))
                .headers(header)
                .body(saved);
    }

    @RequestMapping(method = RequestMethod.GET)
    public List<Cow> getAllCows() {
        return cowService.findAll();
    }

    @RequestMapping(method = RequestMethod.PUT)
    public void updateCow(Cow cow) {
        cowService.save(cow);
    }

}
