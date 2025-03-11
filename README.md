# Projet final
Dans ce projet,  vous allez enrichir l'application en y ajoutant de nouvelles fonctionnalités.

## Etape 1 : Gestion d'addresses
Ajouter un mécanisme de gestion d'adresses dans le profile des utilisateurs. Pour cela, vous pouvez utiliser deux approches :
- Geocoding : Récupérer l'addresse à partir de la position de l'utilisateur
- Reverse Geocoding : Récupérer la position GPS à partir d'une addresse saisie manuellement
  Vous pouvez utiliser les apis Google Places pour le geoding et le reverse geocoding.

## Etape 2: Ajouter une vue cartagraphique (eg. Google Maps)
- Ajouter une vue permettant de visualiser les utilisateurs sur une carte.
- Utiliser le mécanisme clustering pour grouper les points en fonction du niveau de zoom sur la carte.
- Afficher les détails d'une utilisateur quand on clique sur un pin de la carte

## Etape 3: Gestion des photos
- Permettre de prendre une ou plusieurs photos qui seront associées aux profils des utilisateurs
- Synchroniser les photos avec l'API
- Modifier la vue de détail des utilisateurs pour y ajouter une gallerie photo

## Etape 4: Authentification
- Mettre en place une page d'authentification dans l'application en utilisant Firebase
- Offrir la possibilité de créer des comptes, pour les nouveaux utilisateurs

## Etape 5: Mise en relation
- Ajoutez une fonctionnalité de chat dans l'application en utilisant Firebase
- Gestion des notifications (eg. messages recus alors que l'application est fermée)

## Etape 6 : Gestion de liens profonds
- Mettre en place un mécanisme de lien profond (Deep link) permettant de générer des liens directs vers les profils utilisateurs de l'applcation.
  Lors du partage du profile de l'utilisateur, ajouter un lien profond permettant d'afficher le profile de l'utilisateur dans l'aplication.
  Dans la pratique, le lien profond permet de lancer l'application (si elle est installée sur le téléphone) et afficher directement le profile de l'utilisateur.

## Rendu
- Réaliser une vidéo démontrant toutes les fonctionnalittés de l'application
- Veuillez à effectuer votre dernier commit avant la deadline

## Bonus
- Toute fonctionnalité supplémentaire permettant d'enrichir l'application vous permettra de gagner des points bonus.
  Quelques idéées : gestion de calendrier, utiliser compose, ...

## Contraintes
- Respecter les principes de la clean architecture
- Respecter les bonnes pratiques en terme de naming de fichiers, classes etc...
- Bien respecter le principe du single responsability
