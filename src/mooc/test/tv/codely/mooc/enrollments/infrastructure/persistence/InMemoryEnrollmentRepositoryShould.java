package tv.codely.mooc.enrollments.infrastructure.persistence;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tv.codely.mooc.courses.domain.CourseIdMother;
import tv.codely.mooc.enrollments.domain.Enrollment;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

final class InMemoryEnrollmentRepositoryShould {

    private static final String STUDENT_ID       = "s1";
    private static final String OTHER_STUDENT_ID = "s2";
    private static final String UNKNOWN_STUDENT  = "s9";

    private InMemoryEnrollmentRepository repository;

    @BeforeEach
    void setUp() {
        repository = new InMemoryEnrollmentRepository();
    }

    @Test
    void return_all_enrollments_of_a_student() {
        repository.save(enrollmentOf("e1", STUDENT_ID));
        repository.save(enrollmentOf("e2", STUDENT_ID));

        List<Enrollment> result = repository.searchByStudent(STUDENT_ID);

        assertEquals(2, result.size());
    }

    @Test
    void return_empty_list_when_student_has_no_enrollments() {
        List<Enrollment> result = repository.searchByStudent(UNKNOWN_STUDENT);

        assertTrue(result.isEmpty());
    }

    @Test
    void return_only_enrollments_of_that_student() {
        repository.save(enrollmentOf("e1", STUDENT_ID));
        repository.save(enrollmentOf("e2", OTHER_STUDENT_ID));

        List<Enrollment> result = repository.searchByStudent(STUDENT_ID);

        assertEquals(1, result.size());
        assertEquals(STUDENT_ID, result.get(0).studentId());
    }

    private Enrollment enrollmentOf(String enrollmentId, String studentId) {
        return new Enrollment(enrollmentId, studentId, CourseIdMother.random());
    }
}