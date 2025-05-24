package servlets;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.IOException;

import static org.mockito.Mockito.*;

public class ShelfCapacityServletTest {

    private ShelfCapacityServlet servlet;
    private HttpServletRequest request;
    private HttpServletResponse response;
    private RequestDispatcher dispatcher;

    @BeforeEach
    public void setUp() {
        servlet = Mockito.spy(new ShelfCapacityServlet());
        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);
        dispatcher = mock(RequestDispatcher.class);
    }

    @Test
    public void testDoPost_MissingParameters() throws ServletException, IOException {
        when(request.getParameter("shelfId")).thenReturn("");
        when(request.getParameter("itemId")).thenReturn("");
        when(request.getParameter("capacityLimit")).thenReturn("");
        when(request.getRequestDispatcher("/WEB-INF/views/shelfCapacity.jsp")).thenReturn(dispatcher);

        servlet.doPost(request, response);

        verify(request).setAttribute(eq("error"), eq("All fields are required"));
        verify(dispatcher).forward(request, response);
    }

    @Test
    public void testDoPost_InvalidCapacityLimit() throws ServletException, IOException {
        when(request.getParameter("shelfId")).thenReturn("S001");
        when(request.getParameter("itemId")).thenReturn("I001");
        when(request.getParameter("capacityLimit")).thenReturn("abc");
        when(request.getRequestDispatcher("/WEB-INF/views/shelfCapacity.jsp")).thenReturn(dispatcher);

        servlet.doPost(request, response);

        verify(request).setAttribute(eq("error"), eq("Capacity limit must be a number"));
        verify(dispatcher).forward(request, response);
    }
}
