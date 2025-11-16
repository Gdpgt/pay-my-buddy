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

## Infos :
Cette application est un prototype. Etant donné que la fonctionnalité d'alimentation du solde n'est pas implémentée, le solde de chaque utilisateur est automatiquement crédité de 100€ lors de l'enregistrement. Cela permet de tester le système d'envoi via l'interface utilisateur.

