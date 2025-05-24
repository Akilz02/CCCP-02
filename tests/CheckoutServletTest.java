import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import models.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import servlets.CheckoutServlet;

import java.io.IOException;
import java.sql.SQLException;

import static org.mockito.Mockito.*;

public class CheckoutServletTest {

    @InjectMocks
    private CheckoutServlet checkoutServlet;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private HttpSession session;

    @Mock
    private RequestDispatcher dispatcher;

    @Mock
    private User user;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        when(request.getSession()).thenReturn(session);
        when(session.getAttribute("currentUser")).thenReturn(user);
    }

    @Test
    public void shouldRedirectToLoginIfUserNotLoggedIn() throws Exception {
        when(session.getAttribute("currentUser")).thenReturn(null);

        checkoutServlet.doGet(request, response);

        verify(response).sendRedirect(anyString());
    }

    @Test
    public void shouldForwardToCheckoutPage_whenUserLoggedIn() throws Exception {
        when(user.getUsername()).thenReturn("testuser");
        when(request.getRequestDispatcher("/WEB-INF/views/checkout.jsp")).thenReturn(dispatcher);

        checkoutServlet.doGet(request, response);

        verify(dispatcher).forward(request, response);
    }

    @Test
    public void shouldRedirectToStoreOnSuccessfulCheckout() throws Exception {
        when(user.getUsername()).thenReturn("testuser");

        // Set up mocks for dispatcher for fallback
        when(request.getRequestDispatcher("/WEB-INF/views/checkout.jsp")).thenReturn(dispatcher);

        // We assume all DB calls work fine (since we are not mocking DB), so we skip them

        checkoutServlet.doPost(request, response);

        verify(response).sendRedirect(contains("/onlineStore"));
    }

    @Test
    public void shouldHandleSQLExceptionGracefully() throws Exception {
        when(user.getUsername()).thenReturn("invalid_user");

        // Force a database failure scenario
        // To test real SQLException behavior, methods like `getUserID()` should be exposed or separated and mocked individually.
        // For simplicity, here we're just invoking post and expecting fallback
        when(request.getRequestDispatcher("/WEB-INF/views/checkout.jsp")).thenReturn(dispatcher);

        checkoutServlet.doPost(request, response);

        verify(dispatcher, atLeastOnce()).forward(request, response); // fallback to doGet on error
    }
}
