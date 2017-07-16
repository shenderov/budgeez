package com.budgeez.restcontroller;

import com.budgeez.model.entities.dao.CustomCategory;
import com.budgeez.model.entities.dao.GeneralCategory;
import com.budgeez.model.entities.dao.Record;
import com.budgeez.model.entities.external.*;
import com.budgeez.model.exceptions.*;
import com.budgeez.model.interfaces.IUserRequestHandler;
import com.budgeez.model.interfaces.IUserRestController;
import com.budgeez.security.JwtTokenUtil;
import com.budgeez.security.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

@RequestMapping(path = "${user.path}")
@RestController
@CrossOrigin(origins = "*")
public class UserRestController implements IUserRestController {

    @Autowired
    private IUserRequestHandler handler;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @CrossOrigin(origins = "*")
    @RequestMapping(value = "/${user.route.getCategoriesList}", method = RequestMethod.GET)
    public List<GeneralCategory> getCategoriesList(HttpServletRequest request) {
        String username = jwtTokenUtil.getUsernameFromToken(request.getHeader("Authorization"));
        return handler.getCategoriesList(userRepository.findByUsername(username).getId());
    }

    @CrossOrigin(origins = "*")
    @RequestMapping(value = "/${user.route.getUserDefaultDataTable}", method = RequestMethod.GET)
    public ChartWrapper getUserDefaultDataTable(HttpServletRequest request) throws UnknownSelectionIdException, DateRangeException {
        String username = jwtTokenUtil.getUsernameFromToken(request.getHeader("Authorization"));
        return handler.getUserDefaultDataTable(userRepository.findByUsername(username).getId());
    }

    @CrossOrigin(origins = "*")
    @RequestMapping(value = "/${user.route.getUserDataTable}", method = RequestMethod.POST)
    public ChartWrapper getGeneralDataTable(@RequestBody @Valid ChartRequestWrapper chartRequestWrapper, HttpServletRequest request) throws UnknownSelectionIdException, DateRangeException {
        String username = jwtTokenUtil.getUsernameFromToken(request.getHeader("Authorization"));
        return handler.getUserDataTable(chartRequestWrapper, userRepository.findByUsername(username).getId());
    }

    @CrossOrigin(origins = "*")
    @RequestMapping(value = "/${user.route.addRecord}", method = RequestMethod.POST)
    public Record addRecord(@RequestBody @Valid ERecord record, HttpServletRequest request) {
        String username = jwtTokenUtil.getUsernameFromToken(request.getHeader("Authorization"));
        return handler.addRecord(record, userRepository.findByUsername(username));
    }

    @CrossOrigin(origins = "*")
    @RequestMapping(value = "/${user.route.addCustomCategory}", method = RequestMethod.POST)
    public GeneralCategory addCustomCategory(@RequestBody @Valid ECustomCategory newCategory, HttpServletRequest request) throws CustomCategoryAlreadyExistException, InvalidParameterException {
        String username = jwtTokenUtil.getUsernameFromToken(request.getHeader("Authorization"));
        CustomCategory customCategory = new CustomCategory(userRepository.findByUsername(username), newCategory.getName());
        return handler.addCustomCategory(customCategory);
    }

    @RequestMapping(value = "/${user.route.getRecordsList}", method = RequestMethod.POST)
    public Page<Record> getRecordsList(@RequestBody DatePicker datePicker, HttpServletRequest request) {
        String username = jwtTokenUtil.getUsernameFromToken(request.getHeader("Authorization"));
        return handler.getRecordsList(datePicker, userRepository.findByUsername(username).getId());
    }

    @CrossOrigin(origins = "*")
    @RequestMapping(value = "/${user.route.deleteRecord}", method = RequestMethod.POST)
    public boolean deleteRecord(@RequestBody long recordId, HttpServletRequest request) throws RecordDoesNotExistException {
        String username = jwtTokenUtil.getUsernameFromToken(request.getHeader("Authorization"));
        return handler.deleteRecord(recordId, userRepository.findByUsername(username).getId());
    }

    @CrossOrigin(origins = "*")
    @RequestMapping(value = "/${user.route.editRecord}", method = RequestMethod.POST)
    public Record editRecord(@RequestBody @Valid EditRecordWrapper record, HttpServletRequest request) throws RecordDoesNotExistException, InvalidParameterException {
        String username = jwtTokenUtil.getUsernameFromToken(request.getHeader("Authorization"));
        return handler.editRecord(record, userRepository.findByUsername(username).getId());
    }
}