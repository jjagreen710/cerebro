package com.comic.cerebro.controllers;

import java.util.ArrayList;
import java.util.List;
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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.comic.cerebro.models.Collection;
import com.comic.cerebro.models.Comic;
import com.comic.cerebro.models.User;
import com.comic.cerebro.repository.CollectionRepository;
import com.comic.cerebro.repository.ComicRepository;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/collection")
public class CollectionController {
	
	@Autowired
	private CollectionRepository collectionRepository;
	
	@Autowired
	private ComicRepository comicRepository;
	
	@GetMapping("/search")
	public ResponseEntity<List<Collection>> getAllCollections(@RequestParam(required = false) String collectionName) {
		try {
			List<Collection> collections = new ArrayList<Collection>();
			
			if (collectionName == null)
				collectionRepository.findAll().forEach(collections::add);
			else
				collectionRepository.findByCollectionNameContaining(collectionName).forEach(collections::add);
			if (collections.isEmpty()) {
				return new ResponseEntity<>(HttpStatus.NO_CONTENT);
			}
			return new ResponseEntity<>(collections, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
			}
		}
	
	@GetMapping("/admin/{id}")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<Collection> getCollectionById(@PathVariable("id") long id) {
	    Optional<Collection> collectionData = collectionRepository.findById(id);

	    if (collectionData.isPresent()) {
	      return new ResponseEntity<>(collectionData.get(), HttpStatus.OK);
	    } else {
	      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	    }
	  }
	
	@PostMapping("/admin")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<Collection> addCollection(@RequestBody Collection collection) {
		try {
			Collection _collection = collectionRepository.save(new Collection(collection.getIsbn(), collection.getCollectionName(), collection.getBookFormat(), collection.getComicsInCollection()));
				return new ResponseEntity<>(_collection, HttpStatus.CREATED);
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.EXPECTATION_FAILED);
		}
	}
	
	@PutMapping("/admin/{id}")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<Collection> updateCollection(@PathVariable("id") Long id, @RequestBody Collection collection) {
		Optional<Collection> collectionData = collectionRepository.findById(id);
		
		if (collectionData.isPresent()) {
			Collection _collection = collectionData.get();
			_collection.setIsbn(collection.getIsbn());
			_collection.setCollectionName(collection.getCollectionName());
			_collection.setBookFormat(collection.getBookFormat());
			_collection.setComicsInCollection(collection.getComicsInCollection());
			return new ResponseEntity<>(collectionRepository.save(_collection), HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}
	
	@DeleteMapping("/admin/{id}")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<Collection> deleteCollection(@PathVariable("id") Long id) {
		try {
			collectionRepository.deleteById(id);
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.EXPECTATION_FAILED);
		}
	}
	
	
	@GetMapping("/{id}")
	@PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
	public ResponseEntity<Set<Comic>> getComicsInCollection(@PathVariable("id") Long id) {
		try { Set<Comic> comicsInCollection = collectionRepository.getComicsInCollection(id);
		if (comicsInCollection.isEmpty()) {
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		} else {
			return new ResponseEntity<>(comicsInCollection, HttpStatus.OK);
		} 
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}	
	
	@PutMapping("/{collectionId}/{comicId}")
	@PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
	public ResponseEntity<Collection> addComicToCollection(@PathVariable("collectionId") long collectionId, @PathVariable("comicId") long comicId) {
			Optional<Collection> collection = collectionRepository.findById(collectionId);
			if (collection.isPresent()) {
				Collection _collection = collection.get();
				Optional<Comic> comic = comicRepository.findById(comicId);
					if (comic.isPresent()) {
						Comic _comic = comic.get();
						Set<Comic> comicsInCollection = _collection.getComicsInCollection();
						comicsInCollection.add(_comic);
						_collection.setComicsInCollection(comicsInCollection);
						return new ResponseEntity<>(collectionRepository.save(_collection), HttpStatus.OK);
					} else {
						return new ResponseEntity<>(HttpStatus.NOT_FOUND);
					}  	
			} else {
				return new ResponseEntity<>(HttpStatus.NOT_FOUND);
			} 
	}
	
	@DeleteMapping("/{collectionId}/{comicId}")
	@PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
	public ResponseEntity<Collection> deleteComicFromCollection(@PathVariable("collectionId") long collectionId, @PathVariable("comicId") long comicId) {
		Optional<Collection> collection = collectionRepository.findById(collectionId);
		if (collection.isPresent()) {
			Collection _collection = collection.get();
			Optional<Comic> comic = comicRepository.findById(comicId);
				if (comic.isPresent()) {
					Comic _comic = comic.get();
					_collection.getComicsInCollection().remove(_comic);
					return new ResponseEntity<>(collectionRepository.save(_collection), HttpStatus.OK);
				} else {
					return new ResponseEntity<>(HttpStatus.NOT_FOUND);
				}  	
		} else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		
	}

}
