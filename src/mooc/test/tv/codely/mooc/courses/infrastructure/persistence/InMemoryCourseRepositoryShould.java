package tv.codely.mooc.courses.infrastructure.persistence;

import org.junit.jupiter.api.Test;
import tv.codely.mooc.courses.CoursesModuleInfrastructureTestCase;
import tv.codely.mooc.courses.domain.Course;
import tv.codely.mooc.courses.domain.CourseIdMother;
import tv.codely.mooc.courses.domain.CourseMother;
import tv.codely.mooc.courses.domain.CourseName;
import tv.codely.mooc.courses.domain.CourseDuration;
import tv.codely.mooc.courses.domain.CourseId;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import tv.codely.shared.domain.criteria.Criteria;
import tv.codely.shared.domain.criteria.Filters;
import tv.codely.shared.domain.criteria.Order;
import tv.codely.shared.domain.criteria.Filter;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertTrue;

final class InMemoryCourseRepositoryShould extends CoursesModuleInfrastructureTestCase {
    @Test
    void save_a_course() {
        // Arrange
        Course course = CourseMother.random();

        // Act
        inMemoryCourseRepository.save(course);

        // Assert
        assertEquals(
            Optional.of(course),
            inMemoryCourseRepository.search(course.id())
        );
    }

    @Test
    void return_an_existing_course() {
        Course course = CourseMother.random();

        inMemoryCourseRepository.save(course);

        assertEquals(Optional.of(course), inMemoryCourseRepository.search(course.id()));
    }

    @Test
    void not_return_a_non_existing_course() {
        assertFalse(inMemoryCourseRepository.search(CourseIdMother.random()).isPresent());
    }

    @Test
    void return_all_courses_when_matching_without_criteria() {
        // Arrange
        Course first = CourseMother.random();
        Course second = CourseMother.random();

        inMemoryCourseRepository.save(first);
        inMemoryCourseRepository.save(second);

        Criteria criteria = new Criteria(
            Filters.none(),
            Order.none()
        );

        // Act
        List<Course> courses =
            inMemoryCourseRepository.matching(criteria);

        // Assert
        assertEquals(2, courses.size());
        assertTrue(courses.contains(first));
        assertTrue(courses.contains(second));
    }

    @Test
    void filter_courses_by_name() {
        // Arrange
        Course first = CourseMother.random();
        Course second = CourseMother.random();

        inMemoryCourseRepository.save(first);
        inMemoryCourseRepository.save(second);

        Filter filter = Filter.create(
            "name",
            "=",
            first.name().value()
        );

        Criteria criteria = new Criteria(
            new Filters(List.of(filter)),
            Order.none()
        );

        // Act
        List<Course> courses =
            inMemoryCourseRepository.matching(criteria);

        // Assert
        assertEquals(1, courses.size());
        assertEquals(first, courses.get(0));
    }

    @Test
    void filter_courses_by_name_contains() {
        // Arrange
        Course first = new Course(
            CourseIdMother.random(),
            new CourseName("Java DDD"),
            new CourseDuration("10 hours")
        );

        Course second = new Course(
            CourseIdMother.random(),
            new CourseName("Python Basics"),
            new CourseDuration("8 hours")
        );

        inMemoryCourseRepository.save(first);
        inMemoryCourseRepository.save(second);

        Filter filter = Filter.create(
            "name",
            "CONTAINS",
            "Java"
        );

        Criteria criteria = new Criteria(
            new Filters(List.of(filter)),
            Order.none()
        );

        // Act
        List<Course> courses =
            inMemoryCourseRepository.matching(criteria);

        // Assert
        assertEquals(1, courses.size());
        assertEquals(first, courses.get(0));
    }

    @Test
    void order_courses_by_name_ascending() {
        // Arrange
        Course first = new Course(
            CourseIdMother.random(),
            new CourseName("Beta"),
            new CourseDuration("10 hours")
        );

        Course second = new Course(
            CourseIdMother.random(),
            new CourseName("Alpha"),
            new CourseDuration("8 hours")
        );

        inMemoryCourseRepository.save(first);
        inMemoryCourseRepository.save(second);

        Criteria criteria = new Criteria(
            Filters.none(),
            Order.asc("name")
        );

        // Act
        List<Course> courses =
            inMemoryCourseRepository.matching(criteria);

        // Assert
        assertEquals(second, courses.get(0));
        assertEquals(first, courses.get(1));
    }

    @Test
    void order_courses_by_name_descending() {
        // Arrange
        Course first = new Course(
            CourseIdMother.random(),
            new CourseName("Beta"),
            new CourseDuration("10 hours")
        );

        Course second = new Course(
            CourseIdMother.random(),
            new CourseName("Alpha"),
            new CourseDuration("8 hours")
        );

        inMemoryCourseRepository.save(first);
        inMemoryCourseRepository.save(second);

        Criteria criteria = new Criteria(
            Filters.none(),
            Order.desc("name")
        );

        // Act
        List<Course> courses =
            inMemoryCourseRepository.matching(criteria);

        // Assert
        assertEquals(first, courses.get(0));
        assertEquals(second, courses.get(1));
    }

