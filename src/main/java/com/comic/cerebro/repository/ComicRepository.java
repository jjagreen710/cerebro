package com.comic.cerebro.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.comic.cerebro.models.Collection;
import com.comic.cerebro.models.Comic;

@Repository
public interface ComicRepository extends JpaRepository<Comic, Long> {

	List<Comic> findBySearchableTitleContaining(String searchableTitle);
	
	List<Comic> findBySearchableTitleLike(String searchableTitle);
		
}
