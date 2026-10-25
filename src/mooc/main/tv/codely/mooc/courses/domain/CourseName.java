package tv.codely.mooc.courses.domain;

import tv.codely.shared.domain.StringValueObject;

public final class CourseName extends StringValueObject {
    public CourseName(String value) {
        super(value);

        if (value.isBlank()) {
            throw new IllegalArgumentException(
                "Course name cannot be empty"
            );
        }
    }

    public CourseName() {
        super("");
    }
}
