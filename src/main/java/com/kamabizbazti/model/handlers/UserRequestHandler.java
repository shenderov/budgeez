package com.kamabizbazti.model.handlers;

import com.kamabizbazti.model.dao.*;
import com.kamabizbazti.model.entities.*;
import com.kamabizbazti.model.enumerations.CategoryType;
import com.kamabizbazti.model.enumerations.ChartSelectionIdEnum;
import com.kamabizbazti.model.exceptions.*;
import com.kamabizbazti.model.exceptions.codes.EntitiesErrorCode;
import com.kamabizbazti.model.interfaces.*;
import com.kamabizbazti.model.repository.ChartSelectionRepository;
import com.kamabizbazti.model.repository.CustomCategoryRepository;
import com.kamabizbazti.model.repository.GeneralCategoryRepository;
import com.kamabizbazti.model.repository.RecordRepository;
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
    private IValidationHelper validationHelper;

    @Autowired
    private IUserStatisticsHandler userStatisticsHandler;

    @Autowired
    private CustomCategoryRepository customCategoryRepository;

    @Autowired
    private GeneralCategoryRepository generalCategoryRepository;

    @Autowired
    private RecordRepository recordRepository;

    @Autowired
    private IExceptionMessagesHelper exceptionMessagesHelper;

    @Autowired
    private ChartSelectionRepository chartSelectionRepository;

    public static final ChartSelectionIdEnum DEFAULT_CHART_SELECTION = ChartSelectionIdEnum.USER_CURRENT_MONTH;
    private static final int MAX_RESULT_NUMBER = 500;

    public List<GeneralCategory> getCategoriesList(long userId) {
        return customCategoryRepository.findAllUserSpecified(userId);
    }

    public ChartWrapper getUserDefaultDataTable(long userId) throws UnknownSelectionIdException, DateRangeException {
        ChartRequestWrapper requestWrapper = new ChartRequestWrapper();
        requestWrapper.setChartSelection(chartSelectionRepository.findOne(DEFAULT_CHART_SELECTION));
        return getUserDataTable(requestWrapper, userId);
    }

    public ChartWrapper getUserDataTable(ChartRequestWrapper chartRequestWrapper, long userId) throws UnknownSelectionIdException, DateRangeException {
        ChartSelection selection = chartSelectionRepository.findOne(chartRequestWrapper.getChartSelection().getSelectionId());
        if (selection.isDatePicker())
            validationHelper.validateDatePicker(chartRequestWrapper.getDatePicker());
        chartRequestWrapper.setChartSelection(selection);
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
                throw new UnknownSelectionIdException(EntitiesErrorCode.UNKNOWN_CHART_SELECTION_ID, exceptionMessagesHelper.getLocalizedMessage("error.chartselection.unknown"));
        }
        return wrapper;
    }

    public Record addRecord(ERecord record, User user) {
        Record rec = new Record();
        rec.setCategory(getCategory(record.getCategoryId(), user.getId()));
        rec.setUserId(user);
        rec.setAmount(record.getAmount());
        rec.setDate(record.getDate());
        rec.setComment(record.getComment());
        return recordRepository.save(rec);
    }

    public GeneralCategory addCustomCategory(CustomCategory customCategory) throws CustomCategoryAlreadyExistException, InvalidParameterException {
        try {
            return customCategoryRepository.save(customCategory);
        } catch (DataIntegrityViolationException e) {
            throw new CustomCategoryAlreadyExistException(EntitiesErrorCode.CUSTOM_CATEGORY_ALREADY_EXIST, exceptionMessagesHelper.getLocalizedMessage("error.customcategory.alreadyexist"));
        } catch (javax.validation.ConstraintViolationException e1) {
            e1.printStackTrace();
            throw new InvalidParameterException("Test");
        }
    }

    public Page<Record> getRecordsList(DatePicker datePicker, long userId) {
        if (datePicker.getStartDate() == null && datePicker.getEndDate() == null) {
            return recordRepository.getRecordsPageble(userId, dateHelper.getFirstDayOfCurrentMonth(), dateHelper.getLastDayOfCurrentMonth(), new PageRequest(0, MAX_RESULT_NUMBER, Sort.Direction.ASC, "date"));
        } else
            validationHelper.validateDatePicker(datePicker);
        return recordRepository.getRecordsPageble(userId, datePicker.getStartDate(), datePicker.getEndDate(), new PageRequest(0, MAX_RESULT_NUMBER, Sort.Direction.ASC, "date"));
    }

    public boolean deleteRecord(long recordId, long userId) throws RecordDoesNotExistException {
        if (recordRepository.getRecordByIdAndUserId(recordId, userId) == null)
            throw new RecordDoesNotExistException(EntitiesErrorCode.RECORD_DOES_NOT_EXIST, exceptionMessagesHelper.getLocalizedMessage("error.record.notexist"));
        else
            recordRepository.deleteRecord(recordId, userId);
        return true;
    }

    public Record editRecord(EditRecordWrapper record, long userId) throws RecordDoesNotExistException, InvalidParameterException {
        if (recordRepository.getRecordByIdAndUserId(record.getRecordId(), userId) == null)
            throw new RecordDoesNotExistException(EntitiesErrorCode.RECORD_DOES_NOT_EXIST, exceptionMessagesHelper.getLocalizedMessage("error.record.notexist"));
        GeneralCategory category = getCategory(record.getRecord().getCategoryId(), userId);
        recordRepository.updateRecord(category, category.getType(), record.getRecord().getAmount(), record.getRecord().getComment(), record.getRecord().getDate(), record.getRecordId(), userId);
        return recordRepository.getRecordByIdAndUserId(record.getRecordId(), userId);
    }

    private GeneralCategory getCategory(Long categoryId, Long userId) {
        GeneralCategory category = generalCategoryRepository.findCategoryForUser(userId, categoryId);
        if (category == null)
            throw new CategoryNotFoundException(EntitiesErrorCode.CATEGORY_DOES_NOT_EXIST, exceptionMessagesHelper.getLocalizedMessage("error.category.notexist"));
        return category;
    }
}