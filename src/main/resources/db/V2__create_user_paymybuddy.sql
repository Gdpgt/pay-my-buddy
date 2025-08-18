CREATE USER IF NOT EXISTS '${DB_USER}'@'localhost'
  IDENTIFIED BY '${DB_PASSWORD}';

GRANT SELECT, INSERT, UPDATE, DELETE, CREATE, ALTER, INDEX, DROP
ON paymybuddy.* TO '${DB_USER}'@'localhost';

FLUSH PRIVILEGES;

-- Vérification
SELECT user, host, plugin FROM mysql.user WHERE user='${DB_USER}';
