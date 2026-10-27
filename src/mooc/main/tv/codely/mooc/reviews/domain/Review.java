package tv.codely.mooc.reviews.domain;

import tv.codely.mooc.courses.domain.CourseId;

public final class Review {

    public static final int MAX_COMMENT_LENGTH = 100;

    private final String id;
    private final String studentId;
    private final CourseId courseId;
    private final int rating;
    private final String comment;

    public Review(
        String id,
        String studentId,
        CourseId courseId,
        int rating,
        String comment
    ) {
        if (id == null || id.isBlank()) {
            throw new IllegalArgumentException("id cannot be empty.");
        }
        if (studentId == null || studentId.isBlank()) {
            throw new IllegalArgumentException("studentId cannot be empty.");
        }
        if (courseId == null) {
            throw new IllegalArgumentException("courseId cannot be null.");
        }
        if (rating < 1 || rating > 5) {
            throw new IllegalArgumentException("Rating must be between 1 and 5.");
        }
        if (comment == null || comment.isBlank()) {
            throw new IllegalArgumentException("Comment cannot be empty.");
        }
        if (comment.length() > MAX_COMMENT_LENGTH) {
            throw new IllegalArgumentException("Comment exceeds the maximum length.");
        }

        this.id = id;
        this.studentId = studentId;
        this.courseId = courseId;
        this.rating = rating;
        this.comment = comment;
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

    public int rating() {
        return rating;
    }

    public String comment() {
        return comment;
    }
}
