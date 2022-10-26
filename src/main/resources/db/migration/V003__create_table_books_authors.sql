create table books_authors
(
    book_id   bigint,
    author_id bigint,
    FOREIGN KEY (book_id) references book (id),
    FOREIGN KEY (author_id) references author (id),
    PRIMARY KEY (book_id, author_id)

);