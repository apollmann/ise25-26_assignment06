package de.seuhd.campuscoffee.domain.model;

import lombok.Builder;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

import org.jspecify.annotations.NonNull;
import io.micrometer.common.lang.Nullable; // Why other package than in Pos.java?


/**
 * Domain record that stores the User metadata.
 * We validate the fields in the API layer based on the DTOs, so no validation annotations are needed here.
 *
 * @param id          the unique identifier; null when the User has not been created yet
 * @param createdAt   timestamp set on User creation
 * @param updatedAt   timestamp set on User creation and update
 * @param loginName        the name of the User
 * @param emailAddress        the email address of the User
 * @param firstName        the first name of the User
 * @param lastName        the last name of the User 
 */
@Builder(toBuilder = true)
public record User(
        @Nullable Long id, // null when the User has not been created yet
        @Nullable LocalDateTime createdAt, // set on User creation
        @Nullable LocalDateTime updatedAt, // set on User creation and update
        @NonNull String loginName,
        @NonNull String emailAddress,
        @NonNull String firstName,
        @NonNull String lastName
) implements Serializable { // serializable to allow cloning (see TestFixtures class).
    @Serial
    private static final long serialVersionUID = 1L;
}
