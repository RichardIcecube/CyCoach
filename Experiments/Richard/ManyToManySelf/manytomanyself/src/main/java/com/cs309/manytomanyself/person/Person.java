package com.cs309.manytomanyself.person;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;

@Entity
@Table(name="persons")
@Data
public class Person {
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="person_id")
    private Long id;
	
	@NotNull
	@Size(max=100)
	@Column
	private String name;
	
	
	@ManyToMany(cascade={CascadeType.ALL})
	@JoinTable(name="friends_with",
	joinColumns={@JoinColumn(name="person_id")},
	inverseJoinColumns={@JoinColumn(name="friend_id")})
	@JsonIgnore
	private Set<Person> friends = new HashSet<Person>();
	
	@ManyToMany(mappedBy="friends")
	@JsonIgnore
	private Set<Person> friendsOf = new HashSet<Person>();
}
