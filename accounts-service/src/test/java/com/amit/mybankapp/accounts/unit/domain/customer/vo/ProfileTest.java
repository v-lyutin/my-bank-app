package com.amit.mybankapp.accounts.unit.domain.customer.vo;

import com.amit.mybankapp.accounts.domain.customer.vo.Profile;
import com.amit.mybankapp.accounts.domain.customer.vo.exception.InvalidProfileException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class ProfileTest {

    private static final String VALID_FIRST_NAME = "John";

    private static final String VALID_LAST_NAME = "Doe";

    private static final LocalDate VALID_BIRTH_DATE = LocalDate.now().minusYears(18);

    @Test
    @DisplayName(value = "Should create profile when all fields are valid")
    void shouldCreateProfileWhenAllFieldsAreValid() {
        Profile profile = new Profile(VALID_FIRST_NAME, VALID_LAST_NAME, VALID_BIRTH_DATE);

        assertAll(
                () -> assertEquals(VALID_FIRST_NAME, profile.firstName()),
                () -> assertEquals(VALID_LAST_NAME, profile.lastName()),
                () -> assertEquals(VALID_BIRTH_DATE, profile.birthDate())
        );
    }

    @Test
    @DisplayName(value = "Should trim firstName and lastName when they contain leading or trailing spaces")
    void shouldTrimFirstNameAndLastNameWhenTheyContainLeadingOrTrailingSpaces() {
        Profile profile = new Profile("  John  ", "  Doe  ", VALID_BIRTH_DATE);

        assertAll(
                () -> assertEquals("John", profile.firstName()),
                () -> assertEquals("Doe", profile.lastName())
        );
    }

    @Test
    @DisplayName(value = "Should throw NullPointerException when firstName is null")
    void constructor_shouldThrowNullPointerExceptionWhenFirstNameIsNull() {
        NullPointerException exception = assertThrows(
                NullPointerException.class,
                () -> new Profile(null, VALID_LAST_NAME, VALID_BIRTH_DATE)
        );

        assertEquals("firstName must not be null", exception.getMessage());
    }

    @Test
    @DisplayName(value = "Should throw NullPointerException when lastName is null")
    void constructor_shouldThrowNullPointerExceptionWhenLastNameIsNull() {
        NullPointerException exception = assertThrows(
                NullPointerException.class,
                () -> new Profile(VALID_FIRST_NAME, null, VALID_BIRTH_DATE)
        );

        assertEquals("lastName must not be null", exception.getMessage());
    }

    @Test
    @DisplayName(value = "Should throw NullPointerException when birthDate is null")
    void constructor_shouldThrowNullPointerExceptionWhenBirthDateIsNull() {
        NullPointerException exception = assertThrows(
                NullPointerException.class,
                () -> new Profile(VALID_FIRST_NAME, VALID_LAST_NAME, null)
        );

        assertEquals("birthDate must not be null", exception.getMessage());
    }

    @Test
    @DisplayName(value = "Should throw InvalidProfileException when firstName is blank after trimming")
    void constructor_shouldThrowInvalidProfileExceptionWhenFirstNameIsBlankAfterTrimming() {
        InvalidProfileException exception = assertThrows(
                InvalidProfileException.class,
                () -> new Profile("   ", VALID_LAST_NAME, VALID_BIRTH_DATE)
        );

        assertEquals("firstName and lastName must not be blank", exception.getMessage());
    }

    @Test
    @DisplayName(value = "Should throw InvalidProfileException when lastName is blank after trimming")
    void constructor_shouldThrowInvalidProfileExceptionWhenLastNameIsBlankAfterTrimming() {
        InvalidProfileException exception = assertThrows(
                InvalidProfileException.class,
                () -> new Profile(VALID_FIRST_NAME, "   ", VALID_BIRTH_DATE)
        );

        assertEquals("firstName and lastName must not be blank", exception.getMessage());
    }

    @Test
    @DisplayName(value = "Should throw InvalidProfileException when firstName length is greater than 64")
    void constructor_shouldThrowInvalidProfileExceptionWhenFirstNameLengthIsGreaterThan64() {
        String firstNameLongerThanAllowed = "A".repeat(65);

        InvalidProfileException exception = assertThrows(
                InvalidProfileException.class,
                () -> new Profile(firstNameLongerThanAllowed, VALID_LAST_NAME, VALID_BIRTH_DATE)
        );

        assertEquals("firstName/lastName is too long (max 64)", exception.getMessage());
    }

    @Test
    @DisplayName(value = "Should throw InvalidProfileException when lastName length is greater than 64")
    void constructor_shouldThrowInvalidProfileExceptionWhenLastNameLengthIsGreaterThan64() {
        String lastNameLongerThanAllowed = "A".repeat(65);

        InvalidProfileException exception = assertThrows(
                InvalidProfileException.class,
                () -> new Profile(VALID_FIRST_NAME, lastNameLongerThanAllowed, VALID_BIRTH_DATE)
        );

        assertEquals("firstName/lastName is too long (max 64)", exception.getMessage());
    }

    @Test
    @DisplayName(value = "Should create profile when firstName and lastName length is exactly 64")
    void shouldCreateProfileWhenFirstNameAndLastNameLengthIsExactly64() {
        String nameWithMaximumAllowedLength = "A".repeat(64);

        Profile profile = new Profile(nameWithMaximumAllowedLength, nameWithMaximumAllowedLength, VALID_BIRTH_DATE);

        assertAll(
                () -> assertEquals(nameWithMaximumAllowedLength, profile.firstName()),
                () -> assertEquals(nameWithMaximumAllowedLength, profile.lastName())
        );
    }

    @Test
    @DisplayName(value = "Should throw InvalidProfileException when firstName contains ISO control characters")
    void constructor_shouldThrowInvalidProfileExceptionWhenFirstNameContainsIsoControlCharacters() {
        String firstNameWithControlCharacter = "Jo\u0007hn";

        InvalidProfileException exception = assertThrows(
                InvalidProfileException.class,
                () -> new Profile(firstNameWithControlCharacter, VALID_LAST_NAME, VALID_BIRTH_DATE)
        );

        assertEquals("firstName/lastName contains invalid characters", exception.getMessage());
    }

    @Test
    @DisplayName(value = "Should throw InvalidProfileException when lastName contains ISO control characters")
    void constructor_shouldThrowInvalidProfileExceptionWhenLastNameContainsIsoControlCharacters() {
        String lastNameWithControlCharacter = "Do\u0000e";

        InvalidProfileException exception = assertThrows(
                InvalidProfileException.class,
                () -> new Profile(VALID_FIRST_NAME, lastNameWithControlCharacter, VALID_BIRTH_DATE)
        );

        assertEquals("firstName/lastName contains invalid characters", exception.getMessage());
    }

    @Test
    @DisplayName(value = "Should throw InvalidProfileException when profile owner is younger than 18 years old")
    void constructor_shouldThrowInvalidProfileExceptionWhenProfileOwnerIsYoungerThan18YearsOld() {
        LocalDate birthDateYoungerThanEighteenYears = LocalDate.now().minusYears(18).plusDays(1);

        InvalidProfileException exception = assertThrows(
                InvalidProfileException.class,
                () -> new Profile(VALID_FIRST_NAME, VALID_LAST_NAME, birthDateYoungerThanEighteenYears)
        );

        assertEquals("profile owner must be at least 18 years old", exception.getMessage());
    }

    @Test
    @DisplayName(value = "Should create profile when profile owner is exactly 18 years old")
    void shouldCreateProfileWhenProfileOwnerIsExactly18YearsOld() {
        LocalDate birthDateExactlyEighteenYears = LocalDate.now().minusYears(18);

        Profile profile = new Profile(VALID_FIRST_NAME, VALID_LAST_NAME, birthDateExactlyEighteenYears);

        assertEquals(birthDateExactlyEighteenYears, profile.birthDate());
    }

}