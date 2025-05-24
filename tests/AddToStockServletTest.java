import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import servlets.AddToStockServlet;

import java.io.IOException;
import java.sql.Date;

import static org.mockito.Mockito.*;

public class AddToStockServletTest {

    @Mock private HttpServletRequest request;
    @Mock private HttpServletResponse response;
    @Mock private RequestDispatcher dispatcher;

    private AddToStockServlet servlet;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        servlet = spy(new AddToStockServlet());

        when(request.getRequestDispatcher("/WEB-INF/views/addToStock.jsp")).thenReturn(dispatcher);
    }

    @Test
    public void should_ShowError_when_InvalidInputGiven() throws ServletException, IOException {
        when(request.getParameter("itemId")).thenReturn("123");
        when(request.getParameter("batchId")).thenReturn("B001");
        when(request.getParameter("quantity")).thenReturn("abc"); // Invalid integer
        when(request.getParameter("unitPrice")).thenReturn("10.5");
        when(request.getParameter("dateOfPurchase")).thenReturn("2025-05-01");
        when(request.getParameter("expiryDate")).thenReturn("2025-10-01");

        servlet.doPost(request, response);

        verify(request).setAttribute(eq("error"), eq("Invalid input values."));
        verify(dispatcher).forward(request, response);
    }

    @Test
    public void should_ShowError_when_BatchAlreadyExists() throws ServletException, IOException {
        when(request.getParameter("itemId")).thenReturn("123");
        when(request.getParameter("batchId")).thenReturn("B001");
        when(request.getParameter("quantity")).thenReturn("10");
        when(request.getParameter("unitPrice")).thenReturn("15.75");
        when(request.getParameter("dateOfPurchase")).thenReturn("2025-05-01");
        when(request.getParameter("expiryDate")).thenReturn("2025-10-01");

        doReturn(true).when(servlet).checkBatchAvailabilityInStock("123", "B001");

        servlet.doPost(request, response);

        verify(request).setAttribute(eq("error"), eq("Batch is already available in the stock."));
        verify(dispatcher).forward(request, response);
    }

    @Test
    public void should_SaveBatch_when_ValidInputProvided() throws ServletException, IOException {
        when(request.getParameter("itemId")).thenReturn("123");
        when(request.getParameter("batchId")).thenReturn("B002");
        when(request.getParameter("quantity")).thenReturn("20");
        when(request.getParameter("unitPrice")).thenReturn("30.50");
        when(request.getParameter("dateOfPurchase")).thenReturn("2025-05-01");
        when(request.getParameter("expiryDate")).thenReturn("2025-10-01");

        doReturn(false).when(servlet).checkBatchAvailabilityInStock("123", "B002");
        doNothing().when(servlet).saveBatchToStock(
                eq("123"), eq("B002"), eq(20), eq(30.50),
                any(Date.class), any(Date.class)
        );

        servlet.doPost(request, response);

        verify(servlet).saveBatchToStock(eq("123"), eq("B002"), eq(20), eq(30.50),
                eq(Date.valueOf("2025-05-01")), eq(Date.valueOf("2025-10-01")));
        verify(dispatcher).forward(request, response);
    }
}
