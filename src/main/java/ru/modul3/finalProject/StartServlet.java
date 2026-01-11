package ru.modul3.finalProject;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet("/start")
public class StartServlet extends HttpServlet {

    private static final String GAME_START = "/game?step=start";

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("/index.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String playerName = req.getParameter("playerName");
        if (playerName == null || playerName.trim().isEmpty()) {
            playerName = "Путишественник на дачу";
        }

        HttpSession session = req.getSession();
        // Сохраняем  имя  в  сессии
        session.setAttribute("playerName", playerName.trim());
        session.setAttribute("gamesPlayer", 0);

        resp.sendRedirect(req.getContextPath() + GAME_START);
    }
}
