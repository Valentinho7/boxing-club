package fr.eql.ai115.boxing.club.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.CONFLICT, reason = "Compte existant")
public class AccountExistsException extends Exception {
    
}
