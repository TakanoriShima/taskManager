package action;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import model.User;
import repository.TaskRepository;

public class FavoriteToggleAction extends BaseAuthAction {

    private TaskRepository taskRepository = new TaskRepository();

    @Override
    protected String executeAuthenticated(HttpServletRequest request,
                                          HttpServletResponse response,
                                          User loginUser)
            throws ServletException, IOException {

        String taskIdParam = request.getParameter("taskId");

        if (taskIdParam == null || !taskIdParam.matches("\\d+")) {
            return "redirect:/app/task/list";
        }

        int taskId = Integer.parseInt(taskIdParam);
        int userId = loginUser.getId();

        try {
            if (!taskRepository.isOwner(taskId, userId)) {
                return "redirect:/app/task/list";
            }

            taskRepository.toggleFavorite(taskId, userId);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return "redirect:/app/task/list";
    }
}

