package com.kamabizbazti.model.helpers;

import com.kamabizbazti.model.entities.GeneralPurpose;
import com.kamabizbazti.model.interfaces.IStatisticsHelper;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class StatisticsHelper implements IStatisticsHelper {

    private static final String [] AVG_LABELS = {"Purpose",  "Amount"};
    private static final String AVG_OTHERS = "Others";
    private static final String LABEL_MONTH_WEEK = "Month/Week";
    private static final String LABEL_MONTH = "Month";
    private static final String LABEL_YEAR = "Year";

    public List<String> setLabelsForPurposes(List<GeneralPurpose> purposes, boolean others) {
        List <String> labels = new ArrayList<>();
        labels.add(LABEL_MONTH);
        for(GeneralPurpose pps : purposes)
            labels.add(pps.getName());
        if(others)
            labels.add(AVG_OTHERS);
        return labels;
    }

    public String [] setLabels (){
        return AVG_LABELS;
    }
}