package com.comic.cerebro.repository;

import java.util.List;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.comic.cerebro.models.Collection;
import com.comic.cerebro.models.Comic;

@Repository
public interface CollectionRepository extends JpaRepository<Collection, Long> {

	List<Collection> findByCollectionNameContaining(String collectionName);
	
	@Query("SELECT c.comicsInCollection FROM Collection c WHERE c.id = ?1")
	Set<Comic> getComicsInCollection(Long id);

}
