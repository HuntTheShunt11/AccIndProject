# AccIndProject - sujet pour interview

## Prérequis pour démarrer le projet

Pour pouvoir démarrer le projet, il est nécessaire d'avoir les éléments suivants installés sur votre machine :

Pour le backend :
  - Maven
 - docker compose

Pour le frontend :
 - Node.js
 - npm


### Initilisation de l'environnement et création des données

Pour créer la base de données et les tables, ainsi que pour insérer les données et créer les indexes, il suffit de lancer le conteneur Docker avec la commande suivante :

```bash
docker compose up -d
```

Ensuite dès que le conteneur est up il est possible de lancer le backend et le frontend, dans cet ordre.


### Lancement du backend

Pour lancer le backend, il suffit de se rendre dans le dossier `backend` et de lancer la commande suivante :

```bash
./mvn spring-boot:run
```
Une fois le backend lancé, il sera accessible à l'adresse `http://localhost:8080/`.

Un swagger est également disponible à l'adresse `http://localhost:8080/swagger-ui/index.html` pour avoir la description de l'endpoint de l'API REST.

### Lancement du frontend

Pour lancer le frontend, il suffit de se rendre dans le dossier `frontend` et de lancer la commande suivante :

```bash
npm install
ng serve
```
Le frontend sera accessible à l'adresse `http://localhost:4200/` par défaut.

###  Nettoyage des données

Pour supprimer les données et les tables, il suffit de lancer la commande suivante :

```bash
docker compose down -v
```
