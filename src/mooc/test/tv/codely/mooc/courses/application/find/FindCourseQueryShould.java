package tv.codely.mooc.courses.application.find;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

final class FindCourseQueryShould {

    @Test
    void return_course_id() {
        // Arrange
        String id =
            "11111111-1111-1111-1111-111111111111";

        FindCourseQuery query =
            new FindCourseQuery(id);

        // Act
        String result = query.id();

        // Assert
        assertEquals(id, result);
    }
}