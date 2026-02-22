package util;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import model.User;

public class AuthUtil {

    public static User getLoginUser(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session == null) return null;
        return (User) session.getAttribute("loginUser");
    }

    public static boolean isLoggedIn(HttpServletRequest request) {
        return getLoginUser(request) != null;
    }

    public static boolean isOwner(Integer loginUserId, Integer resourceUserId) {
        return loginUserId != null && loginUserId.equals(resourceUserId);
    }

    public static boolean canAccess(HttpServletRequest request, Integer resourceUserId) {
        User loginUser = getLoginUser(request);
        if (loginUser == null) return false;
        return isOwner(loginUser.getId(), resourceUserId);
    }

    public static String redirectToLogin(HttpServletRequest request) {
        String requestURI = request.getRequestURI();
        String queryString = request.getQueryString();
        String returnUrl = requestURI + (queryString != null ? "?" + queryString : "");

        HttpSession session = request.getSession();
        session.setAttribute("returnUrl", returnUrl);

        return "redirect:/app/login";
    }
}

