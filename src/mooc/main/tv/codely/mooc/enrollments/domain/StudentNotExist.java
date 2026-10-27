package tv.codely.mooc.enrollments.domain;

public final class StudentNotExist extends RuntimeException {
    public StudentNotExist(String studentId) {
        super(String.format("The student <%s> doesn't exist", studentId));
    }
}