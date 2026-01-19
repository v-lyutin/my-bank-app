package com.amit.mybankapp.accounts.unit.domain.customer.vo;

import com.amit.mybankapp.accounts.domain.customer.vo.CustomerId;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class CustomerIdTest {

    @Test
    @DisplayName(value = "Should create customerId when value is not null")
    void shouldCreateCustomerIdWhenValueIsNotNull() {
        UUID customerIdentifier = UUID.randomUUID();

        CustomerId customerId = new CustomerId(customerIdentifier);

        assertEquals(customerIdentifier, customerId.value());
    }

    @Test
    @DisplayName(value = "Should throw NullPointerException when value is null")
    void constructor_shouldThrowNullPointerExceptionWhenValueIsNull() {
        NullPointerException exception = assertThrows(
                NullPointerException.class,
                () -> new CustomerId(null)
        );

        assertEquals("userId must not be null", exception.getMessage());
    }

    @Test
    @DisplayName(value = "Should create customerId using factory method when value is not null")
    void of_shouldCreateCustomerIdWhenValueIsNotNull() {
        UUID customerIdentifier = UUID.randomUUID();

        CustomerId customerId = CustomerId.of(customerIdentifier);

        assertEquals(customerIdentifier, customerId.value());
    }

    @Test
    @DisplayName(value = "Should throw NullPointerException when factory method receives null value")
    void of_shouldThrowNullPointerExceptionWhenFactoryMethodReceivesNullValue() {
        NullPointerException exception = assertThrows(
                NullPointerException.class,
                () -> CustomerId.of(null)
        );

        assertEquals("userId must not be null", exception.getMessage());
    }

}