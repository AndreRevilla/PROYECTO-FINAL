package tv.codely.mooc.enrollments.domain;

import java.util.List;

public interface EnrollmentRepository {
    void save(Enrollment enrollment);

    List<Enrollment> searchByStudent(String studentId);
}