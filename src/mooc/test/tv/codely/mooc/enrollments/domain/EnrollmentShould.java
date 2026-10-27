package tv.codely.mooc.enrollments.domain;

import org.junit.jupiter.api.Test;
import tv.codely.mooc.courses.domain.CourseId;
import tv.codely.mooc.courses.domain.CourseIdMother;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

final class EnrollmentShould {

    @Test
    void be_created_with_valid_data() {
        // Arrange
        CourseId courseId = CourseIdMother.random();

        // Act
        Enrollment enrollment = new Enrollment("e1", "s1", courseId);

        // Assert
        assertEquals("s1", enrollment.studentId());
        assertEquals(courseId, enrollment.courseId());
    }

    @Test
    void expose_its_id() {
        Enrollment enrollment = new Enrollment("e1", "s1", CourseIdMother.random());

        assertEquals("e1", enrollment.id());
    }

    @Test
    void fail_when_id_is_null() {
        assertThrows(
            IllegalArgumentException.class,
            () -> new Enrollment(null, "s1", CourseIdMother.random())
        );
    }

    @Test
    void fail_when_student_id_is_blank() {
        assertThrows(
            IllegalArgumentException.class,
            () -> new Enrollment("e1", "  ", CourseIdMother.random())
        );
    }

    @Test
    void fail_when_course_id_is_null() {
        assertThrows(
            IllegalArgumentException.class,
            () -> new Enrollment("e1", "s1", null)
        );
    }

    @Test
    void match_same_student_and_same_course() {
        CourseId courseId = CourseIdMother.random();
        Enrollment enrollment = new Enrollment("e1", "s1", courseId);

        assertTrue(enrollment.matches("s1", courseId));
    }

    @Test
    void not_match_a_different_student() {
        CourseId courseId = CourseIdMother.random();
        Enrollment enrollment = new Enrollment("e1", "s1", courseId);

        assertFalse(enrollment.matches("s2", courseId));
    }

    @Test
    void not_match_a_different_course() {
        Enrollment enrollment = new Enrollment("e1", "s1", CourseIdMother.random());

        assertFalse(enrollment.matches("s1", CourseIdMother.random()));
    }
}