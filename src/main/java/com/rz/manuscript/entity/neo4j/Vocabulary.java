package com.rz.manuscript.entity.neo4j;

import lombok.Data;
import org.neo4j.ogm.annotation.GeneratedValue;
import org.neo4j.ogm.annotation.Id;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Relationship;

import java.util.Objects;
import java.util.Set;

@Data
@NodeEntity
public class Vocabulary {
    @Id
    @GeneratedValue
    private Long id;

    private String name;

    private Integer nodeType;

    @Relationship("NearSynonym")
    public Set<Vocabulary> nearSynonyms;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Vocabulary person = (Vocabulary) o;
        return  Objects.equals(name, person.name) ;
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
