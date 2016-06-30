package network.response;

import org.simpleframework.xml.Element;

/**
 * Created by Mikey on 12/17/2015.
 */
public class ErrorResponse {

    @Element(name = "error")
    private String error;

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }
}
