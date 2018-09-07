package com.epam.training.entity;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
@EqualsAndHashCode
public class User {
	private int id;
	private String name;
	private String surname;
	private String password;
	private String type;
	private String email;
}
