package tv.codely.mooc.enrollments.application.enroll;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tv.codely.mooc.courses.domain.CourseId;
import tv.codely.mooc.courses.domain.CourseIdMother;
import tv.codely.mooc.courses.domain.CourseMother;
import tv.codely.mooc.courses.domain.CourseNotExist;
import tv.codely.mooc.courses.domain.CourseRepository;
import tv.codely.mooc.enrollments.domain.AlreadyEnrolled;
import tv.codely.mooc.enrollments.domain.Enrollment;
import tv.codely.mooc.enrollments.domain.EnrollmentPolicy;
import tv.codely.mooc.enrollments.domain.EnrollmentRepository;
import tv.codely.mooc.enrollments.domain.StudentLookup;
import tv.codely.mooc.enrollments.domain.StudentNotExist;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import org.mockito.ArgumentCaptor;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

final class EnrollStudentShould {

    private static final String ENROLLMENT_ID = "e1";
    private static final String STUDENT_ID    = "s1";

    private CourseRepository     courseRepository;      // doble 1
    private StudentLookup        studentLookup;         // doble 2
    private EnrollmentRepository enrollmentRepository;  // doble 3
    private EnrollStudent        enrollStudent;

    @BeforeEach
    void setUp() {
        courseRepository     = mock(CourseRepository.class);
        studentLookup        = mock(StudentLookup.class);
        enrollmentRepository = mock(EnrollmentRepository.class);

        enrollStudent = new EnrollStudent(
            courseRepository,
            studentLookup,
            enrollmentRepository,
            new EnrollmentPolicy()
        );
    }

    @Test
    void enroll_student_when_course_and_student_exist() {
        CourseId courseId = CourseIdMother.random();
        givenCourseExists(courseId);
        givenStudentExists(STUDENT_ID);
        when(enrollmentRepository.searchByStudent(STUDENT_ID)).thenReturn(List.of());

        enrollStudent.enroll(ENROLLMENT_ID, STUDENT_ID, courseId);

        ArgumentCaptor<Enrollment> enrollmentCaptor = ArgumentCaptor.forClass(Enrollment.class);
        verify(enrollmentRepository, times(1)).save(enrollmentCaptor.capture());

        Enrollment savedEnrollment = enrollmentCaptor.getValue();
        assertEquals(ENROLLMENT_ID, savedEnrollment.id());
        assertEquals(STUDENT_ID, savedEnrollment.studentId());
        assertEquals(courseId, savedEnrollment.courseId());
    }

    @Test
    void fail_when_course_does_not_exist() {
        CourseId courseId = CourseIdMother.random();
        when(courseRepository.search(courseId)).thenReturn(Optional.empty());

        assertThrows(
            CourseNotExist.class,
            () -> enrollStudent.enroll(ENROLLMENT_ID, STUDENT_ID, courseId)
        );

        verify(enrollmentRepository, never()).save(any());
    }

    @Test
    void fail_when_student_does_not_exist() {
        CourseId courseId = CourseIdMother.random();
        givenCourseExists(courseId);
        when(studentLookup.exists(STUDENT_ID)).thenReturn(false);

        assertThrows(
            StudentNotExist.class,
            () -> enrollStudent.enroll(ENROLLMENT_ID, STUDENT_ID, courseId)
        );

        verify(enrollmentRepository, never()).save(any());
    }

    @Test
    void fail_when_student_is_already_enrolled() {
        CourseId courseId = CourseIdMother.random();
        givenCourseExists(courseId);
        givenStudentExists(STUDENT_ID);
        when(enrollmentRepository.searchByStudent(STUDENT_ID))
            .thenReturn(List.of(new Enrollment("e0", STUDENT_ID, courseId)));

        assertThrows(
            AlreadyEnrolled.class,
            () -> enrollStudent.enroll(ENROLLMENT_ID, STUDENT_ID, courseId)
        );

        verify(enrollmentRepository, never()).save(any());
    }

    private void givenCourseExists(CourseId courseId) {
        when(courseRepository.search(courseId)).thenReturn(Optional.of(CourseMother.random()));
    }

    private void givenStudentExists(String studentId) {
        when(studentLookup.exists(studentId)).thenReturn(true);
    }
}