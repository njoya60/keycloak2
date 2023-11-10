package com.mobapp.j2ds.mobappsecurity.service.impl;

import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.keycloak.admin.client.resource.GroupsResource;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.representations.idm.GroupRepresentation;

public class KeycloakGroupCreationImpl {

    public static void main(String[] args) {
        // Configuration pour se connecter à l'interface d'administration Keycloak
        String serverUrl = "http://localhost:8080/auth";
        String realm = "j2s-realm";
        String clientId = "admin-cli"; // Le client utilisé pour l'administration
        String username = "admin"; // Votre nom d'utilisateur admin
        String password = "admin"; // Votre mot de passe admin

        // Initialisation du client Keycloak Admin
        Keycloak keycloak = KeycloakBuilder.builder()
                .serverUrl(serverUrl)
                .realm(realm)
                .clientId(clientId)
                .username(username)
                .password(password)
                .build();

        // Obtention de la ressource Realm
        RealmResource realmResource = keycloak.realm(realm);

        // Création d'une représentation de groupe
        GroupRepresentation groupRepresentation = new GroupRepresentation();
        groupRepresentation.setName("user"); // Nom du groupe

        // Obtention de la ressource de groupes
        GroupsResource groupsResource = realmResource.groups();

        // Création du groupe
        groupsResource.add(groupRepresentation);

        System.out.println("Group created successfully.");
    }
}
