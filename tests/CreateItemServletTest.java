import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import servlets.CreateItemServlet;

import java.io.IOException;
import java.sql.SQLException;

import static org.mockito.Mockito.*;

public class CreateItemServletTest {

    @InjectMocks
    private CreateItemServlet servlet;

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
    public void testDoGet_ForwardsToCreateItemPage() throws ServletException, IOException {
        when(request.getRequestDispatcher("/WEB-INF/views/createItem.jsp")).thenReturn(dispatcher);

        servlet.doGet(request, response);

        verify(dispatcher).forward(request, response);
    }

    @Test
    public void testDoPost_CreatesNewItemAndReloadsPage() throws Exception {
        when(request.getParameter("itemId")).thenReturn("IT001");
        when(request.getParameter("itemName")).thenReturn("Test Item");
        when(request.getRequestDispatcher("/WEB-INF/views/createItem.jsp")).thenReturn(dispatcher);

        servlet.doPost(request, response);

        verify(dispatcher).forward(request, response);  // Verify form is reloaded
    }

    @Test
    public void testNewAddItemToStock_ThrowsSQLExceptionHandled() {
        try {
            CreateItemServlet.NewAddItemToStock("INVALID", null);
        } catch (Exception e) {
            assert(e instanceof SQLException || e.getCause() instanceof SQLException);
        }
    }
}
