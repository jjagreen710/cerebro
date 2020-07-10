package com.comic.cerebro.models;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
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
@Table(name="comics")
public class Comic {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	private String seriesTitle;
	
	private int issueNumber;
	
	private String publicationDate;
	
	private String storyTitle;
	
	private String crossover;
	
	private String searchableTitle;
	
	private int marvelId;
	
	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable( name = "creative_team",
				joinColumns = @JoinColumn(name = "comic_id"),
				inverseJoinColumns = @JoinColumn(name = "creator_id"))
	private Set<Creator> creativeTeam = new HashSet<>();
	
	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable( name = "characters_in_comic",
				joinColumns = @JoinColumn(name = "comic_id"),
				inverseJoinColumns = @JoinColumn(name = "character_id"))
	private Set<Character> charactersInComic = new HashSet<>();
	
	@JsonIgnore
	@ManyToMany(mappedBy = "comicsInCollection", fetch = FetchType.LAZY)
	private Set<Collection> collected;
	
	public Comic() {
		
	}
	
	public Comic(String seriesTitle, int issueNumber, String publicationDate, String storyTitle, String crossover, int marvelId, String searchableTitle) {
		this.seriesTitle = seriesTitle;
		this.issueNumber = issueNumber;
		this.publicationDate = publicationDate;
		this.storyTitle = storyTitle;
		this.crossover = crossover;
		this.marvelId = marvelId;
		this.searchableTitle = searchableTitle;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getSeriesTitle() {
		return seriesTitle;
	}

	public void setSeriesTitle(String seriesTitle) {
		this.seriesTitle = seriesTitle;
	}

	public int getIssueNumber() {
		return issueNumber;
	}

	public void setIssueNumber(int issueNumber) {
		this.issueNumber = issueNumber;
	}

	public String getPublicationDate() {
		return publicationDate;
	}

	public void setPublicationDate(String publicationDate) {
		this.publicationDate = publicationDate;
	}

	public String getStoryTitle() {
		return storyTitle;
	}

	public void setStoryTitle(String storyTitle) {
		this.storyTitle = storyTitle;
	}

	public String getCrossover() {
		return crossover;
	}

	public void setCrossover(String crossover) {
		this.crossover = crossover;
	}

	public Set<Creator> getCreativeTeam() {
		return creativeTeam;
	}

	public void setCreativeTeam(Set<Creator> creativeTeam) {
		this.creativeTeam = creativeTeam;
	}

	public Set<Character> getCharactersInComic() {
		return charactersInComic;
	}

	public void setCharactersInComic(Set<Character> charactersInComic) {
		this.charactersInComic = charactersInComic;
	}
	
	public int getMarvelId() {
		return marvelId;
	}
	
	public void setMarvelId(int marvelId) {
		this.marvelId = marvelId;
	}

	public String getSearchableTitle() {
		return searchableTitle;
	}

	public void setSearchableTitle(String searchableTitle) {
		this.searchableTitle = searchableTitle;
	}
	
	public Set<Collection> getCollected() {
		return collected;
	}
		
	public void setCollected(Set<Collection> collected) {
		this.collected = collected;
	}
	
}
