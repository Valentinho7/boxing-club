package fr.eql.ai115.boxing.club.controller.rest;

import fr.eql.ai115.boxing.club.entity.dto.AddSessionDto;
import fr.eql.ai115.boxing.club.entity.dto.AddSessionTypeDto;
import fr.eql.ai115.boxing.club.service.impl.ApplicationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/admin/sessions/types")
public class AdminSessionTypeRestController {

    @Autowired
    ApplicationService applicationService;

    @PostMapping("/add")
    public void saveSessionType(@RequestBody AddSessionTypeDto addSessionTypeDto) {
        applicationService.saveSessionType(addSessionTypeDto);
    }


}
