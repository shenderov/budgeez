package com.kamabizbazti.restcontroller;

import com.kamabizbazti.model.entities.ChartRequestWrapper;
import com.kamabizbazti.model.entities.ChartSelection;
import com.kamabizbazti.model.entities.ChartWrapper;
import com.kamabizbazti.model.interfaces.IGeneralRequestHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RequestMapping(path = "${general.path}")
@RestController
@CrossOrigin(origins = "*")
public class GeneralRestController {

    @Autowired
    private IGeneralRequestHandler handler;

    @CrossOrigin(origins = "*")
    @RequestMapping(value = "/${general.route.getGeneralChartSelectionsList}", method = RequestMethod.GET)
    public List<ChartSelection> getGeneralChartSelectionsList() {
        return handler.getGeneralChartSelectionsList();
    }

    @CrossOrigin(origins = "*")
    @RequestMapping(value = "/${general.route.getUserChartSelectionsList}", method = RequestMethod.GET)
    public List<ChartSelection> getUserChartSelectionsList() {
        return handler.getUserChartSelectionsList();
    }

    @CrossOrigin(origins = "*")
    @RequestMapping(value = "/${general.route.getDefaultDataTable}", method = RequestMethod.GET)
    public ChartWrapper getDefaultDataTable() throws Exception {
        return handler.getDefaultDataTable();
    }

    @CrossOrigin(origins = "*")
    @RequestMapping(value = "/${general.route.getGeneralDataTable}", method = RequestMethod.POST)
    public ChartWrapper getGeneralDataTable(@RequestBody ChartRequestWrapper chartRequestWrapper) {
        return handler.getGeneralDatatable(chartRequestWrapper);
    }
}