    @Test
    void limit_matching_results() {
        // Arrange
        Course first = CourseMother.random();
        Course second = CourseMother.random();

        inMemoryCourseRepository.save(first);
        inMemoryCourseRepository.save(second);

        Criteria criteria = new Criteria(
            Filters.none(),
            Order.none(),
            Optional.of(1),
            Optional.of(0)
        );

        // Act
        List<Course> courses =
            inMemoryCourseRepository.matching(criteria);

        // Assert
        assertEquals(1, courses.size());
    }

    @Test
    void apply_offset_to_matching_results() {
        // Arrange
        Course first = CourseMother.random();
        Course second = CourseMother.random();

        inMemoryCourseRepository.save(first);
        inMemoryCourseRepository.save(second);

        Criteria criteria = new Criteria(
            Filters.none(),
            Order.none(),
            Optional.empty(),
            Optional.of(1)
        );

        // Act
        List<Course> courses =
            inMemoryCourseRepository.matching(criteria);

        // Assert
        assertEquals(1, courses.size());
    }

    @Test
    void return_empty_list_when_offset_is_out_of_range() {
        // Arrange
        Course course = CourseMother.random();

        inMemoryCourseRepository.save(course);

        Criteria criteria = new Criteria(
            Filters.none(),
            Order.none(),
            Optional.empty(),
            Optional.of(10)
        );

        // Act
        List<Course> courses =
            inMemoryCourseRepository.matching(criteria);

        // Assert
        assertTrue(courses.isEmpty());
    }

    @Test
    void filter_courses_by_name_not_equal() {
        Course first = new Course(
            CourseIdMother.random(),
            new CourseName("Java"),
            new CourseDuration("10")
        );

        Course second = new Course(
            CourseIdMother.random(),
            new CourseName("Python"),
            new CourseDuration("8")
        );

        inMemoryCourseRepository.save(first);
        inMemoryCourseRepository.save(second);

        Filter filter = Filter.create(
            "name",
            "!=",
            "Java"
        );

        Criteria criteria = new Criteria(
            new Filters(List.of(filter)),
            Order.none()
        );

        List<Course> courses =
            inMemoryCourseRepository.matching(criteria);

        assertEquals(1, courses.size());
        assertEquals(second, courses.get(0));
    }

    @Test
    void filter_courses_by_name_not_contains() {
        Course first = new Course(
            CourseIdMother.random(),
            new CourseName("Java DDD"),
            new CourseDuration("10")
        );

        Course second = new Course(
            CourseIdMother.random(),
            new CourseName("Python Basics"),
            new CourseDuration("8")
        );

        inMemoryCourseRepository.save(first);
        inMemoryCourseRepository.save(second);

        Filter filter = Filter.create(
            "name",
            "NOT_CONTAINS",
            "Java"
        );

        Criteria criteria = new Criteria(
            new Filters(List.of(filter)),
            Order.none()
        );

        List<Course> courses =
            inMemoryCourseRepository.matching(criteria);

        assertEquals(1, courses.size());
        assertEquals(second, courses.get(0));
    }

    @Test
    void filter_courses_greater_than_name() {
        Course first = new Course(
            CourseIdMother.random(),
            new CourseName("Alpha"),
            new CourseDuration("10")
        );

        Course second = new Course(
            CourseIdMother.random(),
            new CourseName("Beta"),
            new CourseDuration("8")
        );

        inMemoryCourseRepository.save(first);
        inMemoryCourseRepository.save(second);

        Filter filter = Filter.create(
            "name",
            ">",
            "Alpha"
        );

        Criteria criteria = new Criteria(
            new Filters(List.of(filter)),
            Order.none()
        );

        List<Course> courses =
            inMemoryCourseRepository.matching(criteria);

        assertEquals(1, courses.size());
        assertEquals(second, courses.get(0));
    }

    @Test
    void filter_courses_less_than_name() {
        Course first = new Course(
            CourseIdMother.random(),
            new CourseName("Alpha"),
            new CourseDuration("10")
        );

        Course second = new Course(
            CourseIdMother.random(),
            new CourseName("Beta"),
            new CourseDuration("8")
        );

        inMemoryCourseRepository.save(first);
        inMemoryCourseRepository.save(second);

        Filter filter = Filter.create(
            "name",
            "<",
            "Beta"
        );

        Criteria criteria = new Criteria(
            new Filters(List.of(filter)),
            Order.none()
        );

        List<Course> courses =
            inMemoryCourseRepository.matching(criteria);

        assertEquals(1, courses.size());
        assertEquals(first, courses.get(0));
    }

