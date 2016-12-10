package com.kamabizbazti.model.interfaces;

import com.kamabizbazti.model.entities.*;
import com.kamabizbazti.model.exceptions.*;
import com.kamabizbazti.security.entities.User;
import org.springframework.data.domain.Page;

import java.util.List;

public interface IUserRequestHandler {
    List<GeneralPurpose> getPurposesList(long userId);

    ChartWrapper getUserDefaultDataTable(long userId) throws UnknownSelectionIdException, DateRangeException;

    ChartWrapper getUserDataTable(ChartRequestWrapper chartRequestWrapper, long userId) throws UnknownSelectionIdException, DateRangeException;

    Record addRecord(Record record, User user);

    GeneralPurpose addCustomPurpose(CustomPurpose customPurpose, User user) throws CustomPurposeAlreadyExistException, InvalidParameterException;

    Page<Record> getRecordsList(DatePicker datePicker, long userId);

    boolean deleteRecord(long recordId, long userId) throws RecordDoesNotExistException;

    Record editRecord(Record record, long userId) throws RecordDoesNotExistException, InvalidParameterException;
}