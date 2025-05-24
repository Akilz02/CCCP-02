import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import servlets.OnlineStorePageServlet;

import java.io.IOException;

import static org.mockito.Mockito.*;

class OnlineStorePageServletTest {

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private HttpSession session;

    @Mock
    private RequestDispatcher dispatcher;

    private OnlineStorePageServlet servlet;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        servlet = new OnlineStorePageServlet();
    }

    @Test
    public void testDoGet_runsWithoutError() throws ServletException, IOException {
        when(request.getRequestDispatcher("/WEB-INF/views/onlineStorePage.jsp")).thenReturn(dispatcher);

        servlet.doGet(request, response);

        verify(request).setAttribute(eq("items"), any());
        verify(dispatcher).forward(request, response);
    }

    @Test
    public void testDoPost_logoutAction_runsWithoutError() throws ServletException, IOException {
        when(request.getParameter("action")).thenReturn("logout");
        when(request.getSession()).thenReturn(session);
        when(request.getRequestDispatcher("/WEB-INF/views/onlineStoreLogin.jsp")).thenReturn(dispatcher);

        servlet.doPost(request, response);

        verify(session).invalidate();
        verify(dispatcher).forward(request, response);
    }

    @Test
    public void testDoPost_otherAction_doesNothing() throws ServletException, IOException {
        when(request.getParameter("action")).thenReturn("someOtherAction");

        servlet.doPost(request, response);

        // No forwarding or session invalidation expected here
        verifyNoInteractions(session);
        verify(response, never()).sendRedirect(anyString());
    }
}
