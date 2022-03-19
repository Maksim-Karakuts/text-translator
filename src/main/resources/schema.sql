DROP TABLE IF EXISTS TRANSLATION;
CREATE TABLE TRANSLATION
(
    id              INT AUTO_INCREMENT PRIMARY KEY,
    initial_text    VARCHAR(1000) NOT NULL,
    translated_text VARCHAR(1000),
    from_language   VARCHAR(2)    NOT NULL,
    to_language     VARCHAR(2)    NOT NULL,
    request_time    TIMESTAMP     NOT NULL,
    user_ip         VARCHAR(40)
);

DROP TABLE IF EXISTS WORD;
CREATE TABLE WORD
(
    id              INT AUTO_INCREMENT PRIMARY KEY,
    translation_id  INT,
    initial_word    VARCHAR(25) NOT NULL,
    translated_word VARCHAR(25),
    foreign key (translation_id) references TRANSLATION (id)
);