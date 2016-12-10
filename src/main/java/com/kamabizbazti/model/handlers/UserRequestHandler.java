package com.kamabizbazti.model.handlers;

import com.kamabizbazti.model.entities.*;
import com.kamabizbazti.model.exceptions.*;
import com.kamabizbazti.model.interfaces.IDateHelper;
import com.kamabizbazti.model.interfaces.IUserRequestHandler;
import com.kamabizbazti.model.interfaces.IUserStatisticsHandler;
import com.kamabizbazti.model.repository.ChartSelectionRepository;
import com.kamabizbazti.model.repository.CustomPurposeRepository;
import com.kamabizbazti.model.repository.RecordRepository;
import com.kamabizbazti.security.entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class UserRequestHandler implements IUserRequestHandler {

    @Autowired
    private IDateHelper dateHelper;

    @Autowired
    private IUserStatisticsHandler userStatisticsHandler;

    @Autowired
    private CustomPurposeRepository customPurposeRepository;

    @Autowired
    private RecordRepository recordRepository;

    @Autowired
    private ChartSelectionRepository chartSelectionRepository;

    private static final ChartSelectionId DEFAULT_CHART_SELECTION = ChartSelectionId.USER_CURRENT_MONTH;
    private static final int MAX_RESULT_NUMBER = 500;

    public List<GeneralPurpose> getPurposesList(long userId) {
        return customPurposeRepository.findAllUserSpecified(userId);
    }

    public ChartWrapper getUserDefaultDataTable(long userId) throws UnknownSelectionIdException, DateRangeException {
        ChartRequestWrapper requestWrapper = new ChartRequestWrapper();
        requestWrapper.setChartSelection(chartSelectionRepository.findOne(DEFAULT_CHART_SELECTION));
        return getUserDataTable(requestWrapper, userId);
    }

    public ChartWrapper getUserDataTable(ChartRequestWrapper chartRequestWrapper, long userId) throws UnknownSelectionIdException, DateRangeException {
        if (chartRequestWrapper.getStartDate() > chartRequestWrapper.getEndDate())
            throw new DateRangeException("END_DATE_BEFORE_START_DATE");
        ChartWrapper wrapper;
        switch (chartRequestWrapper.getChartSelection().getSelectionId()) {
            case USER_CURRENT_MONTH:
                wrapper = userStatisticsHandler.getCurrentMonthTotal(chartRequestWrapper, userId);
                break;
            case USER_PREV_MONTH:
                wrapper = userStatisticsHandler.getPrevNMonthsTotal(chartRequestWrapper, userId, 1);
                break;
            case USER_PREV_THREE_MONTH_AVG:
                wrapper = userStatisticsHandler.getPrevNMonthsAverage(chartRequestWrapper, userId, 3);
                break;
            case USER_PREV_THREE_MONTH_TOTAL:
                wrapper = userStatisticsHandler.getPrevNMonthsTotal(chartRequestWrapper, userId, 3);
                break;
            case USER_CUSTOM_PERIOD_AVG:
                wrapper = userStatisticsHandler.getCustomPeriodAverage(chartRequestWrapper, userId);
                break;
            case USER_CUSTOM_PERIOD_TOTAL:
                wrapper = userStatisticsHandler.getCustomPeriodTotal(chartRequestWrapper, userId);
                break;
            case USER_LAST_THREE_MONTH_DETAILED:
                wrapper = userStatisticsHandler.getLastNMonthsDetailed(chartRequestWrapper, userId, 3);
                break;
            case USER_LAST_SIX_MONTH_DETAILED:
                wrapper = userStatisticsHandler.getLastNMonthsDetailed(chartRequestWrapper, userId, 6);
                break;
            case USER_LAST_YEAR_DETAILED:
                wrapper = userStatisticsHandler.getLastNMonthsDetailed(chartRequestWrapper, userId, 12);
                break;
            case USER_CUSTOM_PERIOD_DETAILED:
                wrapper = userStatisticsHandler.getCustomPeriodDetailed(chartRequestWrapper, userId);
                break;
            default:
                throw new UnknownSelectionIdException();
        }
        return wrapper;
    }

    public Record addRecord(Record record, User user) {
        record.setUserId(user);
        return recordRepository.save(record);
    }

    public GeneralPurpose addCustomPurpose(CustomPurpose customPurpose, User user) throws CustomPurposeAlreadyExistException, InvalidParameterException {
        customPurpose.setUser(user);
        try {
            return customPurposeRepository.save(customPurpose);
        } catch (DataIntegrityViolationException e) {
            throw new CustomPurposeAlreadyExistException();
        } catch (javax.validation.ConstraintViolationException e1) {
            throw new InvalidParameterException();
        }
    }

    public Page<Record> getRecordsList(DatePicker datePicker, long userId) {
        if ((datePicker.getStartDate() != 0) && (datePicker.getEndDate() != 0) && (datePicker.getStartDate() <= datePicker.getEndDate()))
            return recordRepository.getRecordsPageble(userId, datePicker.getStartDate(), datePicker.getEndDate(), new PageRequest(0, MAX_RESULT_NUMBER, Sort.Direction.ASC, "date"));
        else
            return recordRepository.getRecordsPageble(userId, dateHelper.getFirstDayOfCurrentMonth(), dateHelper.getLastDayOfCurrentMonth(), new PageRequest(0, MAX_RESULT_NUMBER, Sort.Direction.ASC, "date"));
    }

    public boolean deleteRecord(long recordId, long userId) throws RecordDoesNotExistException {
        if (recordRepository.getRecordByIdAndUserId(recordId, userId) == null)
            throw new RecordDoesNotExistException();
        else
            recordRepository.deleteRecord(recordId, userId);
        return true;
    }

    public Record editRecord(Record record, long userId) throws RecordDoesNotExistException, InvalidParameterException {
        if (recordRepository.getRecordByIdAndUserId(record.getRecordId(), userId) == null)
            throw new RecordDoesNotExistException();
        try {
            recordRepository.updateRecord(record.getPurpose(), record.getPurposeType(), record.getAmount(), record.getComment(), record.getDate(), record.getRecordId(), userId);
        } catch (DataIntegrityViolationException e) {
            throw new InvalidParameterException();
        }
        return recordRepository.getRecordByIdAndUserId(record.getRecordId(), userId);
    }
}