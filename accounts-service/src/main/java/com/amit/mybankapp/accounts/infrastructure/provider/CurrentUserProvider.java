package com.amit.mybankapp.accounts.infrastructure.provider;

import com.amit.mybankapp.accounts.domain.customer.vo.CustomerId;

public interface CurrentUserProvider {

    CustomerId currentUserId();

}
