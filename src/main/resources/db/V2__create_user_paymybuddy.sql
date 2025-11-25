-- Ce script a pour but de créer un user et mot de passe que l'app paymybuddy 
-- pourra utiliser pour se connecter à la DB. C'est une bonne pratique qui évite 
-- de laisser l'app tourner avec les identifiants 'root' superutilisateur
-- de MySQL (qui autorise par exemple le table drop)

-- ATTENTION !!! Remplacer les variables ${} par les vraies valeurs choisies

CREATE USER IF NOT EXISTS '${DB_USER}'@'localhost'
  IDENTIFIED BY '${DB_PASSWORD}';

GRANT SELECT, INSERT, UPDATE, DELETE
ON paymybuddy.* TO '${DB_USER}'@'localhost';

FLUSH PRIVILEGES;

-- Vérification
SELECT user, host, plugin FROM mysql.user WHERE user='${DB_USER}';
