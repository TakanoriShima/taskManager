package action;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import model.Task;
import model.User;
import repository.TaskRepository;
import util.AuthUtil;

public class TaskDeleteAction extends BaseAuthAction {

    private TaskRepository taskRepository = new TaskRepository();

    @Override
    protected String executeAuthenticated(
            HttpServletRequest request,
            HttpServletResponse response,
            User loginUser) throws ServletException, IOException {

        String idParam = request.getParameter("id");
        if (idParam == null || idParam.trim().isEmpty()) {
            return "redirect:/app/task/list";
        }

        int taskId;
        try {
            taskId = Integer.parseInt(idParam);
        } catch (NumberFormatException e) {
            return "redirect:/app/task/list";
        }

        Task task = taskRepository.findById(taskId);
        if (task == null || !AuthUtil.isOwner(loginUser.getId(), task.getUserId())) {
            response.sendError(403, "アクセス権限がありません");
            return null;
        }

        boolean deleted = taskRepository.deleteByIdAndUserId(taskId, loginUser.getId());

        HttpSession session = request.getSession();
        if (deleted) {
            session.setAttribute("message", "タスクを削除しました");
        } else {
            session.setAttribute("error", "削除に失敗しました");
        }

        return "redirect:/app/task/list";
    }
}
