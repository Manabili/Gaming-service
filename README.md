Functionality Available are -

1. User registration / login  -> If new user, new entry will be inserted, otherwise if the credentials are valid, user will be logged in.
2. Game Score storing         -> If user's score is greater than previous highest score, it will be updated, otherwise it won't be updated.
3. Get Top 5 users for the game   -> TimeStamp for the users has also been stored, incase of same score of multiple users, preference has been given to timeStamp in ascending order.


MySQL Schema Details - 

User Table : 

CREATE TABLE `users` (
`userId` int NOT NULL AUTO_INCREMENT,
`userName` varchar(100) NOT NULL,
`userEmail` varchar(100) NOT NULL,
`password` varchar(100) NOT NULL,
PRIMARY KEY (`userId`),
UNIQUE KEY `userEmail` (`userEmail`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci



game_score table :

CREATE TABLE `game_scores` (
`userId` int NOT NULL,
`game_score` int DEFAULT NULL,
`game_timestamp` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
PRIMARY KEY (`userId`),
CONSTRAINT `fk_userId` FOREIGN KEY (`userId`) REFERENCES `users` (`userId`) ON DELETE RESTRICT ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci
