package io.smanicome.racemanager.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class RaceNumberAlreadyUsedForRequestedDateException extends Exception {
}
