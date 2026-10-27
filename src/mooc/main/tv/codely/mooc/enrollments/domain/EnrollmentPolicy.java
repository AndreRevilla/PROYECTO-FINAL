package tv.codely.mooc.enrollments.domain;

import tv.codely.mooc.courses.domain.CourseId;

import java.util.List;

public final class EnrollmentPolicy {

    public void ensureNotEnrolled(List<Enrollment> existing, String studentId, CourseId courseId) {
        if (isAlreadyEnrolled(existing, studentId, courseId)) {
            throw new AlreadyEnrolled(studentId, courseId);
        }
    }

    private boolean isAlreadyEnrolled(List<Enrollment> existing, String studentId, CourseId courseId) {
        return existing.stream()
            .anyMatch(enrollment -> enrollment.matches(studentId, courseId));
    }
}