package tv.codely.mooc.courses.application.search_last;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tv.codely.mooc.courses.CoursesModuleUnitTestCase;
import tv.codely.mooc.courses.application.CoursesResponse;
import tv.codely.shared.domain.criteria.Criteria;
import tv.codely.mooc.courses.domain.Course;
import tv.codely.mooc.courses.domain.CourseMother;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

final class SearchLastCoursesQueryHandlerShould
    extends CoursesModuleUnitTestCase {

    private SearchLastCoursesQueryHandler handler;

    @BeforeEach
    protected void setUp() {
        super.setUp();

        LastCoursesSearcher searcher =
            new LastCoursesSearcher(repository);

        handler =
            new SearchLastCoursesQueryHandler(searcher);
    }

    @Test
    void search_last_courses() {
        // Arrange
        SearchLastCoursesQuery query =
            SearchLastCoursesQueryMother.create(5);

        when(repository.matching(any(Criteria.class)))
            .thenReturn(java.util.Collections.emptyList());

        // Act
        CoursesResponse response =
            handler.handle(query);

        // Assert
        assertNotNull(response);

        org.mockito.ArgumentCaptor<Criteria> captor =
            org.mockito.ArgumentCaptor.forClass(Criteria.class);

        org.mockito.Mockito.verify(repository)
            .matching(captor.capture());

        Criteria criteria = captor.getValue();

        assertEquals(
            java.util.Optional.of(5),
            criteria.limit()
        );

        assertEquals(
            java.util.Optional.empty(),
            criteria.offset()
        );

        assertFalse(
            criteria.hasFilters()
        );

        assertFalse(
            criteria.order().hasOrder()
        );
    }

    @Test
    void return_existing_courses() {
        // Arrange
        SearchLastCoursesQuery query =
            SearchLastCoursesQueryMother.create(1);

        Course course = CourseMother.random();

        when(repository.matching(any(Criteria.class)))
            .thenReturn(java.util.List.of(course));

        // Act
        CoursesResponse response =
            handler.handle(query);

        // Assert
        assertNotNull(response);
    }
}