    @Test
    void filter_courses_by_id() {
        Course course = CourseMother.random();

        inMemoryCourseRepository.save(course);

        Filter filter = Filter.create(
            "id",
            "=",
            course.id().value()
        );

        Criteria criteria = new Criteria(
            new Filters(List.of(filter)),
            Order.none()
        );

        List<Course> courses =
            inMemoryCourseRepository.matching(criteria);

        assertEquals(1, courses.size());
        assertEquals(course, courses.get(0));
    }

    @Test
    void filter_courses_by_duration() {
        Course course = new Course(
            CourseIdMother.random(),
            new CourseName("Java"),
            new CourseDuration("10 hours")
        );

        inMemoryCourseRepository.save(course);

        Filter filter = Filter.create(
            "duration",
            "=",
            "10 hours"
        );

        Criteria criteria = new Criteria(
            new Filters(List.of(filter)),
            Order.none()
        );

        List<Course> courses =
            inMemoryCourseRepository.matching(criteria);

        assertEquals(1, courses.size());
        assertEquals(course, courses.get(0));
    }

    @Test
    void return_no_courses_for_unknown_filter_field() {
        Course course = CourseMother.random();

        inMemoryCourseRepository.save(course);

        Filter filter = Filter.create(
            "unknown",
            "=",
            "value"
        );

        Criteria criteria = new Criteria(
            new Filters(List.of(filter)),
            Order.none()
        );

        List<Course> courses =
            inMemoryCourseRepository.matching(criteria);

        assertTrue(courses.isEmpty());
    }

    @Test
    void ignore_order_when_field_is_invalid() {
        Course first = CourseMother.random();
        Course second = CourseMother.random();

        inMemoryCourseRepository.save(first);
        inMemoryCourseRepository.save(second);

        Criteria criteria = new Criteria(
            Filters.none(),
            Order.asc("unknown")
        );

        List<Course> courses =
            inMemoryCourseRepository.matching(criteria);

        assertEquals(2, courses.size());
    }

    @Test
    void order_courses_by_duration_ascending() {
        // Arrange
        Course first = new Course(
            CourseIdMother.random(),
            new CourseName("Java"),
            new CourseDuration("20")
        );

        Course second = new Course(
            CourseIdMother.random(),
            new CourseName("Python"),
            new CourseDuration("10")
        );

        inMemoryCourseRepository.save(first);
        inMemoryCourseRepository.save(second);

        Criteria criteria = new Criteria(
            Filters.none(),
            Order.asc("duration")
        );

        // Act
        List<Course> courses =
            inMemoryCourseRepository.matching(criteria);

        // Assert
        assertEquals(second, courses.get(0));
        assertEquals(first, courses.get(1));
    }

    @Test
    void order_courses_by_id_ascending() {
        // Arrange
        Course first = new Course(
            new CourseId(
                "22222222-2222-2222-2222-222222222222"
            ),
            new CourseName("Java"),
            new CourseDuration("10")
        );

        Course second = new Course(
            new CourseId(
                "11111111-1111-1111-1111-111111111111"
            ),
            new CourseName("Python"),
            new CourseDuration("20")
        );

        inMemoryCourseRepository.save(first);
        inMemoryCourseRepository.save(second);

        Criteria criteria = new Criteria(
            Filters.none(),
            Order.asc("id")
        );

        // Act
        List<Course> courses =
            inMemoryCourseRepository.matching(criteria);

        // Assert
        assertEquals(second, courses.get(0));
        assertEquals(first, courses.get(1));
    }

    @Test
    void keep_courses_when_already_ordered_ascending() {
        // Arrange
        Course first = new Course(
            CourseIdMother.random(),
            new CourseName("Alpha"),
            new CourseDuration("10")
        );

        Course second = new Course(
            CourseIdMother.random(),
            new CourseName("Beta"),
            new CourseDuration("20")
        );

        inMemoryCourseRepository.save(first);
        inMemoryCourseRepository.save(second);

        Criteria criteria = new Criteria(
            Filters.none(),
            Order.asc("name")
        );

        // Act
        List<Course> courses =
            inMemoryCourseRepository.matching(criteria);

        // Assert
        assertEquals(first, courses.get(0));
        assertEquals(second, courses.get(1));
    }

    @Test
    void keep_courses_when_already_ordered_descending() {
        // Arrange
        Course first = new Course(
            CourseIdMother.random(),
            new CourseName("Beta"),
            new CourseDuration("20")
        );

        Course second = new Course(
            CourseIdMother.random(),
            new CourseName("Alpha"),
            new CourseDuration("10")
        );

        inMemoryCourseRepository.save(first);
        inMemoryCourseRepository.save(second);

        Criteria criteria = new Criteria(
            Filters.none(),
            Order.desc("name")
        );

        // Act
        List<Course> courses =
            inMemoryCourseRepository.matching(criteria);

        // Assert
        assertEquals(first, courses.get(0));
        assertEquals(second, courses.get(1));
    }
}
