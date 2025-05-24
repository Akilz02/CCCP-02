import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;
import models.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import servlets.AddToCartServlet;

import java.io.IOException;

import static org.mockito.Mockito.*;

public class AddToCartServletTest {

    @Mock private HttpServletRequest request;
    @Mock private HttpServletResponse response;
    @Mock private HttpSession session;
    @Mock private RequestDispatcher dispatcher;
    @Mock private User user;

    private AddToCartServlet servlet;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        servlet = spy(new AddToCartServlet());

        when(request.getSession()).thenReturn(session);
        when(request.getRequestDispatcher("/WEB-INF/views/addToCart.jsp")).thenReturn(dispatcher);
    }

    @Test
    public void should_RedirectToLogin_when_UserIsNull() throws ServletException, IOException {
        when(session.getAttribute("currentUser")).thenReturn(null);


    }

    @Test
    public void should_ShowError_when_QuantityIsInvalid() throws ServletException, IOException {
        when(session.getAttribute("currentUser")).thenReturn(user);
        when(user.getUsername()).thenReturn("jane");
        when(request.getParameter("itemId")).thenReturn("111");
        when(request.getParameter("quantity")).thenReturn("abc");

        servlet.doPost(request, response);

        verify(request).setAttribute(eq("error"), eq("Invalid quantity."));
        verify(dispatcher).forward(request, response);
    }

    @Test
    public void should_ShowError_when_ItemNotInStock() throws ServletException, IOException {
        when(session.getAttribute("currentUser")).thenReturn(user);
        when(user.getUsername()).thenReturn("jane");
        when(request.getParameter("itemId")).thenReturn("111");
        when(request.getParameter("quantity")).thenReturn("1");

        servlet.doPost(request, response);

        verify(request).setAttribute(eq("error"), eq("Item is not available in the stock."));
        verify(dispatcher).forward(request, response);
    }

//    @Test
//    public void should_AddToCart_when_ValidDataProvided() throws ServletException, IOException {
////        when(session.getAttribute("currentUser")).thenReturn(user);
//        when(user.getUsername()).thenReturn("jane");
//        when(request.getParameter("itemId")).thenReturn("111");
//        when(request.getParameter("quantity")).thenReturn("2");
//
//
////        servlet.doPost(request, response);
//
//        verify(request).setAttribute(eq("success"), eq("Item added to cart."));
//        verify(dispatcher).forward(request, response);
//    }
}
