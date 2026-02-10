package com.amit.mybankapp.client.restclient;

import com.amit.mybankapp.commons.client.TransferClient;
import com.amit.mybankapp.commons.client.dto.transfer.CreateTransferRequest;
import com.amit.mybankapp.commons.client.dto.transfer.CreateTransferResponse;
import org.springframework.web.client.RestClient;

public class RestClientTransferClient implements TransferClient {

    private final RestClient restClient;

    public RestClientTransferClient(RestClient restClient) {
        this.restClient = restClient;
    }

    @Override
    public CreateTransferResponse createTransfer(CreateTransferRequest createTransferRequest) {
        return this.restClient.post()
                .uri("/customers/me/transfers")
                .body(createTransferRequest)
                .retrieve()
                .body(CreateTransferResponse.class);
    }

}
