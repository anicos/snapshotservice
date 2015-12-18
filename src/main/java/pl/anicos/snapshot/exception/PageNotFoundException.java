package pl.anicos.snapshot.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "Wrong URL")
public class PageNotFoundException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public PageNotFoundException() {
        super("Can't open the page, wrong url");
    }

}
