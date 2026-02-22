package util;

import java.util.ArrayList;
import java.util.List;

public class ValidationUtil {

    public static List<String> validateTitle(String title) {
        List<String> errors = new ArrayList<>();

        if (title == null || title.trim().isEmpty()) {
            errors.add("タイトルは必須です");
            return errors;
        }

        if (title.length() > 200) {
            errors.add("タイトルは200文字以内で入力してください");
        }
        if (title.trim().length() < 2) {
            errors.add("タイトルは2文字以上で入力してください");
        }

        return errors;
    }

    public static List<String> validateDescription(String description) {
        List<String> errors = new ArrayList<>();

        if (description != null && description.length() > 1000) {
            errors.add("説明は1000文字以内で入力してください");
        }

        return errors;
    }

    public static List<String> validateStatus(String status) {
        List<String> errors = new ArrayList<>();

        if (status != null && !status.trim().isEmpty()) {
            if (!status.matches("pending|doing|done")) {
                errors.add("無効なステータスです");
            }
        }

        return errors;
    }

    public static List<String> validateCategory(String category) {
        List<String> errors = new ArrayList<>();

        if (category != null && !category.trim().isEmpty()) {
            if (!category.matches("work|private|study")) {
                errors.add("無効なカテゴリです");
            }
        }

        return errors;
    }

    public static List<String> validatePriority(String priority) {
        List<String> errors = new ArrayList<>();

        if (priority != null && !priority.trim().isEmpty()) {
            if (!priority.matches("low|medium|high")) {
                errors.add("無効な優先度です");
            }
        }

        return errors;
    }
}
