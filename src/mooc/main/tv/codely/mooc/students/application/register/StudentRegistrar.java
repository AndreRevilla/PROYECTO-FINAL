package tv.codely.mooc.students.application.register;

import tv.codely.mooc.students.domain.Student;
import tv.codely.mooc.students.domain.StudentAlreadyExists;
import tv.codely.mooc.students.domain.StudentId;
import tv.codely.mooc.students.domain.StudentRepository;
import tv.codely.shared.domain.Service;
import tv.codely.shared.domain.UuidGenerator;

@Service
public final class StudentRegistrar {

    private final StudentRepository repository;
    private final UuidGenerator generator;

    public StudentRegistrar(StudentRepository repository, UuidGenerator generator) {
        this.repository = repository;
        this.generator = generator;
    }

    public void register(String name, String surname, String email) {
        if (repository.searchByEmail(email).isPresent()) {
            throw new StudentAlreadyExists(email);
        }

        Student student = new Student(
            new StudentId(generator.generate()),
            name,
            surname,
            email
        );

        repository.save(student);
    }
}
