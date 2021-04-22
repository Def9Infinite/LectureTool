package nl.tudelft.oopp.project.exceptions;

import java.net.http.HttpResponse;

public class ServerResponseException extends RuntimeException {

    final HttpResponse<String> response;

    public ServerResponseException(HttpResponse<String> response) {
        super("Received status code " + response.statusCode());
        this.response = response;
    }

    public HttpResponse<String> getResponse() {
        return response;
    }
}
