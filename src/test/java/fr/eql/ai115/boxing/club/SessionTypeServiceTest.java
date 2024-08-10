package fr.eql.ai115.boxing.club;

import fr.eql.ai115.boxing.club.entity.Session;
import fr.eql.ai115.boxing.club.service.impl.ApplicationService;
import fr.eql.ai115.boxing.club.service.impl.SessionService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class SessionTypeServiceTest {

    @Mock
    private SessionService sessionService;

    @InjectMocks
    private ApplicationService applicationService;

    @Test
    void testFindAllSessions() {
        List<Session> expectedSessions = Arrays.asList(new Session(), new Session());
        when(sessionService.findAllSessions()).thenReturn(expectedSessions);

        List<Session> actualSessions = applicationService.findAllSessions();

        assertEquals(expectedSessions, actualSessions);
    }
}
