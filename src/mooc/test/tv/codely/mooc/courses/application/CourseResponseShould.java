package tv.codely.mooc.courses.application;

import org.junit.jupiter.api.Test;
import tv.codely.mooc.courses.domain.Course;
import tv.codely.mooc.courses.domain.CourseMother;

import static org.junit.jupiter.api.Assertions.assertEquals;

final class CourseResponseShould {

    @Test
    void create_course_response() {
        CourseResponse response =
            new CourseResponse(
                "1",
                "Java",
                "10 hours"
            );

        assertEquals("1", response.id());
        assertEquals("Java", response.name());
        assertEquals(
            "10 hours",
            response.duration()
        );
    }

    @Test
    void create_response_from_course() {
        Course course =
            CourseMother.random();

        CourseResponse response =
            CourseResponse.fromAggregate(course);

        assertEquals(
            course.id().value(),
            response.id()
        );

        assertEquals(
            course.name().value(),
            response.name()
        );

        assertEquals(
            course.duration().value(),
            response.duration()
        );
    }
}