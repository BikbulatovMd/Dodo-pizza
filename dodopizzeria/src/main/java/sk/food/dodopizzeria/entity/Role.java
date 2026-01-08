package sk.food.dodopizzeria.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "roles")
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Názov roly je povinný")
    @Size(max = 50, message = "Názov roly nesmie presiahnuť 50 znakov")
    @Column(nullable = false, unique = true, length = 50)
    private String name;

    @NotBlank(message = "Zobrazovaný názov je povinný")
    @Size(max = 50, message = "Zobrazovaný názov nesmie presiahnuť 50 znakov")
    @Column(name = "display_name_sk", nullable = false, length = 50)
    private String displayNameSk;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @ManyToMany(mappedBy = "roles")
    private Set<User> users = new HashSet<>();

    public Role() {
    }

    public Role(Long id, String name, String displayNameSk, LocalDateTime createdAt,
                LocalDateTime updatedAt, Set<User> users) {
        this.id = id;
        this.name = name;
        this.displayNameSk = displayNameSk;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.users = users;
    }

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDisplayNameSk() {
        return displayNameSk;
    }

    public void setDisplayNameSk(String displayNameSk) {
        this.displayNameSk = displayNameSk;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Set<User> getUsers() {
        return users;
    }

    public void setUsers(Set<User> users) {
        this.users = users;
    }

    @Override
    public String toString() {
        return "Role{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", displayNameSk='" + displayNameSk + '\'' +
                '}';
    }
}
