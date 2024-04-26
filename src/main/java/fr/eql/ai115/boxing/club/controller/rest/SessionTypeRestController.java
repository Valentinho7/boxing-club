package fr.eql.ai115.boxing.club.controller.rest;

import fr.eql.ai115.boxing.club.entity.dto.AddSessionTypeDto;
import fr.eql.ai115.boxing.club.service.impl.ApplicationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/admin/sessions/types")
public class SessionTypeRestController {

    @Autowired
    ApplicationService applicationService;

    @PostMapping
    public void saveSessionType(@RequestBody AddSessionTypeDto addSessionTypeDto) {
        applicationService.saveSessionType(addSessionTypeDto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteSessionType(@PathVariable Long id) {
        applicationService.deleteSessionType(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> updateSessionType(@RequestBody AddSessionTypeDto updateSessionTypeDto, @PathVariable Long id) {
        applicationService.updateSessionType(updateSessionTypeDto, id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<Object> findAllSessionTypes() {
        return new ResponseEntity<>(applicationService.findAllSessionTypes(), HttpStatus.OK);
    }
}
