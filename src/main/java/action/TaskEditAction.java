package action;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import model.Task;
import model.User;
import repository.TaskRepository;
import util.ValidationUtil;

public class TaskEditAction extends BaseAuthAction {

    private TaskRepository taskRepository = new TaskRepository();

    @Override
    protected String executeAuthenticated(HttpServletRequest request, HttpServletResponse response, User loginUser)
            throws ServletException, IOException {

        String method = request.getMethod();

        if ("GET".equalsIgnoreCase(method)) {
            return showEditForm(request, response, loginUser);
        }
        if ("POST".equalsIgnoreCase(method)) {
            return processUpdate(request, response, loginUser);
        }

        response.setStatus(405);
        return null;
    }

    private String showEditForm(HttpServletRequest request, HttpServletResponse response, User loginUser)
            throws IOException {

        String idParam = request.getParameter("id");
        if (idParam == null || idParam.trim().isEmpty()) return "redirect:/app/task/list";

        int taskId;
        try {
            taskId = Integer.parseInt(idParam);
        } catch (NumberFormatException e) {
            return "redirect:/app/task/list";
        }

        Task task = taskRepository.findById(taskId);
        if (task == null || !taskRepository.isOwner(taskId, loginUser.getId())) {
            response.sendError(403, "アクセス権限がありません");
            return null;
        }

        request.setAttribute("task", task);
        return "/WEB-INF/views/task/edit.jsp";
    }

    private String processUpdate(HttpServletRequest request, HttpServletResponse response, User loginUser)
            throws IOException {

        String idParam = request.getParameter("id");
        if (idParam == null || idParam.trim().isEmpty()) return "redirect:/app/task/list";

        int taskId;
        try {
            taskId = Integer.parseInt(idParam);
        } catch (NumberFormatException e) {
            return "redirect:/app/task/list";
        }

        if (!taskRepository.isOwner(taskId, loginUser.getId())) {
            response.sendError(403, "アクセス権限がありません");
            return null;
        }

        String title = request.getParameter("title");
        String description = request.getParameter("description");
        String category = request.getParameter("category");
        String status = request.getParameter("status");

        String priority = "medium";

        if (category == null || category.trim().isEmpty()) category = "work";
        if (status == null || status.trim().isEmpty()) status = "pending";

        Map<String, List<String>> fieldErrors = validateTaskInputEnhanced(title, description, category, status, priority);

        if (!fieldErrors.isEmpty()) {
            request.setAttribute("errors", fieldErrors);

            Task task = taskRepository.findById(taskId);
            if (task == null) return "redirect:/app/task/list";

            task.setTitle(title);
            task.setDescription(description);
            task.setCategory(category);
            task.setStatus(status);

            request.setAttribute("task", task);
            return "/WEB-INF/views/task/edit.jsp";
        }

        Task task = new Task();
        task.setId(taskId);
        task.setUserId(loginUser.getId());
        task.setTitle(title != null ? title.trim() : null);
        task.setDescription(description);
        task.setCategory(category);
        task.setStatus(status);
        task.setPriority(priority);

        boolean updated = taskRepository.update(task);

        HttpSession session = request.getSession();
        if (updated) {
            session.setAttribute("message", "タスクを更新しました");
        } else {
            session.setAttribute("error", "タスクの更新に失敗しました");
        }

        return "redirect:/app/task/list";
    }

    private Map<String, List<String>> validateTaskInputEnhanced(
            String title, String description, String category, String status, String priority) {

        Map<String, List<String>> fieldErrors = new HashMap<>();

        List<String> titleErrors = ValidationUtil.validateTitle(title);
        if (!titleErrors.isEmpty()) fieldErrors.put("title", titleErrors);

        List<String> descErrors = ValidationUtil.validateDescription(description);
        if (!descErrors.isEmpty()) fieldErrors.put("description", descErrors);

        List<String> categoryErrors = ValidationUtil.validateCategory(category);
        if (!categoryErrors.isEmpty()) fieldErrors.put("category", categoryErrors);

        List<String> statusErrors = ValidationUtil.validateStatus(status);
        if (!statusErrors.isEmpty()) fieldErrors.put("status", statusErrors);

        List<String> priorityErrors = ValidationUtil.validatePriority(priority);
        if (!priorityErrors.isEmpty()) fieldErrors.put("priority", priorityErrors);

        return fieldErrors;
    }
}
