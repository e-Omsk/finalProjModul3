package ru.modul3.finalProject;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import java.io.IOException;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class StartServletTest {

    private StartServlet startServlet;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private HttpSession session;

    @Mock
    private RequestDispatcher requestDispatcher;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        startServlet = new StartServlet();

        when(request.getSession()).thenReturn(session);
        when(request.getRequestDispatcher("/index.jsp")).thenReturn(requestDispatcher);
        when(request.getContextPath()).thenReturn("/ToTheDacha");
    }

    @Test
    public void testDoGet_ShouldForwardToIndexPage() throws ServletException, IOException {

        startServlet.doGet(request, response);

        verify(request).getRequestDispatcher("/index.jsp");
        verify(requestDispatcher).forward(request, response);
    }

    @Test
    public void testDoPost_WithValidName_ShouldSetSessionAttributes() throws ServletException, IOException {

        String playerName = "Слава";
        when(request.getParameter("playerName")).thenReturn(playerName);

        startServlet.doPost(request, response);

        verify(session).setAttribute("playerName", playerName);
        verify(session).setAttribute("gamesPlayer", 0);
        verify(response).sendRedirect("/ToTheDacha/game?step=start");
    }

    @Test
    public void testDoPost_WithEmptyName_ShouldSetDefaultName() throws ServletException, IOException {

        when(request.getParameter("playerName")).thenReturn("   ");


        startServlet.doPost(request, response);


        verify(session).setAttribute("playerName", "Путишественник на дачу");
        verify(session).setAttribute("gamesPlayer", 0);
    }

    @Test
    public void testDoPost_WithNullName_ShouldSetDefaultName() throws ServletException, IOException {

        when(request.getParameter("playerName")).thenReturn(null);


        startServlet.doPost(request, response);


        verify(session).setAttribute("playerName", "Путишественник на дачу");
    }

    @Test
    public void testDoPost_ShouldTrimPlayerName() throws ServletException, IOException {
        // Given
        when(request.getParameter("playerName")).thenReturn("  Слава  ");

        // When
        startServlet.doPost(request, response);

        // Then
        verify(session).setAttribute("playerName", "Слава");
    }
}