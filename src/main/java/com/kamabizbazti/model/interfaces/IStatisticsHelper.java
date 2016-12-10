package com.kamabizbazti.model.interfaces;

import com.kamabizbazti.model.entities.GeneralPurpose;

import java.util.List;

public interface IStatisticsHelper {
    List<String> setLabelsForPurposes(List<GeneralPurpose> purposes, boolean others);

    String[] setLabels();
}