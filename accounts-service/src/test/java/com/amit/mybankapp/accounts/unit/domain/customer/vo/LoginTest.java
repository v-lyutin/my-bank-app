package com.amit.mybankapp.accounts.unit.domain.customer.vo;

import com.amit.mybankapp.accounts.domain.customer.vo.Login;
import com.amit.mybankapp.accounts.domain.customer.vo.exception.InvalidLoginException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Locale;

import static org.junit.jupiter.api.Assertions.*;

class LoginTest {

    private static final String VALID_LOGIN = "john.doe";

    @Test
    @DisplayName(value = "Should create login when value is valid")
    void shouldCreateLoginWhenValueIsValid() {
        Login login = new Login(VALID_LOGIN);

        assertEquals(VALID_LOGIN, login.value());
    }

    @Test
    @DisplayName(value = "Should normalize login to trimmed lowercase when value contains spaces and uppercase letters")
    void shouldNormalizeLoginToTrimmedLowercaseWhenValueContainsSpacesAndUppercaseLetters() {
        Login login = new Login("  JoHn.DoE  ");

        assertEquals("john.doe", login.value());
    }

    @Test
    @DisplayName(value = "Should throw NullPointerException when value is null")
    void constructor_shouldThrowNullPointerExceptionWhenValueIsNull() {
        NullPointerException exception = assertThrows(
                NullPointerException.class,
                () -> new Login(null)
        );

        assertEquals("value must not be null", exception.getMessage());
    }

    @Test
    @DisplayName(value = "Should throw InvalidLoginException when login length is smaller than 3 after normalization")
    void constructor_shouldThrowInvalidLoginExceptionWhenLoginLengthIsSmallerThan3AfterNormalization() {
        InvalidLoginException exception = assertThrows(
                InvalidLoginException.class,
                () -> new Login("ab")
        );

        assertEquals("login length must be between 3 and 128", exception.getMessage());
    }

    @Test
    @DisplayName(value = "Should create login when login length is exactly 3 after normalization")
    void shouldCreateLoginWhenLoginLengthIsExactly3AfterNormalization() {
        Login login = new Login("abc");

        assertEquals("abc", login.value());
    }

    @Test
    @DisplayName(value = "Should create login when login length is exactly 128 after normalization")
    void shouldCreateLoginWhenLoginLengthIsExactly128AfterNormalization() {
        String loginWithMaximumAllowedLength = "a" + "b".repeat(126) + "c";

        assertEquals(128, loginWithMaximumAllowedLength.length());

        Login login = new Login(loginWithMaximumAllowedLength);

        assertEquals(loginWithMaximumAllowedLength, login.value());
    }

    @Test
    @DisplayName(value = "Should throw InvalidLoginException when login length is greater than 128 after normalization")
    void constructor_shouldThrowInvalidLoginExceptionWhenLoginLengthIsGreaterThan128AfterNormalization() {
        String loginLongerThanMaximumAllowedLength = "a" + "b".repeat(127) + "c";

        assertEquals(129, loginLongerThanMaximumAllowedLength.length());

        InvalidLoginException exception = assertThrows(
                InvalidLoginException.class,
                () -> new Login(loginLongerThanMaximumAllowedLength)
        );

        assertEquals("login length must be between 3 and 128", exception.getMessage());
    }

    @Test
    @DisplayName(value = "Should throw InvalidLoginException when normalized login starts with non alphanumeric character")
    void constructor_shouldThrowInvalidLoginExceptionWhenNormalizedLoginStartsWithNonAlphanumericCharacter() {
        InvalidLoginException exception = assertThrows(
                InvalidLoginException.class,
                () -> new Login(".john")
        );

        assertEquals("login contains invalid characters", exception.getMessage());
    }

    @Test
    @DisplayName(value = "Should throw InvalidLoginException when normalized login ends with non alphanumeric character")
    void constructor_shouldThrowInvalidLoginExceptionWhenNormalizedLoginEndsWithNonAlphanumericCharacter() {
        InvalidLoginException exception = assertThrows(
                InvalidLoginException.class,
                () -> new Login("john-")
        );

        assertEquals("login contains invalid characters", exception.getMessage());
    }

    @Test
    @DisplayName(value = "Should throw InvalidLoginException when normalized login contains whitespace characters")
    void constructor_shouldThrowInvalidLoginExceptionWhenNormalizedLoginContainsWhitespaceCharacters() {
        InvalidLoginException exception = assertThrows(
                InvalidLoginException.class,
                () -> new Login("john doe")
        );

        assertEquals("login contains invalid characters", exception.getMessage());
    }

    @Test
    @DisplayName(value = "Should throw InvalidLoginException when normalized login contains characters outside allowed pattern")
    void constructor_shouldThrowInvalidLoginExceptionWhenNormalizedLoginContainsCharactersOutsideAllowedPattern() {
        InvalidLoginException exception = assertThrows(
                InvalidLoginException.class,
                () -> new Login("john@doe")
        );

        assertEquals("login contains invalid characters", exception.getMessage());
    }

    @Test
    @DisplayName(value = "Should create login when normalized login contains dots underscores and hyphens in the middle")
    void shouldCreateLoginWhenNormalizedLoginContainsDotsUnderscoresAndHyphensInTheMiddle() {
        Login login = new Login("john.doe_test-user");

        assertEquals("john.doe_test-user", login.value());
    }

    @Test
    @DisplayName(value = "Should throw InvalidLoginException when normalized login is reserved")
    void constructor_shouldThrowInvalidLoginExceptionWhenNormalizedLoginIsReserved() {
        InvalidLoginException exception = assertThrows(
                InvalidLoginException.class,
                () -> new Login("admin")
        );

        assertEquals("login is reserved", exception.getMessage());
    }

    @Test
    @DisplayName(value = "Should throw InvalidLoginException when normalized login becomes reserved after trimming and lowercasing")
    void constructor_shouldThrowInvalidLoginExceptionWhenNormalizedLoginBecomesReservedAfterTrimmingAndLowercasing() {
        InvalidLoginException exception = assertThrows(
                InvalidLoginException.class,
                () -> new Login("  AdMiN  ")
        );

        assertEquals("login is reserved", exception.getMessage());
    }

    @Test
    @DisplayName(value = "Should create login when value is valid using factory method")
    void of_shouldCreateLoginWhenValueIsValidUsingFactoryMethod() {
        Login login = Login.of(VALID_LOGIN);

        assertEquals(VALID_LOGIN, login.value());
    }

    @Test
    @DisplayName(value = "Should normalize login when factory method is used")
    void of_shouldNormalizeLoginWhenFactoryMethodIsUsed() {
        Login login = Login.of("  JoHn.DoE  ");

        assertEquals("john.doe", login.value());
    }

    @Test
    @DisplayName(value = "Should throw NullPointerException when factory method receives null value")
    void of_shouldThrowNullPointerExceptionWhenFactoryMethodReceivesNullValue() {
        NullPointerException exception = assertThrows(
                NullPointerException.class,
                () -> Login.of(null)
        );

        assertEquals("value must not be null", exception.getMessage());
    }

    @Test
    @DisplayName(value = "Should normalize login using Locale ROOT lowercasing rules")
    void shouldNormalizeLoginUsingLocaleRootLowercasingRules() {
        Locale defaultLocale = Locale.getDefault();
        try {
            Locale.setDefault(Locale.of("tr", "TR"));

            Login login = new Login("Iii");

            assertEquals("iii", login.value());
        } finally {
            Locale.setDefault(defaultLocale);
        }
    }

}