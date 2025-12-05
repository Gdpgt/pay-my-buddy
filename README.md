## Pay My Buddy
Application (prototype) permettant à des amis de s'envoyer de l'argent

## Infos :
Cette application est un prototype. Etant donné que la fonctionnalité d'alimentation du solde n'est pas implémentée, le solde de chaque utilisateur est automatiquement crédité de 100€ lors de l'enregistrement. Cela permet de tester le système d'envoi via l'interface utilisateur.

## Prérequis pour lancer l'application :
- Java 21
- Un IDE avec Maven
- Avoir installé MySQL, et que le service soit démarré

## Comment lancer l'application :
- Cloner le projet sur son IDE favori
- Créer un fichier "env.properties" à la racine du projet, y glisser les variables suivantes accompagnées des valeurs choisies :  
"DB_URL=jdbc:mysql://localhost:3306/paymybuddy?createDatabaseIfNotExist=true&sslMode=REQUIRED",  
"DB_USER=",  
"DB_PASSWORD=",  
"DDL_AUTO=validate"
- Venir remplacer les variable "${}" dans le fichier "V2__create_user_paymybuddy.sql" par les valeurs choisies
- Dans le terminal de l'IDE, lancer "cat src/main/resources/db/V1__init.sql src/main/resources/db/V2__create_user_paymybuddy.sql | mysql -u root -p"
- Entrer le mot de passe du compte "root" MySQL
- Il n'y a plus qu'à lancer l'application soit via l'UI de l'IDE ou en lançant "mvn spring-boot:run" dans le terminal intégré

## Modèle Physique de Données
![Modèle Physique de Données](docs/mpd_paymybuddy.png)

**DBML (source)**

```dbml
Table users {
  id             integer [pk, increment]
  username       varchar(50)  [not null, unique]
  email          varchar(255) [not null, unique]
  password  varchar(100) [not null]
  created_at     timestamp    [not null, default: `CURRENT_TIMESTAMP`]
  balance        decimal(14,2) [not null, default: 0]
}

Table transactions {
  id           integer [pk, increment]
  sender_id    integer [not null, ref: > users.id]
  receiver_id  integer [not null, ref: > users.id]
  description  varchar(150)
  amount       decimal(12,2) [not null]
  created_at   timestamp [not null, default: `CURRENT_TIMESTAMP`]
}

Table user_connections {
  user_id   integer [not null, ref: > users.id]
  friend_id integer [not null, ref: > users.id]

  indexes {
    (user_id, friend_id) [pk]
  }
}
```
