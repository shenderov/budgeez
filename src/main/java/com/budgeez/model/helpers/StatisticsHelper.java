package com.budgeez.model.helpers;

import com.budgeez.model.entities.DataTable;
import com.budgeez.model.entities.dao.GeneralCategory;
import com.budgeez.model.interfaces.IStatisticsHelper;
import org.springframework.security.access.method.P;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class StatisticsHelper implements IStatisticsHelper {

    private static final String[] AVG_LABELS = {"Category", "Amount"};
    private static final String AVG_OTHERS = "Others";
    private static final String LABEL_MONTH_WEEK = "Month/Week";
    private static final String LABEL_MONTH = "Month";
    private static final String LABEL_YEAR = "Year";

    public List<String> setLabelsForCategories(List<GeneralCategory> categories, boolean others) {
        List<String> labels = new ArrayList<>();
        labels.add(LABEL_MONTH);
        for (GeneralCategory cat : categories)
            labels.add(cat.getName());
        if (others)
            labels.add(AVG_OTHERS);
        return labels;
    }

    public String[] setLabels() {
        return AVG_LABELS;
    }

    public DataTable deleteNullOthers(DataTable dataTable){
        List<Object []> data = dataTable.getRowsAsList();
        if(data.size() == 1 && data.get(0)[1] == null){
            data.remove(0);
            dataTable.setRows(data);
        }
        return dataTable;
    }
}