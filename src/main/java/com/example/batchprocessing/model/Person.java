package com.example.batchprocessing.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Person {

	@Id
	private String id;
	private String lastName;
	private String firstName;

	public Person(String firstName, String lastName) {
		this.firstName = firstName;
		this.lastName = lastName;

	}

	@Override
	public String toString() {
		return "ID: " + id + ", First Name: " + firstName + ", Last Name: " + lastName;
	}

}
