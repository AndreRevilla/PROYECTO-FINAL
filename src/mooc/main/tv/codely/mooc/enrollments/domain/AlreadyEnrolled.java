package tv.codely.mooc.enrollments.domain;

import tv.codely.mooc.courses.domain.CourseId;

public final class AlreadyEnrolled extends RuntimeException {
    public AlreadyEnrolled(String studentId, CourseId courseId) {
        super(String.format(
            "The student <%s> is already enrolled in the course <%s>",
            studentId,
            courseId.value()
        ));
    }
}