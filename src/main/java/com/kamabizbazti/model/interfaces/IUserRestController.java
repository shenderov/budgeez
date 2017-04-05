package com.kamabizbazti.model.interfaces;

import com.kamabizbazti.model.entities.dao.GeneralCategory;
import com.kamabizbazti.model.entities.dao.Record;
import com.kamabizbazti.model.entities.external.*;
import com.kamabizbazti.model.exceptions.*;
import org.springframework.data.domain.Page;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface IUserRestController {

    List<GeneralCategory> getCategoriesList(HttpServletRequest request);

    ChartWrapper getUserDefaultDataTable(HttpServletRequest request) throws UnknownSelectionIdException, DateRangeException;

    ChartWrapper getGeneralDataTable(ChartRequestWrapper chartRequestWrapper, HttpServletRequest request) throws UnknownSelectionIdException, DateRangeException;

    Record addRecord(ERecord record, HttpServletRequest request);

    GeneralCategory addCustomCategory(ECustomCategory newCategory, HttpServletRequest request) throws CustomCategoryAlreadyExistException, InvalidParameterException;

    Page<Record> getRecordsList(DatePicker datePicker, HttpServletRequest request);

    boolean deleteRecord(long recordId, HttpServletRequest request) throws RecordDoesNotExistException;

    Record editRecord(EditRecordWrapper record, HttpServletRequest request) throws RecordDoesNotExistException, InvalidParameterException;

}
