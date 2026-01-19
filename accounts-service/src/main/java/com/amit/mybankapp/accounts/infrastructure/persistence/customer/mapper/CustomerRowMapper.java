package com.amit.mybankapp.accounts.infrastructure.persistence.customer.mapper;

import com.amit.mybankapp.accounts.domain.customer.Customer;
import com.amit.mybankapp.accounts.domain.customer.vo.CustomerId;
import com.amit.mybankapp.accounts.domain.customer.vo.Login;
import com.amit.mybankapp.accounts.domain.customer.vo.Profile;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public final class CustomerRowMapper implements RowMapper<Customer> {

    @Override
    public Customer mapRow(ResultSet resultSet, int rowNum) throws SQLException {
        CustomerId customerId = new CustomerId((UUID) resultSet.getObject("customer_id"));
        Login login = new Login(resultSet.getString("login"));

        Profile profile = new Profile(
                resultSet.getString("first_name"),
                resultSet.getString("last_name"),
                resultSet.getDate("birth_date").toLocalDate()
        );

        return new Customer(customerId, login, profile);
    }

}
