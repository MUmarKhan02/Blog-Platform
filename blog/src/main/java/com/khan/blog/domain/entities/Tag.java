package com.khan.blog.domain.entities;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

import jakarta.persistence.*;
import lombok.*;

//--------------------Tag Entity With HashCode, Equals, and Respective Needed Variables--------------------\\

@Entity
@Table(name = "tags")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class Tag {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, unique = true)
    private String name;
    
    @ManyToMany(mappedBy = "tags")
    private Set<Post> posts = new HashSet<>();

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }   

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;

        Tag other = (Tag) obj;
        return Objects.equals(this.id, other.id)
                && Objects.equals(this.name, other.name);
    }
}
