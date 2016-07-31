package com.kamabizbazti.model.handlers;

import com.kamabizbazti.model.entities.GeneralPurpose;
import com.kamabizbazti.model.entities.PurposeType;
import com.kamabizbazti.model.interfaces.IGeneralPurposeRepository;
import com.kamabizbazti.model.interfaces.IGeneralRequestHandler;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

public class GeneralRequestHandler implements IGeneralRequestHandler{
    @Autowired
    IGeneralPurposeRepository repository;
    public List<GeneralPurpose> getPurposesList() {
        Iterable<GeneralPurpose> test = repository.getAllGeneralPurposes();
       for(GeneralPurpose gp : test) {
           System.out.println(gp);
       }
        List <GeneralPurpose> purposes = new ArrayList<>();
        purposes.add(new GeneralPurpose("Food"));
        purposes.add(new GeneralPurpose("Car"));
        purposes.add(new GeneralPurpose("Gas"));
        purposes.add(new GeneralPurpose("Beer"));
        purposes.add(new GeneralPurpose("Bills"));
        purposes.add(new GeneralPurpose("Internet"));
        purposes.add(new GeneralPurpose("Travel"));
        purposes.add(new GeneralPurpose("Vodka"));
        purposes.add(new GeneralPurpose("Transportation"));
        purposes.add(new GeneralPurpose("Pubs"));
        return purposes;
    }
}
