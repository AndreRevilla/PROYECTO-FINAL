package tv.codely.mooc.enrollments.infrastructure.persistence;

import tv.codely.mooc.enrollments.domain.Enrollment;
import tv.codely.mooc.enrollments.domain.EnrollmentRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public final class InMemoryEnrollmentRepository implements EnrollmentRepository {
    private final List<Enrollment> storage = new ArrayList<>();

    @Override
    public void save(Enrollment enrollment) {
        storage.add(enrollment);
    }

    @Override
    public List<Enrollment> searchByStudent(String studentId) {
        return storage.stream()
            .filter(enrollment -> belongsTo(enrollment, studentId))
            .collect(Collectors.toList());
    }

    private boolean belongsTo(Enrollment enrollment, String studentId) {
        return enrollment.studentId().equals(studentId);
    }
}