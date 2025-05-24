import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import servlets.CreateShelfServlet;

import java.io.IOException;

import static org.mockito.Mockito.*;

public class CreateShelfServletTest {

    @InjectMocks
    private CreateShelfServlet servlet;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private RequestDispatcher dispatcher;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testDoGet_ForwardsToCreateShelfPage() throws ServletException, IOException {
        when(request.getRequestDispatcher("/WEB-INF/views/createShelf.jsp")).thenReturn(dispatcher);

        servlet.doGet(request, response);

        verify(dispatcher).forward(request, response);
    }

    @Test
    public void testDoPost_WithValidInput_CreatesShelfAndReloadsPage() throws Exception {
        when(request.getParameter("shelfId")).thenReturn("S001");
        when(request.getParameter("shelfCapacity")).thenReturn("100");
        when(request.getRequestDispatcher("/WEB-INF/views/createShelf.jsp")).thenReturn(dispatcher);

        servlet.doPost(request, response);

        verify(dispatcher).forward(request, response);  // JSP reload after success
    }

    @Test
    public void testDoPost_WithInvalidCapacity_ThrowsNumberFormatException() throws Exception {
        when(request.getParameter("shelfId")).thenReturn("S002");
        when(request.getParameter("shelfCapacity")).thenReturn("invalid");

        try {
            servlet.doPost(request, response);
        } catch (NumberFormatException e) {
            // Test passes if exception is thrown (due to no try-catch in actual code)
            assert(true);
            return;
        }
        assert(false); // Should not reach here
    }
}
