package ru.modul3.finalProject;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import ru.modul3.finalProject.model.QuestStep;
import ru.modul3.finalProject.service.QuestService;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import java.io.IOException;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;


public class GameServletTest {

    private GameServlet gameServlet;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private HttpSession session;

    @Mock
    private RequestDispatcher requestDispatcher;

    @Mock
    private QuestService questService;

    @Before
    public void setUp() {
        // Инициализация мок-объектов Mockito: request, response, session, requestDispatcher, questService
        MockitoAnnotations.initMocks(this);
        // Создание реальный объект сервлета, который мы будем тестировать
        gameServlet = new GameServlet();

        // используем рефлексия для замены questService на Moc
        try {
            var field = GameServlet.class.getDeclaredField("questService");
            field.setAccessible(true);
            field.set(gameServlet, questService);
        } catch (Exception e) {
            throw new RuntimeException();
        }

        // Настройка поведения мок-объектов:

        // Когда вызывается request.getSession(), возвращаем мок-сессию
        when(request.getSession()).thenReturn(session);
        // Когда вызывается request.getRequestDispatcher() с любым строковым параметром,
        // возвращаем мок-диспетчер запросов
        when(request.getRequestDispatcher("/game.jsp")).thenReturn(requestDispatcher);
        // Когда вызывается request.getContextPath(), возвращаем  /quest
        when(request.getContextPath()).thenReturn("/quest");
    }


    @Test
    public void testDoGet_WithStepParameter_ShouldForwardToGamePage() throws ServletException, IOException {
        // Given
        String stepId = "start";
        QuestStep mockStep = new QuestStep(stepId, "Текст", "Опция1", "Опция2", "next1", "next2");

        when(request.getParameter("step")).thenReturn(stepId);
        when(questService.getSteps(stepId)).thenReturn(mockStep);

        // When
        gameServlet.doGet(request, response);

        // Then
        verify(request).setAttribute("step", mockStep);
        verify(request).getRequestDispatcher("/game.jsp");
        verify(requestDispatcher).forward(request, response);
    }

    @Test
    public void testDoGet_WithoutStepParameter_ShouldUseDefaultStart() throws ServletException, IOException {
        // Given
        when(request.getParameter("step")).thenReturn(null);
        QuestStep startStep = new QuestStep("start", "Текст", "Опция1", "Опция2", "next1", "next2");
        when(questService.getSteps("start")).thenReturn(startStep);

        // When
        gameServlet.doGet(request, response);

        // Then
        verify(questService).getSteps("start");
        verify(request).setAttribute("step", startStep);
    }

    @Test
    public void testDoGet_WithInvalidStep_ShouldSend404Error() throws ServletException, IOException {
        // Given
        String invalidStep = "invalid";
        when(request.getParameter("step")).thenReturn(invalidStep);
        when(questService.getSteps(invalidStep)).thenReturn(null);

        // When
        gameServlet.doGet(request, response);

        // Then
        verify(response).sendError(HttpServletResponse.SC_NOT_FOUND,
                "Шаг " + invalidStep + " не найден! Step: null");
        verify(request, never()).getRequestDispatcher(anyString());
    }

    @Test
    public void testDoPost_WithAnswer1_ShouldRedirectToNextStep() throws ServletException, IOException {
        // Given
        String currentStepId = "start";
        String nextStepId = "car";
        QuestStep currentStep = new QuestStep(currentStepId, "Текст", "Опция1", "Опция2", nextStepId, "lose");

        when(request.getParameter("answer")).thenReturn("1");
        when(request.getParameter("currentStep")).thenReturn(currentStepId);
        when(questService.getSteps(currentStepId)).thenReturn(currentStep);
        when(questService.isFinishStep(nextStepId)).thenReturn(false);

        // When
        gameServlet.doPost(request, response);

        // Then
        verify(response).sendRedirect("/quest/game?step=" + nextStepId);
    }

    @Test
    public void testDoPost_WithAnswer2_ShouldRedirectToNextStep() throws ServletException, IOException {
        // Given
        String currentStepId = "start";
        String nextStepId = "obshTransport";
        QuestStep currentStep = new QuestStep(currentStepId, "Текст", "Опция1", "Опция2", "car", nextStepId);

        when(request.getParameter("answer")).thenReturn("2");
        when(request.getParameter("currentStep")).thenReturn(currentStepId);
        when(questService.getSteps(currentStepId)).thenReturn(currentStep);

        // When
        gameServlet.doPost(request, response);

        // Then
        verify(response).sendRedirect("/quest/game?step=" + nextStepId);
    }

    @Test
    public void testDoPost_WithFinishStep_ShouldIncrementGamesPlayed() throws ServletException, IOException {
        // Given
        String currentStepId = "transferTranspont";
        String nextStepId = "win";
        QuestStep currentStep = new QuestStep(currentStepId, "Текст", "Опция1", "Опция2", nextStepId, "lose");

        when(request.getParameter("answer")).thenReturn("1");
        when(request.getParameter("currentStep")).thenReturn(currentStepId);
        when(questService.getSteps(currentStepId)).thenReturn(currentStep);
        when(questService.isFinishStep(nextStepId)).thenReturn(true);
        when(session.getAttribute("gamesPlayed")).thenReturn(2);

        // When
        gameServlet.doPost(request, response);

        // Then
        verify(session).setAttribute("gamesPlayed", 3);
        verify(response).sendRedirect("/quest/game?step=" + nextStepId);
    }

    @Test
    public void testDoPost_WithNullAnswer_ShouldRedirectToStart() throws ServletException, IOException {
        // Given
        when(request.getParameter("answer")).thenReturn(null);
        when(request.getParameter("currentStep")).thenReturn("start");

        // When
        gameServlet.doPost(request, response);

        // Then
        verify(response).sendRedirect("/quest/game?step=start");
        verify(questService, never()).getSteps(anyString());
    }

    @Test
    public void testDoPost_WithNullCurrentStep_ShouldRedirectToStart() throws ServletException, IOException {
        // Given
        when(request.getParameter("answer")).thenReturn("1");
        when(request.getParameter("currentStep")).thenReturn(null);

        // When
        gameServlet.doPost(request, response);

        // Then
        verify(response).sendRedirect("/quest/game?step=start");
    }

    @Test
    public void testDoPost_WithInvalidCurrentStep_ShouldRedirectToStart() throws ServletException, IOException {
        // Given
        when(request.getParameter("answer")).thenReturn("1");
        when(request.getParameter("currentStep")).thenReturn("invalid");
        when(questService.getSteps("invalid")).thenReturn(null);

        // When
        gameServlet.doPost(request, response);

        // Then
        verify(response).sendRedirect("/quest/game?step=start");
    }
    @Test
    public void testDoPost_WhenGamesPlayedIsNull_ShouldSetToOne() throws ServletException, IOException {
        // Given
        String currentStepId = "transferTranspont";
        String nextStepId = "win";
        QuestStep currentStep = new QuestStep(currentStepId, "Текст", "Опция1", "Опция2", nextStepId, "lose");

        when(request.getParameter("answer")).thenReturn("1");
        when(request.getParameter("currentStep")).thenReturn(currentStepId);
        when(questService.getSteps(currentStepId)).thenReturn(currentStep);
        when(questService.isFinishStep(nextStepId)).thenReturn(true);
        when(session.getAttribute("gamesPlayed")).thenReturn(null);

        // When
        gameServlet.doPost(request, response);

        // Then
        verify(session).setAttribute("gamesPlayed", 1);
    }
}