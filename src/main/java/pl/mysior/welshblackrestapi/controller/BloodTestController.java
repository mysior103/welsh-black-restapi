package pl.mysior.welshblackrestapi.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.Mapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import pl.mysior.welshblackrestapi.controller.util.HeaderUtil;
import pl.mysior.welshblackrestapi.model.BloodTest;
import pl.mysior.welshblackrestapi.model.Cow;
import pl.mysior.welshblackrestapi.services.BloodTestService;
import pl.mysior.welshblackrestapi.services.CowService;

import javax.validation.Valid;

@Controller
public class BloodTestController {

    @Autowired
    private BloodTestService bloodTestservice;

    @Autowired
    private CowService cowService;


    @PostMapping(path = "/bloodtests")
    public ResponseEntity<Cow> save(@Valid @RequestBody BloodTest bloodTest){
        if(bloodTest==null){
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("BloodTest","null","Lack of request body")).body(null);
        }else if(bloodTest.getCowNumber()=="" || bloodTest.getCowNumber()==null){
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("BloodTest","null","Lack of cow number")).body(null);
        }else{
            Cow saved = bloodTestservice.save(bloodTest);
            if(saved==null){
                return ResponseEntity.notFound().headers(HeaderUtil.createFailureAlert("BloodTest","Not found", "Cow does not exist")).build();
            }
        }
        return null;
    }
}
