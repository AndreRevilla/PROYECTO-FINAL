package tv.codely.mooc.courses.application.find;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tv.codely.mooc.courses.CoursesModuleUnitTestCase;
import tv.codely.mooc.courses.application.CourseResponse;
import tv.codely.mooc.courses.domain.Course;
import tv.codely.mooc.courses.domain.CourseId;
import tv.codely.mooc.courses.domain.CourseMother;
import tv.codely.mooc.courses.domain.CourseNotExist;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

final class FindCourseQueryHandlerShould
    extends CoursesModuleUnitTestCase {

    private FindCourseQueryHandler handler;

    @BeforeEach
    protected void setUp() {
        super.setUp();

        CourseFinder finder =
            new CourseFinder(repository);

        handler =
            new FindCourseQueryHandler(finder);
    }

    @Test
    void find_an_existing_course() {
        // Arrange
        Course course =
            CourseMother.random();

        FindCourseQuery query =
            new FindCourseQuery(
                course.id().value()
            );

        when(
            repository.search(course.id())
        ).thenReturn(
            Optional.of(course)
        );

        // Act
        CourseResponse response =
            handler.handle(query);

        // Assert
        assertNotNull(response);
    }

    @Test
    void throw_error_when_course_does_not_exist() {
        // Arrange
        String id =
            "11111111-1111-1111-1111-111111111111";

        FindCourseQuery query =
            new FindCourseQuery(id);

        CourseId courseId =
            new CourseId(id);

        when(
            repository.search(courseId)
        ).thenReturn(
            Optional.empty()
        );

        // Act + Assert
        assertThrows(
            CourseNotExist.class,
            () -> handler.handle(query)
        );
    }
}