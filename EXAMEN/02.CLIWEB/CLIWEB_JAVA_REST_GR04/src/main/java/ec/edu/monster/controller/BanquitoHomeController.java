package ec.edu.monster.controller;

import java.io.IOException;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/banquito/home")
public class BanquitoHomeController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response)
            throws ServletException, IOException {

        // Si luego quieres validar sesión, lo haces aquí:
        // Object usuario = request.getSession().getAttribute("usuarioSesion");
        // if (usuario == null) {
        //     response.sendRedirect(request.getContextPath() + "/banquito/login");
        //     return;
        // }

        RequestDispatcher rd = request.getRequestDispatcher(
                "/WEB-INF/views/banquito/banquitoHome.jsp"
        );
        rd.forward(request, response);
    }
}
