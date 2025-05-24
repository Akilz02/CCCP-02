import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import servlets.IndexServlet;

import java.io.IOException;

import static org.mockito.Mockito.*;

public class IndexServletTest {

    private IndexServlet servlet;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private RequestDispatcher dispatcher;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        servlet = new IndexServlet();
    }

    @Test
    public void testDoGet_ForwardsToIndexJsp() throws ServletException, IOException {
        // Arrange
        when(request.getRequestDispatcher("/WEB-INF/views/index.jsp")).thenReturn(dispatcher);

        // Act
        servlet.doGet(request, response);

        // Assert
        verify(request).getRequestDispatcher("/WEB-INF/views/index.jsp");
        verify(dispatcher).forward(request, response);
    }
}
