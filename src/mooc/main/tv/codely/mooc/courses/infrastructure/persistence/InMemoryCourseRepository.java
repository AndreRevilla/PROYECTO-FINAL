package tv.codely.mooc.courses.infrastructure.persistence;

import tv.codely.mooc.courses.domain.Course;
import tv.codely.mooc.courses.domain.CourseId;
import tv.codely.mooc.courses.domain.CourseRepository;
import tv.codely.shared.domain.criteria.Criteria;

import tv.codely.shared.domain.criteria.Filter;

import java.util.stream.Collectors;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;

public final class InMemoryCourseRepository implements CourseRepository {
    private HashMap<String, Course> courses = new HashMap<>();

    @Override
    public void save(Course course) {
        courses.put(course.id().value(), course);
    }

    public Optional<Course> search(CourseId id) {
        return Optional.ofNullable(courses.get(id.value()));
    }

    @Override
    public List<Course> matching(Criteria criteria) {
        return courses.values()
                      .stream()
                      .filter(course -> criteria.filters().filters().stream().allMatch(filter -> matches(course, filter)))
                      .collect(Collectors.toList());
    }

    private boolean matches(Course course, Filter filter) {
        return course.name().value().equals(filter.value().value());
    }
}
