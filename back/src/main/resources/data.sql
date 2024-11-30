-- Use the database mdd
USE
mdd;

-- Insert test users
INSERT INTO users (username, email, password)
VALUES ('devUser1', 'dev1@example.com', 'hashed_password_1'),
       ('devUser2', 'dev2@example.com', 'hashed_password_2');

-- Insert test themes
INSERT INTO themes (name, description)
VALUES ('JavaScript', 'A popular programming language mainly used for web development.'),
       ('Java', 'A general-purpose programming language that is class-based and object-oriented.'),
       ('Python', 'A high-level programming language known for its readability and broad usage.'),
       ('Web3', 'An emerging technology that utilizes blockchain and decentralized applications.');


-- Insert test subscriptions
INSERT INTO subscriptions (user_id, theme_id)
VALUES (1, 1), -- devUser1 subscribes to JavaScript
       (1, 2), -- devUser1 subscribes to Java
       (2, 3);
-- devUser2 subscribes to Python

-- Insert test articles
INSERT INTO articles (title, content, user_id, theme_id)
VALUES ('Introduction to JavaScript', 'An article about JavaScript basics...', 1, 1),
       ('Getting Started with Java', 'An article about Java programming...', 1, 2),
       ('Python Basics', 'An introduction to Python...', 2, 3);

-- Insert test comments
INSERT INTO comments (content, user_id, article_id)
VALUES ('Great article on JavaScript!', 2, 1), -- devUser2 comments on devUser1's article on JavaScript
       ('Very informative.', 1, 2); -- devUser1 comments on their own Java article
