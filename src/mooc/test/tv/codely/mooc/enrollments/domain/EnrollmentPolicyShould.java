package tv.codely.mooc.enrollments.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tv.codely.mooc.courses.domain.CourseId;
import tv.codely.mooc.courses.domain.CourseIdMother;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

final class EnrollmentPolicyShould {

    private static final String STUDENT_ID = "s1";

    private EnrollmentPolicy policy;

    @BeforeEach
    void setUp() {
        policy = new EnrollmentPolicy();
    }

    @Test
    void allow_first_enrollment() {
        assertDoesNotThrow(
            () -> policy.ensureNotEnrolled(List.of(), STUDENT_ID, CourseIdMother.random())
        );
    }

    @Test
    void reject_duplicate_enrollment() {
        CourseId courseId = CourseIdMother.random();
        List<Enrollment> existing = List.of(enrollmentOf(STUDENT_ID, courseId));

        assertThrows(
            AlreadyEnrolled.class,
            () -> policy.ensureNotEnrolled(existing, STUDENT_ID, courseId)
        );
    }

    @Test
    void allow_same_student_in_another_course() {
        List<Enrollment> existing = List.of(enrollmentOf(STUDENT_ID, CourseIdMother.random()));

        assertDoesNotThrow(
            () -> policy.ensureNotEnrolled(existing, STUDENT_ID, CourseIdMother.random())
        );
    }

    private Enrollment enrollmentOf(String studentId, CourseId courseId) {
        return new Enrollment("e1", studentId, courseId);
    }
}