package com.kamabizbazti.restcontroller;

import com.kamabizbazti.model.entities.*;
import com.kamabizbazti.model.interfaces.IUserRequestHandler;
import com.kamabizbazti.security.JwtTokenUtil;
import com.kamabizbazti.security.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RequestMapping(path = "${user.path}")
@RestController
@CrossOrigin(origins = "*")
public class UserRestController {

    @Autowired
    private IUserRequestHandler handler;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @CrossOrigin(origins = "*")
    @RequestMapping(value = "/${user.route.getPurposesList}", method = RequestMethod.GET)
    public List<GeneralPurpose> getPurposesList(HttpServletRequest request) {
        String username = jwtTokenUtil.getUsernameFromToken(request.getHeader("Authorization"));
        return handler.getPurposesList(userRepository.findByUsername(username).getId());
    }

    @CrossOrigin(origins = "*")
    @RequestMapping(value = "/${user.route.getUserDefaultDataTable}", method = RequestMethod.GET)
    public ChartWrapper getUserDefaultDataTable(HttpServletRequest request) {
        String username = jwtTokenUtil.getUsernameFromToken(request.getHeader("Authorization"));
        return handler.getUserDefaultDataTable(userRepository.findByUsername(username).getId());
    }

    @CrossOrigin(origins = "*")
    @RequestMapping(value = "/${user.route.getUserDataTable}", method = RequestMethod.POST)
    public ChartWrapper getGeneralDataTable(@RequestBody ChartRequestWrapper chartRequestWrapper, HttpServletRequest request) {
        String username = jwtTokenUtil.getUsernameFromToken(request.getHeader("Authorization"));
        return handler.getUserDataTable(chartRequestWrapper, userRepository.findByUsername(username).getId());
    }

    @CrossOrigin(origins = "*")
    @RequestMapping(value = "/${user.route.addRecord}", method = RequestMethod.POST)
    public Record addRecord(@RequestBody Record record, HttpServletRequest request) {
        String username = jwtTokenUtil.getUsernameFromToken(request.getHeader("Authorization"));
        return handler.addRecord(record, userRepository.findByUsername(username));
    }

    @CrossOrigin(origins = "*")
    @RequestMapping(value = "/${user.route.addCustomPurpose}", method = RequestMethod.POST)
    public GeneralPurpose addCustomPurpose(@RequestBody CustomPurpose customPurpose, HttpServletRequest request) {
        String username = jwtTokenUtil.getUsernameFromToken(request.getHeader("Authorization"));
        return handler.addCustomPurpose(customPurpose, userRepository.findByUsername(username));
    }

    @RequestMapping(value = "/${user.route.getRecordsList}", method = RequestMethod.POST)
    public Page <Record> getRecordsList(@RequestBody DatePicker datePicker, HttpServletRequest request) {
        String username = jwtTokenUtil.getUsernameFromToken(request.getHeader("Authorization"));
        return handler.getRecordsList(datePicker, userRepository.findByUsername(username).getId());
    }

    @CrossOrigin(origins = "*")
    @RequestMapping(value = "/${user.route.deleteRecord}", method = RequestMethod.POST)
    public boolean deleteRecord(@RequestBody long recordId, HttpServletRequest request) {
        String username = jwtTokenUtil.getUsernameFromToken(request.getHeader("Authorization"));
        return handler.deleteRecord(recordId, userRepository.findByUsername(username).getId());
    }

    @CrossOrigin(origins = "*")
    @RequestMapping(value = "/${user.route.editRecord}", method = RequestMethod.POST)
    public Record editRecord(@RequestBody Record record, HttpServletRequest request) {
        String username = jwtTokenUtil.getUsernameFromToken(request.getHeader("Authorization"));
        return handler.editRecord(record, userRepository.findByUsername(username).getId());
    }

    @CrossOrigin(origins = "*")
    @RequestMapping(value = "/test", method = RequestMethod.GET)
    public String test(HttpServletRequest request) {
        String username = jwtTokenUtil.getUsernameFromToken(request.getHeader("Authorization"));
        System.out.println(userRepository.findByUsername(username).getId());
        return "Test";
    }
}
