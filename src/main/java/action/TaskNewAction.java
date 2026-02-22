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

public class TaskNewAction extends BaseAuthAction {

    private TaskRepository taskRepository = new TaskRepository();

    @Override
    protected String executeAuthenticated(HttpServletRequest request, HttpServletResponse response, User loginUser)
            throws ServletException, IOException {

        String method = request.getMethod();

        if ("GET".equalsIgnoreCase(method)) {
            return "/WEB-INF/views/task/new.jsp";
        }
        if ("POST".equalsIgnoreCase(method)) {
            return processNewTask(request, loginUser);
        }

        response.setStatus(405);
        return null;
    }

    private String processNewTask(HttpServletRequest request, User loginUser) {

        String title = request.getParameter("title");
        String description = request.getParameter("description");
        String category = request.getParameter("category");
        String status = request.getParameter("status");

        // 画面からは使わないがDBカラムがある想定で固定
        String priority = "medium";

        // デフォルト補完（バリデーション前でもOKだが、後でもOK）
        if (category == null || category.trim().isEmpty()) category = "work";
        if (status == null || status.trim().isEmpty()) status = "pending";

        Map<String, List<String>> fieldErrors = validateTaskInputEnhanced(title, description, category, status, priority);

        if (!fieldErrors.isEmpty()) {
            request.setAttribute("errors", fieldErrors);
            request.setAttribute("title", title);
            request.setAttribute("description", description);
            request.setAttribute("category", category);
            request.setAttribute("status", status);
            return "/WEB-INF/views/task/new.jsp";
        }

        Task task = new Task();
        task.setUserId(loginUser.getId());
        task.setTitle(title != null ? title.trim() : null);
        task.setDescription(description);
        task.setCategory(category);
        task.setStatus(status);
        task.setPriority(priority);

        boolean saved = taskRepository.save(task);

        if (saved) {
            HttpSession session = request.getSession();
            session.setAttribute("message", "タスクを作成しました");
            return "redirect:/app/task/list";
        }

        request.setAttribute("error", "タスクの作成に失敗しました");
        request.setAttribute("title", title);
        request.setAttribute("description", description);
        request.setAttribute("category", category);
        request.setAttribute("status", status);
        return "/WEB-INF/views/task/new.jsp";
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
