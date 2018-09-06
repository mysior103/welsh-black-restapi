package pl.mysior.welshblackrestapi.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import pl.mysior.welshblackrestapi.controller.util.HeaderUtil;
import pl.mysior.welshblackrestapi.model.BloodTest;
import pl.mysior.welshblackrestapi.model.Cow;
import pl.mysior.welshblackrestapi.services.BloodTestService;
import pl.mysior.welshblackrestapi.services.CowService;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Optional;

@Controller
public class BloodTestController {

    private static final String OPERATION = "Bloodtest";

    @Autowired
    private BloodTestService bloodTestservice;

    @Autowired
    private CowService cowService;


    @PostMapping(path = "/bloodtests")
    public ResponseEntity<Cow> save(@Valid @RequestBody BloodTest bloodTest) throws URISyntaxException {
        if(bloodTest==null){
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(OPERATION,"null","Lack of request body")).body(null);
        }else if(bloodTest.getCowNumber()=="" || bloodTest.getCowNumber()==null){
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(OPERATION,"null","Lack of cow number")).body(null);
        }else{
            Cow saved = bloodTestservice.save(bloodTest);
            if(saved==null){
                return ResponseEntity.notFound().headers(HeaderUtil.createFailureAlert(OPERATION,"Not found", "Cow does not exist")).build();
            }else{
                return ResponseEntity.created(new URI("/" + saved.getNumber() + "/bloodtests"))
                        .headers(HeaderUtil.createEntityCreationAlert(OPERATION,saved.getNumber()))
                        .body(saved);
            }
        }
    }
}
