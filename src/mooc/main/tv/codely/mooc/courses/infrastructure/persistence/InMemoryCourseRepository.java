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
        List<Course> result =
            new java.util.ArrayList<>(courses.values());

        // Aplicar filtros
        for (tv.codely.shared.domain.criteria.Filter filter
            : criteria.filters().filters()) {

            List<Course> filtered =
                new java.util.ArrayList<>();

            for (Course course : result) {
                if (matches(course, filter)) {
                    filtered.add(course);
                }
            }

            result = filtered;
        }

        // Aplicar orden
        if (criteria.order().hasOrder()) {
            sortCourses(
                result,
                criteria.order().orderBy().value(),
                criteria.order().orderType().isAsc()
            );
        }

        // Aplicar offset
        int offset = criteria.offset().orElse(0);

        if (offset >= result.size()) {
            return java.util.Collections.emptyList();
        }

        // Aplicar limit
        int limit = criteria.limit().orElse(
            result.size() - offset
        );

        int end =
            Math.min(offset + limit, result.size());

        return new java.util.ArrayList<>(
            result.subList(offset, end)
        );
    }

    private boolean matches(
        Course course,
        tv.codely.shared.domain.criteria.Filter filter
    ) {
        String courseValue =
            fieldValue(course, filter.field().value());

        if (courseValue == null) {
            return false;
        }

        String expectedValue =
            filter.value().value();

        switch (filter.operator()) {
            case EQUAL:
                return courseValue.equals(expectedValue);

            case NOT_EQUAL:
                return !courseValue.equals(expectedValue);

            case GT:
                return courseValue.compareTo(expectedValue) > 0;

            case LT:
                return courseValue.compareTo(expectedValue) < 0;

            case CONTAINS:
                return courseValue.contains(expectedValue);

            case NOT_CONTAINS:
                return !courseValue.contains(expectedValue);
        }

        return false;
    }

    private String fieldValue(
        Course course,
        String field
    ) {
        switch (field) {
            case "id":
                return course.id().value();

            case "name":
                return course.name().value();

            case "duration":
                return course.duration().value();

            default:
                return null;
        }
    }

    private void sortCourses(
        List<Course> courses,
        String field,
        boolean ascending
    ) {
        if (!isValidField(field)) {
            return;
        }

        for (int i = 0; i < courses.size() - 1; i++) {
            for (int j = i + 1; j < courses.size(); j++) {

                String first =
                    fieldValue(courses.get(i), field);

                String second =
                    fieldValue(courses.get(j), field);

                int comparison =
                    first.compareTo(second);

                boolean shouldSwap =
                    ascending
                        ? comparison > 0
                        : comparison < 0;

                if (shouldSwap) {
                    Course temp = courses.get(i);

                    courses.set(i, courses.get(j));
                    courses.set(j, temp);
                }
            }
        }
    }

    private boolean isValidField(String field) {
        return field.equals("id")
            || field.equals("name")
            || field.equals("duration");
    }
}
