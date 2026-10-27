package tv.codely.mooc.enrollments.domain;

public interface StudentLookup {
    boolean exists(String studentId);
}