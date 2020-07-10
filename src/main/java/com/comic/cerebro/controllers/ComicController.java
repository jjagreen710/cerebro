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
import com.comic.cerebro.repository.ComicRepository;


@CrossOrigin(origins = "*", allowedHeaders = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/comic")
public class ComicController {

	@Autowired
	private ComicRepository comicRepository;
	
	@GetMapping("/search")
	public ResponseEntity<List<Comic>> getAllComics(@RequestParam(required = false) String searchableTitle) {
		try {
			List<Comic> comics = new ArrayList<Comic>();
			
			if (searchableTitle == null)
				comicRepository.findAll().forEach(comics::add);
			else
				comicRepository.findBySearchableTitleContaining(searchableTitle).forEach(comics::add);
			if (comics.isEmpty()) {
				return new ResponseEntity<>(HttpStatus.NO_CONTENT);
			}
			return new ResponseEntity<>(comics, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
			}
		}
	
//	@GetMapping("/search")
//	public ResponseEntity<List<Comic>> getAllComics(@RequestParam(required = false) String searchableTitle) {
//		try {
//			List<Comic> comics = new ArrayList<Comic>();
//			
//			if (searchableTitle == null)
//				comicRepository.findAll().forEach(comics::add);
//			else
//				comicRepository.findBySearchableTitleLike("%" + searchableTitle + "%").forEach(comics::add);
//			if (comics.isEmpty()) {
//				return new ResponseEntity<>(HttpStatus.NO_CONTENT);
//			}
//			return new ResponseEntity<>(comics, HttpStatus.OK);
//		} catch (Exception e) {
//			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
//			}
//		}
//	
	@GetMapping("/admin/{id}")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<Comic> getComicById(@PathVariable("id") long id) {
	    Optional<Comic> comicData = comicRepository.findById(id);

	    if (comicData.isPresent()) {
	      return new ResponseEntity<>(comicData.get(), HttpStatus.OK);
	    } else {
	      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	    }
	  }
	
	@GetMapping("/collected/{id}")
	public ResponseEntity<List<String>> getCollected (@PathVariable("id") long id) {
		Optional<Comic> comicData = comicRepository.findById(id);
		
		if (comicData.isPresent()) {
			Comic _comic = comicData.get();
			Set<Collection> collected = _comic.getCollected();
			List<Collection> collectedList = new ArrayList<>(collected);
			List<String> collectionTitles = new ArrayList<>();
			for (int i=0; i<collectedList.size(); i++) 
			{ 
			  String collectionName = collectedList.get(i).getCollectionName();
			  collectionTitles.add(collectionName);
			}
			return new ResponseEntity<>(collectionTitles, HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}
	
	@PostMapping("/admin")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<Comic> addComic(@RequestBody Comic comic) {
		try {
			Comic _comic = comicRepository.save(new Comic(comic.getSeriesTitle(), comic.getIssueNumber(), comic.getPublicationDate(), comic.getStoryTitle(), comic.getCrossover(), comic.getMarvelId(), comic.getSearchableTitle()));
				return new ResponseEntity<>(_comic, HttpStatus.CREATED);
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.EXPECTATION_FAILED);
		}
	}
	
	@PutMapping("/admin/{id}")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<Comic> updateComic(@PathVariable("id") Long id, @RequestBody Comic comic) {
		Optional<Comic> comicData = comicRepository.findById(id);
		
		if (comicData.isPresent()) {
			Comic _comic = comicData.get();
			_comic.setSeriesTitle(comic.getSeriesTitle());
			_comic.setIssueNumber(comic.getIssueNumber());
			_comic.setPublicationDate(comic.getPublicationDate());
			_comic.setStoryTitle(comic.getStoryTitle());
			_comic.setCrossover(comic.getCrossover());
			_comic.setMarvelId(comic.getMarvelId());
			_comic.setSearchableTitle(comic.getSearchableTitle());
			return new ResponseEntity<>(comicRepository.save(_comic), HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}
	
	@DeleteMapping("/admin/{id}")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<Comic> deleteComic(@PathVariable("id") Long id) {
		try {
			comicRepository.deleteById(id);
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.EXPECTATION_FAILED);
		}
	}
	
}
