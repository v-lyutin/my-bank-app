package com.amit.mybankapp.accounts.infrastructure.persistence.customer;

import com.amit.mybankapp.accounts.application.model.CustomerLookup;
import com.amit.mybankapp.accounts.application.repository.CustomerRepository;
import com.amit.mybankapp.accounts.domain.customer.Customer;
import com.amit.mybankapp.accounts.domain.customer.vo.CustomerId;
import com.amit.mybankapp.accounts.infrastructure.persistence.customer.mapper.CustomerLookupRowMapper;
import com.amit.mybankapp.accounts.infrastructure.persistence.customer.mapper.CustomerRowMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public class JdbcCustomerRepository implements CustomerRepository {

    private static final CustomerRowMapper CUSTOMER_ROW_MAPPER = new CustomerRowMapper();

    private static final CustomerLookupRowMapper CUSTOMER_LOOKUP_ROW_MAPPER = new CustomerLookupRowMapper();

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Autowired
    public JdbcCustomerRepository(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    @Override
    public Optional<Customer> findByCustomerId(CustomerId customerId) {
        List<Customer> customers = this.namedParameterJdbcTemplate.query(
                SqlCustomerQuery.FIND_BY_CUSTOMER_ID,
                Map.of("customerId", customerId.value()),
                CUSTOMER_ROW_MAPPER
        );
        return customers.stream().findFirst();
    }

    @Override
    public void updateProfile(Customer customer) {
        this.namedParameterJdbcTemplate.update(
                SqlCustomerQuery.UPDATE_PROFILE_BY_CUSTOMER_ID,
                Map.of(
                        "customerId", customer.getCustomerId().value(),
                        "firstName", customer.getProfile().firstName(),
                        "lastName", customer.getProfile().lastName(),
                        "birthDate", customer.getProfile().birthDate()
                )
        );
    }

    @Override
    public List<CustomerLookup> findRecipientCandidatesExcluding(CustomerId excludeCustomerId) {
        return this.namedParameterJdbcTemplate.query(
                SqlCustomerQuery.FIND_RECIPIENT_CANDIDATES_EXCLUDING_CUSTOMER_ID,
                Map.of("excludeCustomerId", excludeCustomerId.value()),
                CUSTOMER_LOOKUP_ROW_MAPPER
        );
    }

}
