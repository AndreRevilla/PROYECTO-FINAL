package tv.codely.mooc.courses.domain;

import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;

import static org.junit.jupiter.api.Assertions.*;

final class CourseShould {

    @Test
    void not_allow_an_empty_course_name() {
        // Arrange
        String emptyName = "";

        // Act + Assert
        assertThrows(
            IllegalArgumentException.class,
            () -> new CourseName(emptyName)
        );
    }

    @Test
    void create_a_valid_course() {
        // Arrange
        CourseId id = CourseIdMother.random();
        CourseName name = CourseNameMother.random();
        CourseDuration duration =
            CourseDurationMother.random();

        // Act
        Course course =
            Course.create(id, name, duration);

        // Assert
        assertEquals(id, course.id());
        assertEquals(name, course.name());
        assertEquals(duration, course.duration());
    }

    @Test
    void be_equal_to_itself() {
        // Arrange
        Course course = CourseMother.random();

        // Act + Assert
        assertEquals(course, course);
    }

    @Test
    void not_be_equal_to_null() {
        // Arrange
        Course course = CourseMother.random();

        // Act + Assert
        assertNotEquals(course, null);
    }

    @Test
    void not_be_equal_to_another_type() {
        // Arrange
        Course course = CourseMother.random();

        // Act + Assert
        assertNotEquals(course, "not a course");
    }

    @Test
    void be_equal_when_all_values_are_equal() {
        // Arrange
        CourseId id = CourseIdMother.random();
        CourseName name = CourseNameMother.random();
        CourseDuration duration =
            CourseDurationMother.random();

        Course first =
            new Course(id, name, duration);

        Course second =
            new Course(id, name, duration);

        // Act + Assert
        assertEquals(first, second);
    }

    @Test
    void not_be_equal_when_id_is_different() {
        // Arrange
        CourseName name = CourseNameMother.random();
        CourseDuration duration =
            CourseDurationMother.random();

        Course first = new Course(
            CourseIdMother.random(),
            name,
            duration
        );

        Course second = new Course(
            CourseIdMother.random(),
            name,
            duration
        );

        // Act + Assert
        assertNotEquals(first, second);
    }

    @Test
    void not_be_equal_when_name_is_different() {
        // Arrange
        CourseId id = CourseIdMother.random();
        CourseDuration duration =
            CourseDurationMother.random();

        Course first = new Course(
            id,
            CourseNameMother.random(),
            duration
        );

        Course second = new Course(
            id,
            CourseNameMother.random(),
            duration
        );

        // Act + Assert
        assertNotEquals(first, second);
    }

    @Test
    void not_be_equal_when_duration_is_different() {
        // Arrange
        CourseId id = CourseIdMother.random();
        CourseName name = CourseNameMother.random();

        Course first = new Course(
            id,
            name,
            CourseDurationMother.random()
        );

        Course second = new Course(
            id,
            name,
            CourseDurationMother.random()
        );

        // Act + Assert
        assertNotEquals(first, second);
    }

    @Test
    void equal_courses_have_same_hash_code() {
        // Arrange
        CourseId id = CourseIdMother.random();
        CourseName name = CourseNameMother.random();
        CourseDuration duration =
            CourseDurationMother.random();

        Course first =
            new Course(id, name, duration);

        Course second =
            new Course(id, name, duration);

        // Act + Assert
        assertEquals(
            first.hashCode(),
            second.hashCode()
        );
    }

    @Test
    void create_course_with_private_constructor()
        throws Exception {

        // Arrange
        Constructor<Course> constructor =
            Course.class.getDeclaredConstructor();

        constructor.setAccessible(true);

        // Act
        Course course = constructor.newInstance();

        // Assert
        assertNotNull(course);
        assertNull(course.id());
        assertNull(course.name());
        assertNull(course.duration());
    }

    @Test
    void create_an_empty_course_name_for_persistence() {
        // Act
        CourseName name = new CourseName();

        // Assert
        assertEquals("", name.value());
    }

    @Test
    void create_course_not_exist_error() {
        // Arrange
        CourseId id = CourseIdMother.random();

        // Act
        CourseNotExist error =
            new CourseNotExist(id);

        // Assert
        assertNotNull(error);
    }

    @Test
    void create_empty_course_duration_for_persistence()
        throws Exception {

        // Arrange
        Constructor<CourseDuration> constructor =
            CourseDuration.class.getDeclaredConstructor();

        constructor.setAccessible(true);

        // Act
        CourseDuration duration =
            constructor.newInstance();

        // Assert
        assertEquals("", duration.value());
    }

    @Test
    void create_empty_course_id_for_persistence() {
        // Act
        CourseId id = new CourseId();

        // Assert
        assertNotNull(id);
    }
}