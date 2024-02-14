create table PUBLIC._GENRES
(
    ID   INTEGER auto_increment
        primary key,
    NAME CHARACTER VARYING(100)
);

create table PUBLIC._RATINGS
(
    ID   INTEGER auto_increment
        primary key,
    NAME CHARACTER VARYING(100)
);

create table PUBLIC._FILMS
(
    ID           INTEGER auto_increment
        primary key,
    NAME         CHARACTER VARYING(150),
    DESCRIPTION  CHARACTER VARYING(500),
    RELEASE_DATE DATE,
    DURATION     INTEGER,
    RATING_ID    INTEGER
        references PUBLIC._RATINGS
);

create table PUBLIC._FILM_GENRES
(
    FILM_ID  INTEGER
        references PUBLIC._FILMS,
    GENRE_ID INTEGER
        references PUBLIC._GENRES
);

create table PUBLIC._USERS
(
    ID       INTEGER auto_increment
        primary key,
    EMAIL    CHARACTER VARYING(100) not null,
    LOGIN    CHARACTER VARYING(100) not null,
    NAME     CHARACTER VARYING(150),
    BIRTHDAY DATE
);

create table PUBLIC._FRIENDSHIPS
(
    USER_ID   INTEGER
        references PUBLIC._USERS,
    FRIEND_ID INTEGER
        references PUBLIC._USERS,
    STATUS    BOOLEAN
);

create table PUBLIC._LIKES
(
    FILM_ID INTEGER
        references PUBLIC._FILMS,
    USER_ID INTEGER
        references PUBLIC._USERS
);

