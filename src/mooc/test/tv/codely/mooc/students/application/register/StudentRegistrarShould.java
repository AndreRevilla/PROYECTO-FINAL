package tv.codely.mooc.students.application.register;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tv.codely.mooc.students.domain.Student;
import tv.codely.mooc.students.domain.StudentAlreadyExists;
import tv.codely.mooc.students.domain.StudentRepository;
import tv.codely.shared.domain.UuidGenerator;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

final class StudentRegistrarShould {

    private StudentRepository repository;
    private UuidGenerator generator;
    private StudentRegistrar registrar;

    @BeforeEach
    void setUp() {
        repository = mock(StudentRepository.class);
        generator = mock(UuidGenerator.class);
        registrar = new StudentRegistrar(repository, generator);

	when(generator.generate()).thenReturn("550e8400-e29b-41d4-a716-446655440000");
    }

    @Test
    void register_student_when_email_does_not_exist() {
        when(repository.searchByEmail("student@mail.com"))
            .thenReturn(Optional.empty());

        registrar.register("Jean", "Mollapaza", "student@mail.com");

        verify(repository).save(any(Student.class));
    }

    @Test
    void fail_when_email_already_exists() {
        Student existing = new Student(
            null,
            "Jean",
            "Mollapaza",
            "student@mail.com"
        );

        when(repository.searchByEmail("student@mail.com"))
            .thenReturn(Optional.of(existing));

        assertThrows(
            StudentAlreadyExists.class,
            () -> registrar.register("Another", "Student", "student@mail.com")
        );

        verify(repository, never()).save(any(Student.class));
    }

    @Test
    void fail_when_name_is_empty() {
        when(repository.searchByEmail("student@mail.com"))
            .thenReturn(Optional.empty());

        assertThrows(
            IllegalArgumentException.class,
            () -> registrar.register("", "Mollapaza", "student@mail.com")
        );

        verify(repository, never()).save(any(Student.class));
    }

    @Test
    void fail_when_email_is_invalid() {
        when(repository.searchByEmail("invalid-email"))
            .thenReturn(Optional.empty());

        assertThrows(
            IllegalArgumentException.class,
            () -> registrar.register("Jean", "Mollapaza", "invalid-email")
        );

        verify(repository, never()).save(any(Student.class));
    }
}
