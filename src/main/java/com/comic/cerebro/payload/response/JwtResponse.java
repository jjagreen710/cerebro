package com.comic.cerebro.payload.response;

import java.util.List;
import java.util.Set;

import com.comic.cerebro.models.Collection;
import com.comic.cerebro.models.Comic;

public class JwtResponse {
	
	private String token;
	
	private String type = "Bearer";
	
	private Long id;
	
	private String username;
	
	private String email;
	
	private List<String> roles;
	
	private Set<Comic> comicWishlist;
	
	private Set<Collection> collectionWishlist;

	public JwtResponse(String accessToken, Long id, String username, String email, Set<Comic> comicWishlist, Set<Collection> collectionWishlist, List<String> roles) {
		this.token = accessToken;
		this.id = id;
		this.username = username;
		this.email = email;
		this.comicWishlist = comicWishlist;
		this.collectionWishlist = collectionWishlist;
		this.roles = roles;
	}

	public String getAccessToken() {
		return token;
	}

	public void setAccessToken(String accessToken) {
		this.token = accessToken;
	}

	public String getTokenType() {
		return type;
	}

	public void setTokenType(String tokenType) {
		this.type = tokenType;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public Set<Comic> getComicWishlist() {
		return comicWishlist;
	}
	
	public void setComicWishlist(Set<Comic> comicWishlist) {
		this.comicWishlist = comicWishlist;
	}
	
	public Set<Collection> getCollectionWishlist() {
		return collectionWishlist;
	}
	
	public void setCollectionWishlist(Set<Collection> collectionWishlist) {
		this.collectionWishlist = collectionWishlist;
	}
	
	public List<String> getRoles() {
		return roles;
	}
	
}