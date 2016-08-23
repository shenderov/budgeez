package com.kamabizbazti.restcontroller;

import com.kamabizbazti.model.entities.Record;
import com.kamabizbazti.model.repository.RecordRepository;
import com.kamabizbazti.security.JwtTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping(path = "user")
@RestController
public class UserRestController {

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    RecordRepository recordRepository;

    @RequestMapping(value = "/addRecord", method = RequestMethod.POST)
    public Record test(@RequestBody Record record) {
        return recordRepository.save(record);
    }

    //    @RequestMapping(value = "/getPurposesList", method = RequestMethod.GET)
//    public List<GeneralPurpose> greeting(HttpServletResponse response) {
//       return handler.getPurposesList();
//    }

    @RequestMapping(value = "/test", method = RequestMethod.POST)
    public String test(@RequestBody String token) {
        System.out.println(token);
        System.out.println(jwtTokenUtil.getUsernameFromToken(token));
        return "Test token user: " + token;
    }
}
