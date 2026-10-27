package tv.codely.mooc.enrollments.application.enroll;

import tv.codely.mooc.courses.domain.CourseId;
import tv.codely.mooc.courses.domain.CourseNotExist;
import tv.codely.mooc.courses.domain.CourseRepository;
import tv.codely.mooc.enrollments.domain.Enrollment;
import tv.codely.mooc.enrollments.domain.EnrollmentPolicy;
import tv.codely.mooc.enrollments.domain.EnrollmentRepository;
import tv.codely.mooc.enrollments.domain.StudentLookup;
import tv.codely.mooc.enrollments.domain.StudentNotExist;

public final class EnrollStudent {
    private final CourseRepository     courseRepository;
    private final StudentLookup        studentLookup;
    private final EnrollmentRepository enrollmentRepository;
    private final EnrollmentPolicy     policy;

    public EnrollStudent(
        CourseRepository courseRepository,
        StudentLookup studentLookup,
        EnrollmentRepository enrollmentRepository,
        EnrollmentPolicy policy
    ) {
        this.courseRepository     = courseRepository;
        this.studentLookup        = studentLookup;
        this.enrollmentRepository = enrollmentRepository;
        this.policy               = policy;
    }

    public void enroll(String enrollmentId, String studentId, CourseId courseId) {
        ensureCourseExists(courseId);
        ensureStudentExists(studentId);
        ensureNotAlreadyEnrolled(studentId, courseId);

        enrollmentRepository.save(new Enrollment(enrollmentId, studentId, courseId));
    }

    private void ensureCourseExists(CourseId courseId) {
        courseRepository.search(courseId)
            .orElseThrow(() -> new CourseNotExist(courseId));
    }

    private void ensureStudentExists(String studentId) {
        if (!studentLookup.exists(studentId)) {
            throw new StudentNotExist(studentId);
        }
    }

    private void ensureNotAlreadyEnrolled(String studentId, CourseId courseId) {
        policy.ensureNotEnrolled(
            enrollmentRepository.searchByStudent(studentId),
            studentId,
            courseId
        );
    }
}