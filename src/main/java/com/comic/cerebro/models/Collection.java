package com.comic.cerebro.models;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name="collections")
public class Collection {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	private long isbn;
	
	private String collectionName;
	
	private String bookFormat;
	
	@JsonIgnore
	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable( name = "comics_in_collection",
				joinColumns = @JoinColumn(name = "collection_id"),
				inverseJoinColumns = @JoinColumn(name = "comic_id"))
	private Set<Comic> comicsInCollection = new HashSet<>();
	
	public Collection() {
		
	}

	public Collection(long isbn, String collectionName, String bookFormat, Set<Comic> comicsInCollection) {
		this.isbn = isbn;
		this.collectionName = collectionName;
		this.bookFormat = bookFormat;
		this.comicsInCollection = comicsInCollection;
	}
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public long getIsbn() {
		return isbn;
	}

	public void setIsbn(long isbn) {
		this.isbn = isbn;
	}

	public String getCollectionName() {
		return collectionName;
	}

	public void setCollectionName(String collectionName) {
		this.collectionName = collectionName;
	}

	public String getBookFormat() {
		return bookFormat;
	}

	public void setBookFormat(String bookFormat) {
		this.bookFormat = bookFormat;
	}

	public Set<Comic> getComicsInCollection() {
		return comicsInCollection;
	}

	public void setComicsInCollection(Set<Comic> comicsInCollection) {
		this.comicsInCollection = comicsInCollection;
	}

}
