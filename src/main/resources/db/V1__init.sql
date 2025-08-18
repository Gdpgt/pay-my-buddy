CREATE DATABASE IF NOT EXISTS paymybuddy;
USE paymybuddy;

CREATE TABLE `users` (
  `id` integer PRIMARY KEY AUTO_INCREMENT,
  `username` varchar(50) UNIQUE NOT NULL,
  `email` varchar(255) UNIQUE NOT NULL,
  `password` varchar(255) NOT NULL,
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `balance` decimal(14,2) NOT NULL DEFAULT 0
);

CREATE TABLE `transactions` (
  `id` integer PRIMARY KEY AUTO_INCREMENT,
  `sender_id` integer NOT NULL,
  `receiver_id` integer NOT NULL,
  `description` varchar(255),
  `amount` decimal(12,2) NOT NULL,
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE `user_connections` (
  `user_id` integer NOT NULL,
  `friend_id` integer NOT NULL,
  PRIMARY KEY (`user_id`, `friend_id`)
);

ALTER TABLE `transactions` ADD FOREIGN KEY (`sender_id`) REFERENCES `users` (`id`) ON DELETE RESTRICT;

ALTER TABLE `transactions` ADD FOREIGN KEY (`receiver_id`) REFERENCES `users` (`id`) ON DELETE RESTRICT;

ALTER TABLE `user_connections` ADD FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE CASCADE;

ALTER TABLE `user_connections` ADD FOREIGN KEY (`friend_id`) REFERENCES `users` (`id`) ON DELETE CASCADE;
