package com.rz.manuscript.mapper.neo4j;

import com.rz.manuscript.entity.neo4j.Vocabulary;
import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface VocabularyMapper  extends Neo4jRepository<Vocabulary, Long> {
    @Query("MATCH (v:Vocabulary {name: $name,nodeType: 1})-[:NearSynonym]->(synonym:Vocabulary) RETURN synonym")
    List<Vocabulary> selectByName(@Param("name") String name);
    @Query("MATCH (v:Vocabulary) WHERE v.name IN $names AND v.nodeType = $nodeType RETURN v")
    List<Vocabulary> findByNamesAndNodeType(@Param("names") List<String> names, @Param("nodeType") int nodeType);

    @Query("MATCH (v:Vocabulary) WHERE v.nodeType = 1 AND v.name CONTAINS $nameLike RETURN count(v)")
    int countByNodeTypeAndNameLike(String nameLike);

    @Query("MATCH (v:Vocabulary) WHERE v.nodeType = 1 AND v.name CONTAINS $nameLike RETURN v SKIP $skip LIMIT $limit")
    List<Vocabulary> findByNodeTypeAndNameLike(String nameLike, int skip, int limit);
}
