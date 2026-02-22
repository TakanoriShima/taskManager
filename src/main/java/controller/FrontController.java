package controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

    @WebServlet("/app/*")
    public class FrontController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        process(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        process(request, response);
    }

    private void process(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
    	
    	request.setCharacterEncoding("UTF-8");
    	response.setCharacterEncoding("UTF-8");

        Action action = ActionFactory.getAction(request);
        if (action == null) {
            response.sendError(404);
            return;
        }

        String result = action.execute(request, response);
        if (result == null) return;

        if (result.startsWith("redirect:")) {
            String path = result.substring("redirect:".length()).trim();

            String contextPath = request.getContextPath();

            if (!path.startsWith("/")) {
                path = "/" + path;
            }

            if (path.startsWith(contextPath + "/") || path.equals(contextPath)) {
                response.sendRedirect(path);
            } else {
                response.sendRedirect(contextPath + path);
            }
            return;
        }

        request.getRequestDispatcher(result).forward(request, response);
    }
}

