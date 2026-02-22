package repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import model.Task;

public class TaskRepository extends BaseRepository {

    public Task findById(int id) {
        String sql = "SELECT id, user_id, title, description, status, priority, category, is_favorite, created_at, updated_at "
                   + "FROM tasks WHERE id = ?";

        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapToTask(rs);
                }
            }
        } catch (SQLException e) {
            handleSQLException(e, "TaskRepository.findById");
        }
        return null;
    }

    public List<Task> findAll() {
        List<Task> tasks = new ArrayList<>();
        String sql = "SELECT id, user_id, title, description, status, priority, category, is_favorite, created_at, updated_at "
                   + "FROM tasks ORDER BY is_favorite DESC, created_at DESC";

        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                tasks.add(mapToTask(rs));
            }
        } catch (SQLException e) {
            handleSQLException(e, "TaskRepository.findAll");
        }
        return tasks;
    }

    public List<Task> findByUserId(int userId) {
        List<Task> tasks = new ArrayList<>();
        String sql = "SELECT id, user_id, title, description, status, priority, category, is_favorite, created_at, updated_at "
                   + "FROM tasks WHERE user_id = ? ORDER BY is_favorite DESC, created_at DESC";

        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, userId);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    tasks.add(mapToTask(rs));
                }
            }
        } catch (SQLException e) {
            handleSQLException(e, "TaskRepository.findByUserId");
        }
        return tasks;
    }

    public boolean isOwner(int taskId, int userId) {
        String sql = "SELECT COUNT(*) FROM tasks WHERE id = ? AND user_id = ?";

        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, taskId);
            ps.setInt(2, userId);

            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() && rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            handleSQLException(e, "TaskRepository.isOwner");
            return false;
        }
    }

    public boolean update(Task task) {
        String sql = "UPDATE tasks SET title=?, description=?, category=?, status=?, priority=?, "
                   + "updated_at=CURRENT_TIMESTAMP "
                   + "WHERE id=? AND user_id=?";

        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, task.getTitle());
            ps.setString(2, task.getDescription());
            ps.setString(3, task.getCategory());
            ps.setString(4, task.getStatus());
            ps.setString(5, task.getPriority());
            ps.setInt(6, task.getId());
            ps.setInt(7, task.getUserId());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            handleSQLException(e, "TaskRepository.update");
            return false;
        }
    }

    public boolean save(Task task) {
        String sql = "INSERT INTO tasks (user_id, title, description, category, status, priority) "
                   + "VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setInt(1, task.getUserId());
            ps.setString(2, task.getTitle());
            ps.setString(3, task.getDescription());
            ps.setString(4, task.getCategory());
            ps.setString(5, task.getStatus());
            ps.setString(6, task.getPriority());

            int insertedRows = ps.executeUpdate();

            if (insertedRows > 0) {
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) task.setId(rs.getInt(1));
                }
                return true;
            }

        } catch (SQLException e) {
            handleSQLException(e, "TaskRepository.save");
        }

        return false;
    }

    public boolean deleteByIdAndUserId(int taskId, int userId) {
        String sql = "DELETE FROM tasks WHERE id = ? AND user_id = ?";

        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, taskId);
            ps.setInt(2, userId);

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            handleSQLException(e, "TaskRepository.deleteByIdAndUserId");
            return false;
        }
    }

    public List<Task> search(int userId, String keyword, String sort) {
        List<Task> tasks = new ArrayList<>();

        StringBuilder sql = new StringBuilder();
        sql.append("SELECT id, user_id, title, description, status, priority, category, is_favorite, created_at, updated_at ");
        sql.append("FROM tasks WHERE user_id = ? AND title LIKE ? ");
        sql.append("ORDER BY is_favorite DESC, created_at ");
        sql.append("ASC".equals(sort) ? "ASC" : "DESC");

        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql.toString())) {

            ps.setInt(1, userId);
            ps.setString(2, "%" + keyword + "%");

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    tasks.add(mapToTask(rs));
                }
            }
        } catch (SQLException e) {
            handleSQLException(e, "TaskRepository.search");
        }

        return tasks;
    }

    public List<Task> findAllByUserIdWithSort(int userId, String sort) {
        List<Task> tasks = new ArrayList<>();

        StringBuilder sql = new StringBuilder();
        sql.append("SELECT id, user_id, title, description, status, priority, category, is_favorite, created_at, updated_at ");
        sql.append("FROM tasks WHERE user_id = ? ");
        sql.append("ORDER BY is_favorite DESC, created_at ");
        sql.append("ASC".equals(sort) ? "ASC" : "DESC");

        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql.toString())) {

            ps.setInt(1, userId);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    tasks.add(mapToTask(rs));
                }
            }
        } catch (SQLException e) {
            handleSQLException(e, "TaskRepository.findAllByUserIdWithSort");
        }

        return tasks;
    }

    public int countTasks(int userId, String keyword) throws SQLException {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT COUNT(*) FROM tasks WHERE user_id = ?");

        boolean hasKeyword = (keyword != null && !keyword.trim().isEmpty());
        if (hasKeyword) {
            sql.append(" AND title LIKE ?");
        }

        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql.toString())) {

            ps.setInt(1, userId);
            if (hasKeyword) {
                ps.setString(2, "%" + keyword + "%");
            }

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        }

        return 0;
    }

    public List<Task> searchWithPaging(int userId, String keyword, String sort, int pageSize, int offset) throws SQLException {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT id, user_id, title, description, status, priority, category, is_favorite, created_at, updated_at ");
        sql.append("FROM tasks WHERE user_id = ?");

        boolean hasKeyword = (keyword != null && !keyword.trim().isEmpty());
        if (hasKeyword) {
            sql.append(" AND title LIKE ?");
        }

        sql.append(" ORDER BY is_favorite DESC, created_at ");
        sql.append("ASC".equals(sort) ? "ASC" : "DESC");
        sql.append(" LIMIT ? OFFSET ?");

        List<Task> tasks = new ArrayList<>();

        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql.toString())) {

            int idx = 1;
            ps.setInt(idx++, userId);

            if (hasKeyword) {
                ps.setString(idx++, "%" + keyword + "%");
            }

            ps.setInt(idx++, pageSize);
            ps.setInt(idx++, offset);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    tasks.add(mapToTask(rs));
                }
            }
        }

        return tasks;
    }

    public boolean toggleFavorite(int taskId, int userId) throws SQLException {
        String sql = "UPDATE tasks SET is_favorite = NOT is_favorite WHERE id = ? AND user_id = ?";

        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, taskId);
            ps.setInt(2, userId);

            int updatedRows = ps.executeUpdate();
            if (updatedRows > 0) {
                return getFavoriteStatus(taskId, userId);
            }
        }

        return false;
    }

    private boolean getFavoriteStatus(int taskId, int userId) throws SQLException {
        String sql = "SELECT is_favorite FROM tasks WHERE id = ? AND user_id = ?";

        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, taskId);
            ps.setInt(2, userId);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getBoolean("is_favorite");
                }
            }
        }
        return false;
    }

    private Task mapToTask(ResultSet rs) throws SQLException {
        Task task = new Task();
        task.setId(rs.getInt("id"));
        task.setUserId(rs.getInt("user_id"));
        task.setTitle(rs.getString("title"));
        task.setDescription(rs.getString("description"));
        task.setStatus(rs.getString("status"));
        task.setPriority(rs.getString("priority"));
        task.setCategory(rs.getString("category"));
        task.setFavorite(rs.getBoolean("is_favorite"));
        task.setCreatedAt(rs.getTimestamp("created_at"));
        task.setUpdatedAt(rs.getTimestamp("updated_at"));
        return task;
    }
}
