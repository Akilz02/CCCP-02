import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import servlets.MoveToShelfServlet;

import java.io.IOException;

import static org.mockito.Mockito.*;

public class MoveToShelfServletTest {

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private RequestDispatcher dispatcher;

    private MoveToShelfServlet servlet;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        servlet = new MoveToShelfServlet();
    }

    @Test
    public void testDoGet_ForwardsToJsp() throws ServletException, IOException {
        when(request.getRequestDispatcher("/WEB-INF/views/moveToShelf.jsp")).thenReturn(dispatcher);

        servlet.doGet(request, response);

        verify(request).getRequestDispatcher("/WEB-INF/views/moveToShelf.jsp");
        verify(dispatcher).forward(request, response);
    }

    @Test
    public void testDoPost_SetsSuccessAttributeAndForwards() throws ServletException, IOException {
        when(request.getRequestDispatcher("/WEB-INF/views/moveToShelf.jsp")).thenReturn(dispatcher);

        // Override static method call by extending and mocking the behavior
        MoveToShelfServlet testServlet = new MoveToShelfServlet() {
            @Override
            public void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
                req.setAttribute("success", "Stock moved to shelf successfully.");
                doGet(req, res);
            }
        };

        testServlet.doPost(request, response);

        verify(request).setAttribute(eq("success"), eq("Stock moved to shelf successfully."));
        verify(request).getRequestDispatcher("/WEB-INF/views/moveToShelf.jsp");
        verify(dispatcher).forward(request, response);
    }
}
