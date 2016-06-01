create table customer
(
	id integer IDENTITY PRIMARY KEY, 
	firstname varchar(20) not null, 
	lastname varchar(20) not null, 
	username varchar(20) not null unique,
	password varchar(20) not null, 
	depositedAmount integer default 0
);
