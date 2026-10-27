package tv.codely.mooc.enrollments.domain;

import tv.codely.mooc.courses.domain.CourseId;

public final class Enrollment {
    private final String   id;
    private final String   studentId;
    private final CourseId courseId;

    public Enrollment(String id, String studentId, CourseId courseId) {
        this.id        = requireNotBlank(id, "id");
        this.studentId = requireNotBlank(studentId, "studentId");
        this.courseId  = requireNotNull(courseId, "courseId");
    }

    private static String requireNotBlank(String value, String field) {
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException(field + " cannot be empty.");
        }
        return value;
    }

    private static <T> T requireNotNull(T value, String field) {
        if (value == null) {
            throw new IllegalArgumentException(field + " cannot be null.");
        }
        return value;
    }

    public String id() {
        return id;
    }

    public String studentId() {
        return studentId;
    }

    public CourseId courseId() {
        return courseId;
    }

    public boolean matches(String studentId, CourseId courseId) {
        return this.studentId.equals(studentId) && this.courseId.equals(courseId);
    }
}