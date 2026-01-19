package com.amit.mybankapp.accounts.it;

import com.amit.mybankapp.accounts.application.model.CustomerLookup;
import com.amit.mybankapp.accounts.domain.customer.Customer;
import com.amit.mybankapp.accounts.domain.customer.vo.CustomerId;
import com.amit.mybankapp.accounts.domain.customer.vo.Login;
import com.amit.mybankapp.accounts.domain.customer.vo.Profile;
import com.amit.mybankapp.accounts.infrastructure.persistence.customer.JdbcCustomerRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@Import(value = JdbcCustomerRepository.class)
class JdbcCustomerRepositoryIT extends AbstractRepositoryIT {

    private static final LocalDate BIRTH_DATE_AT_LEAST_EIGHTEEN_YEARS_AGO = LocalDate.now().minusYears(20);

    @Autowired
    private JdbcCustomerRepository jdbcCustomerRepository;

    @Test
    @DisplayName(value = "Should return customer when customer exists by customerId")
    void findByCustomerId_shouldReturnCustomerWhenCustomerExistsByCustomerId() {
        UUID customerId = UUID.randomUUID();
        this.insertCustomerRow(
                customerId,
                "john.doe",
                "John",
                "Doe"
        );

        Optional<Customer> result = this.jdbcCustomerRepository.findByCustomerId(new CustomerId(customerId));

        assertTrue(result.isPresent());
        Customer customer = result.get();

        assertAll(
                () -> assertEquals(new CustomerId(customerId), customer.getCustomerId()),
                () -> assertEquals(new Login("john.doe"), customer.getLogin()),
                () -> assertEquals(new Profile("John", "Doe", BIRTH_DATE_AT_LEAST_EIGHTEEN_YEARS_AGO), customer.getProfile())
        );
    }

    @Test
    @DisplayName(value = "Should return empty optional when customer does not exist by customerId")
    void findByCustomerId_shouldReturnEmptyOptionalWhenCustomerDoesNotExistByCustomerId() {
        Optional<Customer> result = this.jdbcCustomerRepository.findByCustomerId(new CustomerId(UUID.randomUUID()));

        assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName(value = "Should update profile when customer exists and updateProfile is called")
    void updateProfile_shouldUpdateProfileWhenCustomerExistsAndUpdateProfileIsCalled() {
        UUID customerIdUuid = UUID.randomUUID();
        this.insertCustomerRow(
                customerIdUuid,
                "jane.smith",
                "Jane",
                "Smith"
        );

        CustomerId customerId = new CustomerId(customerIdUuid);

        Customer customer = this.jdbcCustomerRepository.findByCustomerId(customerId).orElseThrow();
        Profile updatedProfile = new Profile("Janet", "Smythe", LocalDate.now().minusYears(30));
        customer.changeProfile(updatedProfile);

        this.jdbcCustomerRepository.updateProfile(customer);

        Customer reloadedCustomer = this.jdbcCustomerRepository.findByCustomerId(customerId).orElseThrow();

        assertAll(
                () -> assertEquals(new Login("jane.smith"), reloadedCustomer.getLogin(), "login must not change on profile update"),
                () -> assertEquals(updatedProfile, reloadedCustomer.getProfile())
        );
    }

    @Test
    @DisplayName(value = "Should return recipient candidates excluding given customerId and ordered by lastName then firstName")
    void findRecipientCandidatesExcluding_shouldReturnRecipientCandidatesExcludingGivenCustomerIdAndOrderedByLastNameThenFirstName() {
        UUID excludedCustomerId = UUID.randomUUID();
        UUID firstCustomerId = UUID.randomUUID();
        UUID secondCustomerId = UUID.randomUUID();
        UUID thirdCustomerId = UUID.randomUUID();

        this.insertCustomerRow(excludedCustomerId, "excluded.user", "Excluded", "User");

        this.insertCustomerRow(firstCustomerId, "alice.alpha", "Alice", "Alpha");
        this.insertCustomerRow(secondCustomerId, "bob.alpha", "Bob", "Alpha");
        this.insertCustomerRow(thirdCustomerId, "charlie.beta", "Charlie", "Beta");

        List<CustomerLookup> candidates = this.jdbcCustomerRepository.findRecipientCandidatesExcluding(new CustomerId(excludedCustomerId));

        assertEquals(3, candidates.size());

        CustomerLookup firstCandidate = candidates.get(0);
        CustomerLookup secondCandidate = candidates.get(1);
        CustomerLookup thirdCandidate = candidates.get(2);

        assertAll(
                () -> assertEquals(firstCustomerId, firstCandidate.userId()),
                () -> assertEquals("alice.alpha", firstCandidate.login()),
                () -> assertEquals("Alice", firstCandidate.firstName()),
                () -> assertEquals("Alpha", firstCandidate.lastName()),

                () -> assertEquals(secondCustomerId, secondCandidate.userId()),
                () -> assertEquals("bob.alpha", secondCandidate.login()),
                () -> assertEquals("Bob", secondCandidate.firstName()),
                () -> assertEquals("Alpha", secondCandidate.lastName()),

                () -> assertEquals(thirdCustomerId, thirdCandidate.userId()),
                () -> assertEquals("charlie.beta", thirdCandidate.login()),
                () -> assertEquals("Charlie", thirdCandidate.firstName()),
                () -> assertEquals("Beta", thirdCandidate.lastName())
        );

        assertTrue(
                candidates.stream().noneMatch(candidate -> candidate.userId().equals(excludedCustomerId)),
                "excluded customer must not be returned"
        );
    }

    private void insertCustomerRow(UUID customerId, String login, String firstName, String lastName) {
        this.namedParameterJdbcTemplate.update(
                """
                INSERT INTO accounts.customers (customer_id, login, first_name, last_name, birth_date)
                VALUES (:customerId, :login, :firstName, :lastName, :birthDate)
                """,
                Map.of(
                        "customerId", customerId,
                        "login", login,
                        "firstName", firstName,
                        "lastName", lastName,
                        "birthDate", JdbcCustomerRepositoryIT.BIRTH_DATE_AT_LEAST_EIGHTEEN_YEARS_AGO
                )
        );
    }

}
