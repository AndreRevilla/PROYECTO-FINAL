package tv.codely.mooc.courses.application;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

final class CoursesResponseShould {

    @Test
    void return_courses() {
        CourseResponse first =
            new CourseResponse(
                "1",
                "Java",
                "10 hours"
            );

        CourseResponse second =
            new CourseResponse(
                "2",
                "Python",
                "8 hours"
            );

        List<CourseResponse> courses =
            List.of(first, second);

        CoursesResponse response =
            new CoursesResponse(courses);

        assertEquals(
            courses,
            response.courses()
        );
    }
}