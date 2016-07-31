package com.kamabizbazti.restcontroller;

import com.kamabizbazti.model.entities.GeneralPurpose;
import com.kamabizbazti.model.entities.PurposeType;
import com.kamabizbazti.model.interfaces.IGeneralPurposeRepository;
import com.kamabizbazti.model.interfaces.IGeneralRequestHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequestMapping(path = "general")
@RestController
public class GeneralRestController {

    @Autowired
    IGeneralRequestHandler handler;

    @RequestMapping(value = "/getPurpose", method = RequestMethod.GET)
    public List<GeneralPurpose> greeting() {
       return handler.getPurposesList();
    }
}
