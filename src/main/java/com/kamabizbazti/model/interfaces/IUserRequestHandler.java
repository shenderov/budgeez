package com.kamabizbazti.model.interfaces;

import com.kamabizbazti.model.dao.CustomCategory;
import com.kamabizbazti.model.dao.GeneralCategory;
import com.kamabizbazti.model.dao.Record;
import com.kamabizbazti.model.entities.*;
import com.kamabizbazti.model.exceptions.*;
import com.kamabizbazti.model.dao.User;
import org.springframework.data.domain.Page;

import java.util.List;

public interface IUserRequestHandler {
    List<GeneralCategory> getCategoriesList(long userId);

    ChartWrapper getUserDefaultDataTable(long userId) throws UnknownSelectionIdException, DateRangeException;

    ChartWrapper getUserDataTable(ChartRequestWrapper chartRequestWrapper, long userId) throws UnknownSelectionIdException, DateRangeException;

    Record addRecord(ERecord record, User user);

    GeneralCategory addCustomCategory(CustomCategory customCategory) throws CustomCategoryAlreadyExistException, InvalidParameterException;

    Page<Record> getRecordsList(DatePicker datePicker, long userId);

    boolean deleteRecord(long recordId, long userId) throws RecordDoesNotExistException;

    Record editRecord(EditRecordWrapper record, long userId) throws RecordDoesNotExistException, InvalidParameterException;
}