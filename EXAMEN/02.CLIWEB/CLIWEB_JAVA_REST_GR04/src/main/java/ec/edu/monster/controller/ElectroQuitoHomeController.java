package ec.edu.monster.controller;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/electroquito/home")
public class ElectroQuitoHomeController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        Object usuarioSesion = request.getSession().getAttribute("usuarioSesion");
        if (usuarioSesion == null) {
            response.sendRedirect(request.getContextPath() + "/electroquito/login");
            return;
        }

        RequestDispatcher rd = request.getRequestDispatcher(
                "/WEB-INF/views/electroquito/electroquitoHome.jsp");
        rd.forward(request, response);
    }
}
