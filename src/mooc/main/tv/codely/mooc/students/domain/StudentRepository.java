package tv.codely.mooc.students.domain;

import java.util.List;
import java.util.Optional;

public interface StudentRepository {

    void save(Student student);

    Optional<Student> searchByEmail(String email);

    List<Student> searchAll();
}
