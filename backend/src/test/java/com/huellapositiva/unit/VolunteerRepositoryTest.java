package com.huellapositiva.unit;

import com.huellapositiva.domain.ExpressRegistrationVolunteer;
import com.huellapositiva.domain.Roles;
import com.huellapositiva.domain.exception.RoleNotFoundException;
import com.huellapositiva.domain.repository.VolunteerRepository;
import com.huellapositiva.domain.valueobjects.EmailConfirmation;
import com.huellapositiva.domain.valueobjects.PasswordHash;
import com.huellapositiva.infrastructure.orm.repository.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static com.huellapositiva.util.TestData.DEFAULT_EMAIL;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class VolunteerRepositoryTest {
    @Mock
    private JpaVolunteerRepository jpaVolunteerRepository;
    @Mock
    private JpaEmailConfirmationRepository jpaEmailConfirmationRepository;
    @Mock
    private JpaRoleRepository jpaRoleRepository;

    @Test
    void saving_volunteer_should_throw_role_not_found_if_role_doesnt_exist() {
        // GIVEN
        EmailConfirmation emailConfirmation = EmailConfirmation.from(DEFAULT_EMAIL, "");
        PasswordHash passwordHash = new PasswordHash("123456");
        ExpressRegistrationVolunteer expressRegistrationVolunteer = new ExpressRegistrationVolunteer(passwordHash, emailConfirmation);
        when(jpaRoleRepository.findByName(Roles.VOLUNTEER_NOT_CONFIRMED.toString())).thenReturn(Optional.empty());

        // WHEN + THEN
        VolunteerRepository volunteerRepository = new VolunteerRepository(jpaVolunteerRepository, jpaEmailConfirmationRepository, jpaRoleRepository);
        assertThrows(RoleNotFoundException.class, () -> volunteerRepository.save(expressRegistrationVolunteer));
    }

}
