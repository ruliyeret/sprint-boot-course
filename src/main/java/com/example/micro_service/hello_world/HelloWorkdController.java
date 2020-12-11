package com.example.micro_service.hello_world;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import java.util.Locale;

@RestController
public class HelloWorkdController {

    @Autowired
    private MessageSource messageSource;

    @GetMapping(path = "/")
    public String helloWorld(){
        
        return "Hello world";
    }

    @GetMapping(path = "/message")
    public String message(){
        return messageSource
                .getMessage("good.morning.message",
                        null, LocaleContextHolder.getLocale());
    }
}
