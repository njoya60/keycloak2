package com.mobapp.j2ds.mobappsecurity.service.impl;

import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.UserRepresentation;

public class fgfg {

    void activedesactive() {
        // Paramètres de connexion à Keycloak
        String serverUrl = "http://localhost:8080/auth"; // URL de Keycloak
        String realm = "your-realm"; // Votre royaume
        String clientId = "admin-cli"; // Client ID pour l'authentification d'administration
        String clientSecret = "your-client-secret"; // Votre client secret pour l'authentification d'administration

        String usernameToDisable = "user-to-disable"; // Nom d'utilisateur du compte à désactiver

        // Initialisation du client Keycloak Admin
        Keycloak keycloak = KeycloakBuilder.builder()
                .serverUrl(serverUrl)
                .realm(realm)
                .clientId(clientId)
                .clientSecret(clientSecret)
                .build();

        // Récupération des ressources du royaume
        RealmResource realmResource = keycloak.realm(realm);
        UsersResource usersResource = realmResource.users();

        // Recherche de l'utilisateur par nom d'utilisateur
        UserRepresentation user = usersResource.search(usernameToDisable).get(0);

        // Désactivation du compte utilisateur
        user.setEnabled(false);

        // Mise à jour du compte utilisateur
        usersResource.get(user.getId()).update(user);

        System.out.println("Le compte utilisateur a été désactivé.");
    }
}
