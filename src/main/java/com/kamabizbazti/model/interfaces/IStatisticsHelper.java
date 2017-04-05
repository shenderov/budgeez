package com.kamabizbazti.model.interfaces;

import com.kamabizbazti.model.entities.dao.GeneralCategory;

import java.util.List;

public interface IStatisticsHelper {
    List<String> setLabelsForCategories(List<GeneralCategory> categories, boolean others);

    String[] setLabels();
}