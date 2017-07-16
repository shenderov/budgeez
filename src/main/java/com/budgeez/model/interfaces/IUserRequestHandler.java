package com.budgeez.model.interfaces;

import com.budgeez.model.entities.dao.CustomCategory;
import com.budgeez.model.entities.dao.GeneralCategory;
import com.budgeez.model.entities.dao.Record;
import com.budgeez.model.entities.external.*;
import com.budgeez.model.exceptions.*;
import com.budgeez.model.entities.dao.User;
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