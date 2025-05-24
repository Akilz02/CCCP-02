import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import servlets.CustomerBillingServlet;

import java.io.IOException;
import java.util.ArrayList;

import static org.mockito.Mockito.*;

public class CustomerBillingServletTest {

    @InjectMocks
    private CustomerBillingServlet servlet;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private RequestDispatcher dispatcher;

    @Mock
    private HttpSession session;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testDoGet_ForwardsToBillingPage() throws ServletException, IOException {
        when(request.getRequestDispatcher("/WEB-INF/views/customerBilling.jsp")).thenReturn(dispatcher);
        when(request.getSession()).thenReturn(session);
        when(session.getAttribute("bill")).thenReturn(new ArrayList<CustomerBillingServlet.BillItem>());

        servlet.doGet(request, response);

        verify(dispatcher).forward(request, response);
    }

    @Test
    public void testDoPost_WithInvalidQuantity_ShowsError() throws ServletException, IOException {
        when(request.getParameter("itemId")).thenReturn("I001");
        when(request.getParameter("batchId")).thenReturn("B001");
        when(request.getParameter("quantity")).thenReturn("invalid");  // Not a number

        when(request.getRequestDispatcher("/WEB-INF/views/customerBilling.jsp")).thenReturn(dispatcher);
        when(request.getSession()).thenReturn(session);
        when(session.getAttribute("bill")).thenReturn(new ArrayList<>());

        servlet.doPost(request, response);

        verify(request).setAttribute(eq("error"), contains("Invalid quantity"));
        verify(dispatcher).forward(request, response);
    }

    // More realistic test cases require mocking the DatabaseConnection and ResultSet,
    // which involves further abstraction or mocking static/singleton methods (advanced)

}
