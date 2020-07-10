package com.comic.cerebro.repository;

import java.util.Optional;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.comic.cerebro.models.Collection;
import com.comic.cerebro.models.Comic;
import com.comic.cerebro.models.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

	Optional<User> findByUsername(String username);
	
	Boolean existsByUsername(String username);
	
	Boolean existsByEmail(String email);
	
	@Query("SELECT c.comicWishlist FROM User c WHERE c.username = ?1")
	Set<Comic> getComicWishlistByUsername(String username);
	
	@Query("SELECT c.collectionWishlist FROM User c WHERE c.username = ?1")
	Set<Collection> getCollectionWishlistByUsername(String username);
	
}
