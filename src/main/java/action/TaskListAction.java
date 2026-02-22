package action;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import model.Task;
import model.User;
import repository.TaskRepository;

public class TaskListAction extends BaseAuthAction {

    private TaskRepository taskRepository = new TaskRepository();

    @Override
    protected String executeAuthenticated(HttpServletRequest request,
                                         HttpServletResponse response,
                                         User loginUser) throws ServletException, IOException {

        int userId = loginUser.getId();

        String keyword = request.getParameter("keyword");
        String sort = request.getParameter("sort");

        if (keyword == null || keyword.trim().isEmpty()) keyword = "";

        if (sort == null || sort.trim().isEmpty()) {
            sort = "DESC";
        }
        if (!"ASC".equals(sort) && !"DESC".equals(sort)) {
            sort = "DESC";
        }

        String pageParam = request.getParameter("page");
        int page = 1;
        if (pageParam != null && pageParam.matches("\\d+")) {
            page = Integer.parseInt(pageParam);
            if (page < 1) page = 1;
        }

        int pageSize = 10;

        try {
            String keywordOrNull = keyword.isEmpty() ? null : keyword;

            int totalRecords = taskRepository.countTasks(userId, keywordOrNull);

            int totalPages = (totalRecords + pageSize - 1) / pageSize;
            if (totalPages == 0) totalPages = 1;

            if (page > totalPages) page = 1;

            int offset = (page - 1) * pageSize;

            List<Task> tasks = taskRepository.searchWithPaging(userId, keywordOrNull, sort, pageSize, offset);

            int startRecord = (totalRecords == 0) ? 0 : offset + 1;
            int endRecord = Math.min(offset + pageSize, totalRecords);
            boolean hasPrevious = (page > 1);
            boolean hasNext = (page < totalPages);

            request.setAttribute("tasks", tasks);
            request.setAttribute("loginUser", loginUser);

            request.setAttribute("keyword", keyword);
            request.setAttribute("sort", sort);

            request.setAttribute("totalRecords", totalRecords);
            request.setAttribute("totalPages", totalPages);
            request.setAttribute("currentPage", page);
            request.setAttribute("startRecord", startRecord);
            request.setAttribute("endRecord", endRecord);
            request.setAttribute("hasPrevious", hasPrevious);
            request.setAttribute("hasNext", hasNext);

            HttpSession session = request.getSession(false);
            if (session != null) {
                Object msg = session.getAttribute("message");
                if (msg != null) {
                    request.setAttribute("message", msg);
                    session.removeAttribute("message");
                }
                Object err = session.getAttribute("error");
                if (err != null) {
                    request.setAttribute("error", err);
                    session.removeAttribute("error");
                }
            }

        } catch (SQLException e) {
            request.setAttribute("error", "データの取得に失敗しました");
        }

        return "/WEB-INF/views/task/list.jsp";
    }
}

