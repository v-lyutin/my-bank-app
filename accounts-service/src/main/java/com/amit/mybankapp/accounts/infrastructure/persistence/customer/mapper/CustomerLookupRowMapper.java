package com.amit.mybankapp.accounts.infrastructure.persistence.customer.mapper;

import com.amit.mybankapp.accounts.application.model.CustomerLookup;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public final class CustomerLookupRowMapper implements RowMapper<CustomerLookup> {

    @Override
    public CustomerLookup mapRow(ResultSet resultSet, int rowNum) throws SQLException {
        return new CustomerLookup(
                resultSet.getObject("customer_id", UUID.class),
                resultSet.getString("login"),
                resultSet.getString("first_name"),
                resultSet.getString("last_name")
        );
    }

}
