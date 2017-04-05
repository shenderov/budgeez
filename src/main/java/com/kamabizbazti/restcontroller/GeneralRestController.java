package com.kamabizbazti.restcontroller;

import com.kamabizbazti.model.entities.external.ChartRequestWrapper;
import com.kamabizbazti.model.entities.dao.ChartSelection;
import com.kamabizbazti.model.entities.external.ChartWrapper;
import com.kamabizbazti.model.entities.external.EVersion;
import com.kamabizbazti.model.exceptions.UnknownSelectionIdException;
import com.kamabizbazti.model.interfaces.IGeneralRequestHandler;
import com.kamabizbazti.model.interfaces.IGeneralRestController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RequestMapping(path = "${general.path}")
@RestController
@CrossOrigin(origins = "*")
public class GeneralRestController implements IGeneralRestController {

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
    public ChartWrapper getGeneralDataTable(@RequestBody @Valid ChartRequestWrapper chartRequestWrapper) throws UnknownSelectionIdException {
        return handler.getGeneralDatatable(chartRequestWrapper);
    }

    @CrossOrigin(origins = "*")
    @RequestMapping(value = "/${general.route.getVersion}", method = RequestMethod.GET)
    public EVersion getGeneralDataTable() {
        return handler.getVersion();
    }
}
