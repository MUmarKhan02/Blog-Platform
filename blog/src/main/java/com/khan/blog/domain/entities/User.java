package com.khan.blog.domain.entities;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

//--------------------User Entity With HashCode, Equals, and Respective Needed Variables--------------------\\

@Entity
@Table(name = "users")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder

public class User {
    @Id
    @GeneratedValue (strategy = GenerationType.UUID)
    private UUID id;
    @Column(nullable = false,unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String name;

    @OneToMany(mappedBy = "author",cascade = CascadeType.ALL,orphanRemoval=true)
    private List<Post> posts = new ArrayList<>();

    @Column(nullable = false)
    private LocalDateTime createdAt;


    @Override
    public int hashCode() {
       return getClass().hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass())return false;
       
        User other = (User) obj;
       
        return id!=null && id.equals(other.id);
    }

    @PrePersist
    public void onCreate() {
        this.createdAt = LocalDateTime.now();
    }
    
}
