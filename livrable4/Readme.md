

# **PC Manager Application**


---

## **Exécution**

### **Identifiants pour la démonstration**
- **Requester** : `nizar@example.com / request123`
- **Assembler** : `assembler@example.com / assemble123`
- **Admin** : `khalil@email.com / admin123`
-**storekeeper**: `storekeeper@eexample.com / store123`
--
### **Fichier APK**
Le fichier APK est disponible dans le dossier `deliverables/apk`. Vous pouvez l'installer directement sur votre appareil Android pour tester l'application.

---


## **Description**
PC Manager est une application Android développée pour simplifier la gestion des commandes d'assemblage de PC. L'application repose sur une architecture basée sur les rôles et fournit des fonctionnalités spécifiques à chaque type d'utilisateur : **Requester**,**storekeeper**, **Assembler** et **Admin**. 

Le projet utilise Firebase pour l'authentification et la gestion en temps réel des données, offrant ainsi une expérience fluide et réactive.

---

## **Fonctionnalités**

### **Rôle : Requester**
- Création de commandes pour des composants spécifiques.
- Consultation des commandes existantes.
- Suppression des commandes si elles sont en attente.

### **Rôle : Assembler**
- Consultation de toutes les commandes en attente.
- Validation des commandes :
  - Mise en attente si le stock est insuffisant.
  - Rejet si la commande est irréalisable (ex. : rupture permanente).
- Approbation des commandes validées et visualisation virtuelle du PC assemblé.
### **Rôle : StoreKeeper**
-Gere les composantes dans le store 
### **Rôle : Admin**
- Gestion des utilisateurs et des rôles (Requester, Assembler, Admin).
- Ajout, modification et suppression des composants dans le stock.
- Réinitialisation complète des données et des stocks.

---

## **Installation**

### Installer l'application : 
- il suffit de telecharger le fichier apk sur un appareil andorid et installer.

### **Prérequis**
1. **Android Studio** version 2021.3 ou supérieure.
2. Compte Firebase configuré :
   - **Firebase Authentication** activée.
   - **Firebase Realtime Database** configurée.
3. Un appareil Android ou un émulateur fonctionnel.

### **Étapes**
1. Clonez le dépôt GitHub :
   ```bash
   git clone https://github.com/votre-projet-ici.git
   cd votre-projet-ici
   ```
2. Ouvrez le projet dans **Android Studio**.
3. Ajoutez le fichier `google-services.json` pour connecter Firebase au projet.
4. Compilez et exécutez l'application sur un appareil ou un émulateur Android.


## **Modèle conceptuel**
Le modèle conceptuel du projet est disponible dans le dossier `deliverables/model`. Il représente les entités et leurs relations principales :
- **Users** : Gère les rôles et les informations des utilisateurs.
- **Orders** : Structure des commandes avec statut, timestamps, etc.
- **Components** : Gestion des stocks.

Les fichiers sont disponibles en format source et PDF.

---

## **Contributions de l'équipe**

| **Nom**           | **Prénom**        | **Numéro étudiant** | **Contributions principales**                                    |
|-------------------|-------------------|---------------------|------------------------------------------------------------------|
| **El Othmani**    | **Mohamed Khalil**| **300389979**       | Intégration Firebase et développement backend.                   |
| **Rachidi**       | **Zouheir**       | **300329396**         | Développement des interfaces Android (UI/UX).                    |
| **Taleb**         | **Nizar**         | **300371104**       | Tests unitaires avec Mockito, coordination GitHub,documentation. |

---

## **Livrable 4 - Tag Git**
Pour marquer ce livrable comme complet, utilisez le tag suivant :
```bash
git tag deliverable-4
git push origin deliverable-4
```

---

## **Structure du dépôt**

- **`app/`** : Code source complet de l'application Android.
- **`deliverables/apk/`** : Fichier APK pour installation directe.
- **`deliverables/model/`** : Modèle conceptuel (source et PDF).
- **`deliverables/report/`** : Rapport final (source et PDF).
- **`README.md`** : Documentation complète.

---

## **Licence**
Ce projet est sous licence **MIT**. Vous pouvez l'utiliser, le modifier et le redistribuer librement. Consultez le fichier `LICENSE` pour plus de détails.

---
```