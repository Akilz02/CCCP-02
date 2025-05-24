package servlets;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.mockito.Mockito.*;

public class ViewItemsServletTest {

    private ViewItemsServlet servlet;
    private HttpServletRequest request;
    private HttpServletResponse response;
    private RequestDispatcher dispatcher;

    @BeforeEach
    public void setUp() {
        servlet = new ViewItemsServlet();
        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);
        dispatcher = mock(RequestDispatcher.class);

        when(request.getRequestDispatcher("/WEB-INF/views/viewItems.jsp")).thenReturn(dispatcher);
    }

    @Test
    public void testDoGet_ForwardsToView() throws ServletException, IOException {
        servlet.doGet(request, response);

        verify(request).setAttribute(eq("items"), any());
        verify(dispatcher).forward(request, response);
    }
}
