package tv.codely.mooc.reviews.domain;

import org.junit.jupiter.api.Test;
import tv.codely.mooc.courses.domain.CourseId;
import tv.codely.mooc.courses.domain.CourseIdMother;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

final class ReviewShould {

    @Test
    void be_created_with_valid_data() {
        // Arrange
        CourseId courseId = CourseIdMother.random();
        String id = "review-1";
        String studentId = "student-1";
        int rating = 5;
        String comment = "Excelente curso";

        // Act
        Review review = new Review(id, studentId, courseId, rating, comment);

        // Assert
        assertEquals(id, review.id());
        assertEquals(studentId, review.studentId());
        assertEquals(courseId, review.courseId());
        assertEquals(rating, review.rating());
        assertEquals(comment, review.comment());
    }

    @Test
    void accept_the_minimum_rating() {
        // Arrange
        CourseId courseId = CourseIdMother.random();

        // Act
        Review review = new Review("review-1", "student-1", courseId, 1, "Aceptable");

        // Assert
        assertEquals(1, review.rating());
    }

    @Test
    void accept_the_maximum_rating() {
        // Arrange
        CourseId courseId = CourseIdMother.random();

        // Act
        Review review = new Review("review-1", "student-1", courseId, 5, "Excelente");

        // Assert
        assertEquals(5, review.rating());
    }

    @Test
    void reject_a_rating_below_one() {
        // Arrange
        CourseId courseId = CourseIdMother.random();

        // Act + Assert
        assertThrows(
            IllegalArgumentException.class,
            () -> new Review("review-1", "student-1", courseId, 0, "Comentario")
        );
    }

    @Test
    void reject_a_rating_above_five() {
        // Arrange
        CourseId courseId = CourseIdMother.random();

        // Act + Assert
        assertThrows(
            IllegalArgumentException.class,
            () -> new Review("review-1", "student-1", courseId, 6, "Comentario")
        );
    }

    @Test
    void reject_a_blank_comment() {
        // Arrange
        CourseId courseId = CourseIdMother.random();

        // Act + Assert
        assertThrows(
            IllegalArgumentException.class,
            () -> new Review("review-1", "student-1", courseId, 4, "   ")
        );
    }

    @Test
    void reject_a_comment_longer_than_100_characters() {
        // Arrange
        CourseId courseId = CourseIdMother.random();
        String comment = "a".repeat(Review.MAX_COMMENT_LENGTH + 1);

        // Act + Assert
        assertThrows(
            IllegalArgumentException.class,
            () -> new Review("review-1", "student-1", courseId, 4, comment)
        );
    }

    @Test
    void accept_a_comment_of_exactly_100_characters() {
        // Arrange
        CourseId courseId = CourseIdMother.random();
        String comment = "a".repeat(Review.MAX_COMMENT_LENGTH);

        // Act
        Review review = new Review("review-1", "student-1", courseId, 4, comment);

        // Assert
        assertEquals(100, review.comment().length());
    }
}
