package controller;

import javax.servlet.http.HttpServletRequest;

import action.FavoriteToggleAction;
import action.HomeAction;
import action.LoginAction;
import action.LogoutAction;
import action.TaskDeleteAction;
import action.TaskEditAction;
import action.TaskListAction;
import action.TaskNewAction;

public class ActionFactory {

    public static Action getAction(HttpServletRequest request) {

        String path = request.getPathInfo();
        if (path == null) return null;

        switch (path) {

            case "/login":
                return new LoginAction();

            case "/logout":
                return new LogoutAction();

            case "/home":
                return new HomeAction();

            case "/task/list":
                return new TaskListAction();

            case "/task/new":
                return new TaskNewAction();

            case "/task/edit":
                return new TaskEditAction();

            case "/task/delete":
                return new TaskDeleteAction();

            case "/favorite/toggle":
                return new FavoriteToggleAction();

            default:
                return null;
        }
    }
}
