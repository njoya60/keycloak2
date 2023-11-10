package com.mobapp.j2ds.mobappsecurity.service.impl;

import com.mobapp.j2ds.mobappsecurity.dto.GroupDto;
import com.mobapp.j2ds.mobappsecurity.dto.UserDto;
import com.mobapp.j2ds.mobappsecurity.service.IKeyCloakService;
import com.mobapp.j2ds.mobappsecurity.utils.KeycloakProvider;
import jakarta.ws.rs.core.Response;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.OAuth2Constants;
import org.keycloak.admin.client.resource.GroupsResource;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.GroupRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
@Slf4j
public class KeycloakServiceImpl implements IKeyCloakService {
    private static final String ROLE = "user";

    @Override
    public List<UserRepresentation> findAllUsers() {
        return KeycloakProvider.getRealmResource()
                .users()
                .list();
    }

    @Override
    public List<UserRepresentation> searchUserByUsername(String username) {
        return KeycloakProvider.getRealmResource()
                .users()
                .searchByUsername(username, true);
    }

    @Override
    public String createUser(@NonNull UserDto userDto) {
        int status;
        UsersResource usersResource = KeycloakProvider.getUserResource();

        Response response = usersResource.create(userRepresentation(userDto));
        System.out.println("*******************************************************");
        System.out.println(response);
        status = response.getStatus();
        if (status == 201) {
            String path = response.getLocation().getPath();
            String userId = path.substring(path.lastIndexOf("/") + 1);
            CredentialRepresentation credentialRepresentation = new CredentialRepresentation();
            credentialRepresentation.setTemporary(false);
            credentialRepresentation.setType(OAuth2Constants.PASSWORD);
            credentialRepresentation.setValue(userDto.password());
            usersResource.get(userId).resetPassword(credentialRepresentation);
            RealmResource realmResource = KeycloakProvider.getRealmResource();
            List<RoleRepresentation> roleRepresentations;
            if (userDto.roles() == null || userDto.roles().isEmpty()) {
                roleRepresentations = List.of(realmResource.roles().get(ROLE).toRepresentation());
            } else {
                roleRepresentations = realmResource.roles()
                        .list()
                        .stream()
                        .filter(role -> userDto.roles()
                                .stream()
                                .anyMatch(roleName -> roleName.equalsIgnoreCase(role.getName())))
                        .toList();
            }
            realmResource.users()
                    .get(userId)
                    .roles()
                    .realmLevel()
                    .add(roleRepresentations);
            sendVerificationEmail(userId);
            return "User created successfully";
        } else if (status == 409) {
            log.error("User already exist !!!");
            return "User already exist !!!";
        } else {
            log.error("Error creting user, Contact administrator");
            return "Error creting user, Contact administrator";
        }

    }

    private UserRepresentation userRepresentation(@NonNull UserDto userDto) {
        UserRepresentation userRepresentation = new UserRepresentation();

        userRepresentation.setFirstName(userDto.firstName());
        userRepresentation.setLastName(userDto.lastName());
        userRepresentation.setEmail(userDto.email());
        userRepresentation.setEmailVerified(true);
        userRepresentation.setUsername(userDto.username());
        userRepresentation.setEnabled(true);
        userRepresentation.setAttributes(userDto.attributes());
        return userRepresentation;
    }

    @Override
    public void deleteUser(String userId) {
        KeycloakProvider.getUserResource()
                .get(userId)
                .remove();
    }

    @Override
    public void updateUser(String userId, @NonNull UserDto userDto) {
        CredentialRepresentation credentialRepresentation = new CredentialRepresentation();
        credentialRepresentation.setTemporary(false);
        credentialRepresentation.setType(OAuth2Constants.PASSWORD);
        credentialRepresentation.setValue(userDto.password());
        userRepresentation(userDto).setCredentials(Collections.singletonList(credentialRepresentation));

        UserResource userResource = KeycloakProvider.getUserResource().get(userId);
        userResource.update(userRepresentation(userDto));

        userResource.sendVerifyEmail(userId);
    }

    @Override
    public String addGroup(GroupDto groupDto) {
        GroupRepresentation groupRepresentation = new GroupRepresentation();
        groupRepresentation.setName(groupDto.name());

        GroupsResource groupsResource = KeycloakProvider.getRealmResource().groups();

        groupsResource.add(groupRepresentation);

        return "Group created successfully.";
    }

    private void sendVerificationEmail(String userId) {
        UserResource userResource = KeycloakProvider.getUserResource().get(userId);

        userResource.sendVerifyEmail();

    }
}
