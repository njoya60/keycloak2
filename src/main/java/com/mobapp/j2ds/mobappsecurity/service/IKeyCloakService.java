package com.mobapp.j2ds.mobappsecurity.service;

import com.mobapp.j2ds.mobappsecurity.dto.GroupDto;
import com.mobapp.j2ds.mobappsecurity.dto.UserDto;
import jakarta.ws.rs.core.Response;
import org.keycloak.representations.idm.UserRepresentation;

import java.util.List;

public interface IKeyCloakService {
    List<UserRepresentation>findAllUsers();
    List<UserRepresentation>searchUserByUsername(String username);
    String createUser(UserDto userDto);
    void deleteUser(String userId);
    void updateUser(String userId, UserDto userDto);
    String addGroup(GroupDto groupDto);
}
