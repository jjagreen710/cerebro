package com.comic.cerebro.controllers;

import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.comic.cerebro.models.Collection;
import com.comic.cerebro.models.Comic;
import com.comic.cerebro.models.User;
import com.comic.cerebro.repository.CollectionRepository;
import com.comic.cerebro.repository.ComicRepository;
import com.comic.cerebro.repository.UserRepository;

@CrossOrigin(origins = "*", allowedHeaders = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/wantlist")
public class WantlistController {
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private ComicRepository comicRepository;
	
	@Autowired
	private CollectionRepository collectionRepository;
	
	@GetMapping("/{username}")
	@PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
	public ResponseEntity<Set<Comic>> getComicWantlist(@PathVariable("username") String username) {
		try { Set<Comic> comicWishlist = userRepository.getComicWishlistByUsername(username);
		if (comicWishlist.isEmpty()) {
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		} else {
			return new ResponseEntity<>(comicWishlist, HttpStatus.OK);
		} 
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}	
	
	@GetMapping("/{username}/{id}")
	@PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
	public ResponseEntity<Comic> getComicFromWantlist(@PathVariable("username") String username, @PathVariable("id") long id) {
		try { Set<Comic> comicWishlist = userRepository.getComicWishlistByUsername(username);
		if (comicWishlist.isEmpty()) {
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		} else { 
			Optional<Comic> comic = comicRepository.findById(id);
				if (comic.isPresent()) {
					Comic _comic = comic.get();
					if (comicWishlist.contains(_comic)) {
						return new ResponseEntity<>(_comic, HttpStatus.OK);
					} else {
						return new ResponseEntity<>(HttpStatus.NOT_FOUND);
					} 
				} else {
					return new ResponseEntity<>(HttpStatus.NOT_FOUND);
				}
			}
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
}
	
	@PutMapping("/{username}/{id}")
//	@PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
	public ResponseEntity<User> addComicToWantlist(@PathVariable("username") String username, @PathVariable("id") long id) {
			Optional<User> user = userRepository.findByUsername(username);
			if (user.isPresent()) {
				User _user = user.get();
				Optional<Comic> comic = comicRepository.findById(id);
					if (comic.isPresent()) {
						Comic _comic = comic.get();
						Set<Comic> comicWishlist = _user.getComicWishlist();
						comicWishlist.add(_comic);
						_user.setComicWishlist(comicWishlist);
						return new ResponseEntity<>(userRepository.save(_user), HttpStatus.OK);
					} else {
						return new ResponseEntity<>(HttpStatus.NOT_FOUND);
					}  	
			} else {
				return new ResponseEntity<>(HttpStatus.NOT_FOUND);
			} 
	}
	
	@DeleteMapping("/{username}/{id}")
	@PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
	public ResponseEntity<User> deleteComicFromWantlist(@PathVariable("username") String username, @PathVariable("id") long id) {
		Optional<User> user = userRepository.findByUsername(username);
		if (user.isPresent()) {
			User _user = user.get();
			Optional<Comic> comic = comicRepository.findById(id);
				if (comic.isPresent()) {
					Comic _comic = comic.get();
					_user.getComicWishlist().remove(_comic);
					return new ResponseEntity<>(userRepository.save(_user), HttpStatus.OK);
				} else {
					return new ResponseEntity<>(HttpStatus.NOT_FOUND);
				}  	
		} else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		
	}
	
//	@GetMapping("/wantlist/{username}")
//	@PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
//	public ResponseEntity<Set<Collection>> getCollectionWantlist(@PathVariable("username") String username) {
//		try { Set<Collection> collectionWishlist = userRepository.getCollectionWishlistByUsername(username);
//		if (collectionWishlist.isEmpty()) {
//			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
//		} else {
//			return new ResponseEntity<>(collectionWishlist, HttpStatus.OK);
//		} 
//		} catch (Exception e) {
//			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
//		}
//	}	
//	
//	@GetMapping("/wantlist/{username}/{id}")
//	@PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
//	public ResponseEntity<Collection> getCollectionFromWantlist(@PathVariable("username") String username, @PathVariable("id") long id) {
//		try { Set<Collection> collectionWishlist = userRepository.getCollectionWishlistByUsername(username);
//		if (collectionWishlist.isEmpty()) {
//			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
//		} else { 
//			Optional<Collection> collection = collectionRepository.findById(id);
//				if (collection.isPresent()) {
//					Collection _collection = collection.get();
//					if (collectionWishlist.contains(_collection)) {
//						return new ResponseEntity<>(_collection, HttpStatus.OK);
//					} else {
//						return new ResponseEntity<>(HttpStatus.NOT_FOUND);
//					} 
//				} else {
//					return new ResponseEntity<>(HttpStatus.NOT_FOUND);
//				}
//			}
//		} catch (Exception e) {
//			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
//		}
//}
//	
//	@PutMapping("/wantlist/{username}/{id}")
//	@PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
//	public ResponseEntity<User> addCollectionToWantlist(@PathVariable("username") String username, @PathVariable("id") long id) {
//			Optional<User> user = userRepository.findByUsername(username);
//			if (user.isPresent()) {
//				User _user = user.get();
//				Optional<Collection> collection = collectionRepository.findById(id);
//					if (collection.isPresent()) {
//						Collection _collection = collection.get();
//						Set<Collection> collectionWishlist = _user.getCollectionWishlist();
//						collectionWishlist.add(_collection);
//						_user.setCollectionWishlist(collectionWishlist);
//						return new ResponseEntity<>(userRepository.save(_user), HttpStatus.OK);
//					} else {
//						return new ResponseEntity<>(HttpStatus.NOT_FOUND);
//					}  	
//			} else {
//				return new ResponseEntity<>(HttpStatus.NOT_FOUND);
//			} 
//	}
//	
//	@DeleteMapping("/wantlist/{username}/{id}")
//	@PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
//	public ResponseEntity<User> deleteCollectionFromWantlist(@PathVariable("username") String username, @PathVariable("id") long id) {
//		Optional<User> user = userRepository.findByUsername(username);
//		if (user.isPresent()) {
//			User _user = user.get();
//			Optional<Collection> collection = collectionRepository.findById(id);
//				if (collection.isPresent()) {
//					Collection _collection = collection.get();
//					_user.getCollectionWishlist().remove(_collection);
//					return new ResponseEntity<>(userRepository.save(_user), HttpStatus.OK);
//				} else {
//					return new ResponseEntity<>(HttpStatus.NOT_FOUND);
//				}  	
//		} else {
//			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
//		}
//		
//	}
//	
}			
		