package com.kamabizbazti.model.interfaces;

import com.kamabizbazti.model.entities.*;
import com.kamabizbazti.security.entities.User;
import org.springframework.data.domain.Page;

import java.util.List;

public interface IUserRequestHandler {
    List<GeneralPurpose> getPurposesList(long userId);

    ChartWrapper getUserDefaultDataTable(long userId);

    ChartWrapper getUserDataTable(ChartRequestWrapper chartRequestWrapper, long userId);

    Record addRecord(Record record, User user);

    GeneralPurpose addCustomPurpose(CustomPurpose customPurpose, User user);

    Page<Record> getRecordsList(DatePicker datePicker, long userId);

    boolean deleteRecord(long recordId, long userId);

    Record editRecord(Record record, long userId);
}