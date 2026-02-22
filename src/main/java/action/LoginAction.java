package action;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import controller.Action;
import model.User;
import repository.UserRepository;
import util.SessionUtil;

public class LoginAction implements Action {

    private UserRepository userRepository = new UserRepository();

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        if ("GET".equalsIgnoreCase(request.getMethod())) {
            return "/WEB-INF/views/login.jsp";
        }

        String username = request.getParameter("username");
        String password = request.getParameter("password");

        User user = userRepository.findByUsername(username);

        if (user == null || !user.getPassword().equals(password)) {
            request.setAttribute("error", "ユーザー名またはパスワードが違います");
            return "/WEB-INF/views/login.jsp";
        }

        SessionUtil.login(request, user);
        return getReturnUrl(request);
    }

    private String getReturnUrl(HttpServletRequest request) {
        HttpSession session = request.getSession();
        String returnUrl = (String) session.getAttribute("returnUrl");

        if (returnUrl != null) {
            session.removeAttribute("returnUrl");
            return "redirect:" + returnUrl;
        }

        return "redirect:/app/home";
    }
}

