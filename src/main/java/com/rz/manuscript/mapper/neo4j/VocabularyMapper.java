package com.rz.manuscript.mapper.neo4j;

import com.rz.manuscript.entity.neo4j.Vocabulary;
import org.springframework.data.neo4j.repository.Neo4jRepository;

public interface VocabularyMapper  extends Neo4jRepository<Vocabulary, Long> {
}
