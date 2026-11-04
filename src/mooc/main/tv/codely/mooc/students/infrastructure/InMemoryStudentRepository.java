package tv.codely.mooc.students.infrastructure;

import tv.codely.mooc.students.domain.Student;
import tv.codely.mooc.students.domain.StudentRepository;
import tv.codely.shared.domain.Service;
import tv.codely.shared.domain.UuidGenerator;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public final class InMemoryStudentRepository implements StudentRepository {

    private final UuidGenerator generator;
    private final List<Student> students = new ArrayList<>();

    public InMemoryStudentRepository(UuidGenerator generator) {
        this.generator = generator;
    }

    @Override
    public void save(Student student) {
        students.add(student);
    }

    @Override
    public Optional<Student> searchByEmail(String email) {
        return students.stream()
            .filter(student -> student.email().equalsIgnoreCase(email))
            .findFirst();
    }

    @Override
    public List<Student> searchAll() {
        return new ArrayList<>(students);
    }
}
