package com.budgeez.restcontroller;

import com.budgeez.model.interfaces.ISettingsRestController;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping(path = "${settings.path}")
@RestController
@CrossOrigin(origins = "*")
public class SettingsRestController implements ISettingsRestController {


}
