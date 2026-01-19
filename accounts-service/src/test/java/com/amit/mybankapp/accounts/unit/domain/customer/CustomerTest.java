package com.amit.mybankapp.accounts.unit.domain.customer;

import com.amit.mybankapp.accounts.domain.customer.Customer;
import com.amit.mybankapp.accounts.domain.customer.vo.CustomerId;
import com.amit.mybankapp.accounts.domain.customer.vo.Login;
import com.amit.mybankapp.accounts.domain.customer.vo.Profile;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class CustomerTest {

    @Test
    @DisplayName(value = "Should create customer when customerId login and profile are not null")
    void shouldCreateCustomerWhenCustomerIdLoginAndProfileAreNotNull() {
        UUID customerIdentifier = UUID.randomUUID();
        CustomerId customerId = new CustomerId(customerIdentifier);

        Login login = new Login("john.doe");

        LocalDate birthDateAtLeastEighteenYearsAgo = LocalDate.now().minusYears(18);
        Profile profile = new Profile("John", "Doe", birthDateAtLeastEighteenYearsAgo);

        Customer customer = new Customer(customerId, login, profile);

        assertAll(
                () -> assertEquals(customerId, customer.getCustomerId()),
                () -> assertEquals(login, customer.getLogin()),
                () -> assertEquals(profile, customer.getProfile())
        );
    }

    @Test
    @DisplayName(value = "Should throw NullPointerException when customerId is null")
    void constructor_shouldThrowNullPointerExceptionWhenCustomerIdIsNull() {
        Login login = new Login("john.doe");
        Profile profile = new Profile("John", "Doe", LocalDate.now().minusYears(18));

        NullPointerException exception = assertThrows(
                NullPointerException.class,
                () -> new Customer(null, login, profile)
        );

        assertEquals("customerId must not be null", exception.getMessage());
    }

    @Test
    @DisplayName(value = "Should throw NullPointerException when login is null")
    void constructor_shouldThrowNullPointerExceptionWhenLoginIsNull() {
        CustomerId customerId = new CustomerId(UUID.randomUUID());
        Profile profile = new Profile("John", "Doe", LocalDate.now().minusYears(18));

        NullPointerException exception = assertThrows(
                NullPointerException.class,
                () -> new Customer(customerId, null, profile)
        );

        assertEquals("login must not be null", exception.getMessage());
    }

    @Test
    @DisplayName(value = "Should throw NullPointerException when profile is null")
    void constructor_shouldThrowNullPointerExceptionWhenProfileIsNull() {
        CustomerId customerId = new CustomerId(UUID.randomUUID());
        Login login = new Login("john.doe");

        NullPointerException exception = assertThrows(
                NullPointerException.class,
                () -> new Customer(customerId, login, null)
        );

        assertEquals("profile must not be null", exception.getMessage());
    }

    @Test
    @DisplayName(value = "Should change profile when newProfile is not null")
    void changeProfile_shouldChangeProfileWhenNewProfileIsNotNull() {
        CustomerId customerId = new CustomerId(UUID.randomUUID());
        Login login = new Login("john.doe");

        Profile originalProfile = new Profile("John", "Doe", LocalDate.now().minusYears(18));
        Customer customer = new Customer(customerId, login, originalProfile);

        Profile newProfile = new Profile("Jane", "Smith", LocalDate.now().minusYears(18));

        customer.changeProfile(newProfile);

        assertEquals(newProfile, customer.getProfile());
    }

    @Test
    @DisplayName(value = "Should throw NullPointerException when changeProfile receives null newProfile")
    void changeProfile_shouldThrowNullPointerExceptionWhenNewProfileIsNull() {
        CustomerId customerId = new CustomerId(UUID.randomUUID());
        Login login = new Login("john.doe");

        Profile originalProfile = new Profile("John", "Doe", LocalDate.now().minusYears(18));
        Customer customer = new Customer(customerId, login, originalProfile);

        NullPointerException exception = assertThrows(
                NullPointerException.class,
                () -> customer.changeProfile(null)
        );

        assertEquals("newProfile must not be null", exception.getMessage());
    }

}
