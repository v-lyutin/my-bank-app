package com.amit.mybankapp.apierrors.client;

import com.amit.mybankapp.apierrors.client.exception.RemoteServiceException;
import com.amit.mybankapp.apierrors.model.ApiErrorResponse;
import com.amit.mybankapp.apierrors.server.exception.base.UpstreamUnavailableException;
import org.springframework.http.HttpRequest;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClient;

import java.io.IOException;
import java.io.InputStream;

public final class RestClientErrorHandler {

    private final ApiErrorResponseDecoder decoder;

    public RestClientErrorHandler(ApiErrorResponseDecoder decoder) {
        this.decoder = decoder;
    }

    public void register(RestClient.Builder builder) {
        builder.requestInterceptor((request, body, execution) -> {
            try {
                return execution.execute(request, body);
            } catch (ResourceAccessException exception) {
                throw new UpstreamUnavailableException("Upstream connection error", exception);
            }
        });
        builder.defaultStatusHandler(HttpStatusCode::isError, this::handleErrorResponse);
    }

    private void handleErrorResponse(HttpRequest request, ClientHttpResponse response) throws IOException {
        int status = response.getStatusCode().value();
        String pathFallback = request.getURI().getPath();

        byte[] body = readBodyOrNull(response);

        ApiErrorResponse apiErrorResponse = this.decoder.decode(
                body,
                status,
                pathFallback,
                response.getStatusText()
        );

        throw new RemoteServiceException(apiErrorResponse);
    }

    private static byte[] readBodyOrNull(ClientHttpResponse response) {
        try {
            InputStream body = response.getBody();
            return body.readAllBytes();
        } catch (IOException exception) {
            return null;
        }
    }

}
