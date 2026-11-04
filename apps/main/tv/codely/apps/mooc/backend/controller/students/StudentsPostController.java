
package tv.codely.apps.mooc.backend.controller.students;

import java.util.HashMap;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import tv.codely.mooc.students.application.register.StudentRegistrar;
import tv.codely.mooc.students.domain.StudentAlreadyExists;
import tv.codely.shared.domain.DomainError;
import tv.codely.shared.domain.bus.command.CommandBus;
import tv.codely.shared.domain.bus.query.QueryBus;
import tv.codely.shared.infrastructure.spring.ApiController;

@RestController
public final class StudentsPostController extends ApiController {

    private final StudentRegistrar registrar;

    public StudentsPostController(
        QueryBus queryBus,
        CommandBus commandBus,
        StudentRegistrar registrar
    ) {
        super(queryBus, commandBus);
        this.registrar = registrar;
    }

    @PostMapping("/students")
    public ResponseEntity<String> index(@RequestBody Request request) {
        try {
            registrar.register(
                request.name(),
                request.surname(),
                request.email()
            );

            return new ResponseEntity<>("Student registered successfully", HttpStatus.CREATED);

        } catch (StudentAlreadyExists exception) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(exception.getMessage());

        } catch (IllegalArgumentException exception) {
            return ResponseEntity.badRequest()
                .body(exception.getMessage());
        }
    }

    @Override
    public HashMap<Class<? extends DomainError>, HttpStatus> errorMapping() {
        return new HashMap<>();
    }
}

final class Request {

    private String name;
    private String surname;
    private String email;

    public void setName(String name) {
        this.name = name;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    String name() {
        return name;
    }

    String surname() {
        return surname;
    }

    String email() {
        return email;
    }
}

