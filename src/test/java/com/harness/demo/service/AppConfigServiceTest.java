package com.harness.demo.service;

import com.harness.demo.model.AppConfig;
import com.harness.demo.repository.AppConfigRepository;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AppConfigServiceTest {
    @Mock AppConfigRepository repository;
    @InjectMocks AppConfigService service;

    @Test void normalizeTrimsAndCollapsesWhitespace() {
        assertEquals("Harness demo", service.normalize("  Harness   demo "));
    }

    @Test void normalizeNullReturnsEmpty() {
        assertEquals("", service.normalize(null));
    }

    @Test void healthyHeadingRequiresThreeCharacters() {
        assertFalse(service.isHealthyHeading("ab"));
        assertTrue(service.isHealthyHeading("abc"));
    }

    @Test void healthyHeadingHandlesNull() {
        assertFalse(service.isHealthyHeading(null));
    }

    @Test void scoreForHarnessHeadingAndLongSubtitleIsHundred() {
        assertEquals(100, service.score("Harness CI", "This is a long subtitle"));
    }

    @Test void scoreForBasicHeadingIsFifty() {
        assertEquals(50, service.score("Demo", null));
    }

    @Test void scoreAddsSubtitlePoints() {
        assertEquals(75, service.score("Demo", "A long enough subtitle"));
    }

    @Test void updateRejectsBlankHeading() {
        assertThrows(IllegalArgumentException.class, () -> service.update(" ", "x"));
        verifyNoInteractions(repository);
    }

    @Test void updateRejectsLongHeading() {
        assertThrows(IllegalArgumentException.class, () -> service.update("x".repeat(121), "x"));
    }

    @Test void updateRejectsLongSubtitle() {
        assertThrows(IllegalArgumentException.class, () -> service.update("Demo", "x".repeat(301)));
    }

    @Test void updateNormalizesAndReturnsFreshConfig() {
        AppConfig updated = new AppConfig(1L, "Demo", "Subtitle", System.currentTimeMillis());
        when(repository.get()).thenReturn(updated);
        when(repository.update("Demo", "Subtitle")).thenReturn(1);
        AppConfig result = service.update(" Demo ", " Subtitle ");
        assertEquals("Demo", result.heading());
        verify(repository).update("Demo", "Subtitle");
    }

    @Test void updateAllowsNullSubtitle() {
        AppConfig updated = new AppConfig(1L, "Demo", "", System.currentTimeMillis());
        when(repository.get()).thenReturn(updated);
        service.update("Demo", null);
        verify(repository).update("Demo", "");
    }

    @Test @Disabled("Intentional failing case for Harness failure-path demo")
    void intentionalFailureExample() {
        assertEquals("expected-value", "actual-value");
    }

    @Test @Disabled("Intentional failing case for Harness failure-path demo")
    void intentionalSecondFailureExample() {
        assertTrue(false, "Intentional failure for demo");
    }
}
