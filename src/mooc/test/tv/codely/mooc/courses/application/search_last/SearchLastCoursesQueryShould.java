package tv.codely.mooc.courses.application.search_last;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

final class SearchLastCoursesQueryShould {

    @Test
    void return_total_courses() {
        // Arrange
        SearchLastCoursesQuery query =
            SearchLastCoursesQueryMother.create(5);

        // Act
        Integer total = query.total();

        // Assert
        assertEquals(5, total);
    }

    @Test
    void be_equal_to_itself() {
        // Arrange
        SearchLastCoursesQuery query =
            SearchLastCoursesQueryMother.create(5);

        // Assert
        assertEquals(query, query);
    }

    @Test
    void not_be_equal_to_null() {
        // Arrange
        SearchLastCoursesQuery query =
            SearchLastCoursesQueryMother.create(5);

        // Assert
        assertNotEquals(null, query);
    }

    @Test
    void not_be_equal_to_another_type() {
        // Arrange
        SearchLastCoursesQuery query =
            SearchLastCoursesQueryMother.create(5);

        // Assert
        assertNotEquals("5", query);
    }

    @Test
    void be_equal_when_total_is_equal() {
        // Arrange
        SearchLastCoursesQuery first =
            SearchLastCoursesQueryMother.create(5);

        SearchLastCoursesQuery second =
            SearchLastCoursesQueryMother.create(5);

        // Assert
        assertEquals(first, second);
    }

    @Test
    void not_be_equal_when_total_is_different() {
        // Arrange
        SearchLastCoursesQuery first =
            SearchLastCoursesQueryMother.create(5);

        SearchLastCoursesQuery second =
            SearchLastCoursesQueryMother.create(10);

        // Assert
        assertNotEquals(first, second);
    }

    @Test
    void have_same_hash_code_when_queries_are_equal() {
        // Arrange
        SearchLastCoursesQuery first =
            SearchLastCoursesQueryMother.create(5);

        SearchLastCoursesQuery second =
            SearchLastCoursesQueryMother.create(5);

        // Assert
        assertEquals(
            first.hashCode(),
            second.hashCode()
        );
    }

    @Test
    void return_false_when_compared_with_null() {
        SearchLastCoursesQuery query =
            SearchLastCoursesQueryMother.create(5);

        boolean result = query.equals(null);

        assertFalse(result);
    }

    @Test
    void return_false_when_compared_with_another_class() {
        SearchLastCoursesQuery query =
            SearchLastCoursesQueryMother.create(5);

        boolean result = query.equals("5");

        assertFalse(result);
    }
}