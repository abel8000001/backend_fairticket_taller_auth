package upb.edu.co.fairticket.domain.model;

import java.util.UUID;
import java.time.LocalDateTime;

import lombok.Getter;
import lombok.AllArgsConstructor;

import upb.edu.co.fairticket.domain.model.enums.Role;
import upb.edu.co.fairticket.domain.model.valueobjects.Email;

@Getter
@AllArgsConstructor
public class User {

    private UUID id;
    private String name;
    private Email email;
    private Role role;
    private String passwordHash;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public User(UUID id, String name, Email email, Role role, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this(id, name, email, role, "", createdAt, updatedAt);
    }

    public static User createBuyer(String name, Email email, String passwordHash) {
        return new User(UUID.randomUUID(), name, email, Role.BUYER, passwordHash, LocalDateTime.now(), LocalDateTime.now());
    }

    public static User createOrganizer(String name, Email email, String passwordHash) {
        return new User(UUID.randomUUID(), name, email, Role.ORGANIZER, passwordHash,
                LocalDateTime.now(), LocalDateTime.now());
    }

    public static User createAdmin(String name, Email email, String passwordHash) {
        return new User(UUID.randomUUID(), name, email, Role.ADMIN, passwordHash,
                LocalDateTime.now(), LocalDateTime.now());
    }

    public void updateProfile(String name, Email email) {
        this.name = name;
        this.email = email;
        this.updatedAt = LocalDateTime.now();
    }

    public boolean isAdmin() {
        return this.role.equals(Role.ADMIN);
    }

    public boolean isOrganizer() {
        return this.role.equals(Role.ORGANIZER);
    }

    public boolean isBuyer() {
        return this.role.equals(Role.BUYER);
    }

}
