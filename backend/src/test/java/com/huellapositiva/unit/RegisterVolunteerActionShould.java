package com.huellapositiva.unit;

import com.huellapositiva.application.dto.RegisterVolunteerRequestDto;
import com.huellapositiva.domain.actions.RegisterVolunteerAction;
import com.huellapositiva.domain.service.VolunteerService;
import com.huellapositiva.domain.valueobjects.EmailTemplate;
import com.huellapositiva.infrastructure.EmailService;
import com.huellapositiva.infrastructure.TemplateService;
import com.huellapositiva.infrastructure.orm.service.IssueService;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.mock;

@ExtendWith(MockitoExtension.class)
class RegisterVolunteerActionShould {
    @Mock
    TemplateService templateService;
    @Mock
    VolunteerService volunteerService;
    @Mock
    EmailService emailService;
    @Mock
    IssueService issueService;

    private RegisterVolunteerAction registerVolunteerAction;

    @BeforeEach
    void beforeEach() {
        registerVolunteerAction = new RegisterVolunteerAction(
                volunteerService, emailService, templateService, issueService);
    }

    @Test
    void send_confirmation_email() {

        String template = "Te acabas de registrar en huellapositiva.com\n" +
                          "Para confirmar tu correo electrónico, haz clic en el enlace\n" +
                          "<a href=\"${CONFIRMATION_URL}\">Clic aquí</a>\n";
        lenient().when(templateService.getEmailConfirmationTemplate(any())).thenReturn(new EmailTemplate(template));

        registerVolunteerAction.execute(RegisterVolunteerRequestDto.builder()
               .email("foo@huellapositiva.com")
               .password("123456")
               .build());

        verify(emailService).sendEmail(any());
    }

    @Test
    void registering_volunteer_throws_exception_in_case_of_failure_sending_confirmation_email(){
        //GIVEN
        RegisterVolunteerRequestDto dto = new RegisterVolunteerRequestDto("foo@huellapositiva.com", "plain-password");
        lenient().doThrow(RuntimeException.class).when(emailService).sendEmail(any());

        //THEN
        Assert.assertThrows(RuntimeException.class, () -> registerVolunteerAction.execute(dto));
    }

    @Test
    void registering_a_volunteer_failure_send_email_confirmation_should_save_email_address_in_db(){
        //GIVEN
        RegisterVolunteerRequestDto dto = new RegisterVolunteerRequestDto("foo@huellapositiva.com", "plain-password");
        lenient().doThrow(RuntimeException.class).when(emailService).sendEmail(any());

        try {
            registerVolunteerAction.execute(dto);
        } catch (Exception ex) {

        }

        //THEN
        verify(issueService).registerFailSendEmailConfirmation(any(),any());
    }
}
