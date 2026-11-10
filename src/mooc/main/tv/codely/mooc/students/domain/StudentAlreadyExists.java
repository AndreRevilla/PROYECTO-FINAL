package tv.codely.mooc.students.domain;

public final class StudentAlreadyExists extends RuntimeException {

    public StudentAlreadyExists(String email) {
        super(String.format("The student with email <%s> already exists", email));
    }
}

