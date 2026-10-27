package tv.codely.mooc.enrollments.domain;

import org.junit.jupiter.api.Test;
import tv.codely.mooc.courses.domain.CourseId;
import tv.codely.mooc.courses.domain.CourseIdMother;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

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
}