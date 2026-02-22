package action;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import model.User;

public class HomeAction extends BaseAuthAction {

    @Override
    protected String executeAuthenticated(
            HttpServletRequest request,
            HttpServletResponse response,
            User loginUser) throws ServletException, IOException {

        request.setAttribute("loginUser", loginUser);
        return "/WEB-INF/views/home.jsp";
    }
}
