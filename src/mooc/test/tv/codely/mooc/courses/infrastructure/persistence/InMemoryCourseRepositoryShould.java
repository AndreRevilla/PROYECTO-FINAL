package tv.codely.mooc.courses.infrastructure.persistence;

import org.junit.jupiter.api.Test;
import tv.codely.mooc.courses.CoursesModuleInfrastructureTestCase;
import tv.codely.mooc.courses.domain.Course;
import tv.codely.mooc.courses.domain.CourseIdMother;
import tv.codely.mooc.courses.domain.CourseMother;
import tv.codely.mooc.courses.domain.CourseDurationMother;
import tv.codely.mooc.courses.domain.CourseId;
import tv.codely.mooc.courses.domain.CourseNameMother;
import tv.codely.shared.domain.criteria.Criteria;
import tv.codely.shared.domain.criteria.Filters;
import tv.codely.shared.domain.criteria.Order;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;



final class InMemoryCourseRepositoryShould extends CoursesModuleInfrastructureTestCase {
    @Test
    void save_a_course() {
        Course course = CourseMother.random();

        inMemoryCourseRepository.save(course);
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
    void overwrite_a_course_when_saving_it_with_an_existing_id() {
        CourseId id = CourseIdMother.random();
        Course original = CourseMother.create(id, CourseNameMother.random(), CourseDurationMother.random());
        Course updated = CourseMother.create(id, CourseNameMother.random(), CourseDurationMother.random());

        inMemoryCourseRepository.save(original);
        inMemoryCourseRepository.save(updated);

        assertEquals(Optional.of(updated), inMemoryCourseRepository.search(id));
    }
    
    @Test
    void return_an_empty_list_when_matching_with_no_saved_courses() {
        Criteria criteria = new Criteria(Filters.none(), Order.none());

        assertEquals(List.of(), inMemoryCourseRepository.matching(criteria));
    }

    @Test
    void return_all_saved_courses_when_matching_without_filters() {
        Course first  = CourseMother.random();
        Course second = CourseMother.random();
        inMemoryCourseRepository.save(first);
        inMemoryCourseRepository.save(second);
        Criteria criteria = new Criteria(Filters.none(), Order.none());

        List<Course> result = inMemoryCourseRepository.matching(criteria);

        assertEquals(2, result.size());
        assertTrue(result.containsAll(List.of(first, second)));
    }
}
