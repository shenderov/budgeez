package com.budgeez.model.interfaces;

import com.budgeez.model.entities.DataTable;
import com.budgeez.model.entities.dao.GeneralCategory;

import java.util.List;

public interface IStatisticsHelper {
    List<String> setLabelsForCategories(List<GeneralCategory> categories, boolean others);

    String[] setLabels();

    DataTable deleteNullOthers(DataTable dataTable);
}