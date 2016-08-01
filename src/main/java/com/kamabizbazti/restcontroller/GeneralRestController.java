package com.kamabizbazti.restcontroller;

import com.kamabizbazti.model.entities.ChartSelection;
import com.kamabizbazti.model.entities.ChartWrapper;
import com.kamabizbazti.model.entities.GeneralPurpose;
import com.kamabizbazti.model.entities.PurposeType;
import com.kamabizbazti.model.interfaces.IGeneralPurposeRepository;
import com.kamabizbazti.model.interfaces.IGeneralRequestHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

@RequestMapping(path = "general")
@RestController
public class GeneralRestController {

    @Autowired
    IGeneralRequestHandler handler;

    //move to user
    @RequestMapping(value = "/getPurposesList", method = RequestMethod.GET)
    public List<GeneralPurpose> greeting(HttpServletResponse response) {
       return handler.getPurposesList();
    }

    @RequestMapping(value = "/getGeneralChartSelectionsList", method = RequestMethod.GET)
    public List<ChartSelection> getGeneralChartSelectionsList(HttpServletResponse response) {
        return handler.getGeneralChartSelectionsList();
    }

    @RequestMapping(value = "/getDefaultDataTable", method = RequestMethod.GET)
    public ChartWrapper getDefaultDataTable(HttpServletResponse response) throws Exception {
        return handler.getDefaultDataTable();
    }

    @RequestMapping(value = "/getGeneralDataTable", method = RequestMethod.POST)
    public ChartWrapper getGeneralDataTable(@RequestBody ChartSelection selection, HttpServletResponse response) throws Exception {
        return handler.getGeneralDatatable(selection);
    }

}
