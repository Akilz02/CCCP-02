import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import servlets.ReportsServlet;

import java.io.IOException;

import static org.mockito.Mockito.*;

public class ReportsServletTest {

    private ReportsServlet servlet;
    private HttpServletRequest request;
    private HttpServletResponse response;
    private RequestDispatcher dispatcher;

    @BeforeEach
    public void setUp() {
        servlet = new ReportsServlet();
        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);
        dispatcher = mock(RequestDispatcher.class);
    }

    @Test
    public void testDoGet_ForwardsToJsp() throws ServletException, IOException {
        when(request.getRequestDispatcher("/WEB-INF/views/reports.jsp")).thenReturn(dispatcher);

        servlet.doGet(request, response);

        verify(dispatcher).forward(request, response);
    }

    @Test
    public void testDoPost_ReorderReport() throws ServletException, IOException {
        when(request.getParameter("reportType")).thenReturn("reorder");
        when(request.getRequestDispatcher("/WEB-INF/views/reports.jsp")).thenReturn(dispatcher);

        servlet.doPost(request, response);

        verify(request).setAttribute(eq("success"), anyString());
        verify(dispatcher).forward(request, response);
    }

    @Test
    public void testDoPost_StockReport() throws ServletException, IOException {
        when(request.getParameter("reportType")).thenReturn("stock");
        when(request.getRequestDispatcher("/WEB-INF/views/reports.jsp")).thenReturn(dispatcher);

        servlet.doPost(request, response);

        verify(request).setAttribute(eq("success"), anyString());
        verify(dispatcher).forward(request, response);
    }

    @Test
    public void testDoPost_BillReport() throws ServletException, IOException {
        when(request.getParameter("reportType")).thenReturn("bill");
        when(request.getRequestDispatcher("/WEB-INF/views/reports.jsp")).thenReturn(dispatcher);

        servlet.doPost(request, response);

        verify(request).setAttribute(eq("success"), anyString());
        verify(dispatcher).forward(request, response);
    }
}
