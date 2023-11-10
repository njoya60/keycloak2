package com.mobapp.j2ds.mobappsecurity.web;

import com.mobapp.j2ds.mobappsecurity.dto.GroupDto;
import com.mobapp.j2ds.mobappsecurity.dto.UserDto;
import com.mobapp.j2ds.mobappsecurity.service.IKeyCloakService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/j2s/user")
//@PreAuthorize("hasRole('client_admin')")
@RequiredArgsConstructor
public class KeyCloakController {

    private final IKeyCloakService iKeyCloakService;

    @GetMapping("search")
    public ResponseEntity<?> findAllUsers() {
        return ResponseEntity.ok(iKeyCloakService.findAllUsers());
    }

    @GetMapping("search/{username}")
    public ResponseEntity<?> findAllUsernames(@PathVariable String username) {
        return ResponseEntity.ok(iKeyCloakService.searchUserByUsername(username));
    }

    @PostMapping("created")
    public ResponseEntity<?> createUser(@RequestBody @Valid UserDto userDto,
                                        BindingResult bindingResult) throws URISyntaxException {
        ResponseEntity<?> errors = getResponseEntity(bindingResult);
        if (errors != null) return errors;
        String response = iKeyCloakService.createUser(userDto);
        return ResponseEntity.created(new URI("/j2s/user/created")).body(response);
    }

    @PutMapping("updated/{userId}")
    public ResponseEntity<?> updateUser(@PathVariable String userId,
                                        @RequestBody @Valid UserDto userDto,
                                        BindingResult bindingResult) {
        ResponseEntity<?> errors = getResponseEntity(bindingResult);
        if (errors != null) return errors;
        iKeyCloakService.updateUser(userId, userDto);
        return ResponseEntity.ok("User updated successfully !!!");
    }

    @DeleteMapping("deleted/{userId}")
    public ResponseEntity<?> deleteUser(@PathVariable String userId) {
        iKeyCloakService.deleteUser(userId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("group_created")
    public ResponseEntity<?> createGroup(@RequestBody @Valid GroupDto groupDto,
                                        BindingResult bindingResult) throws URISyntaxException {
        ResponseEntity<?> errors = getResponseEntity(bindingResult);
        if (errors != null) return errors;
        String response = iKeyCloakService.addGroup(groupDto);
        return ResponseEntity.created(new URI("/j2s/user/group_created")).body(response);
    }

    private ResponseEntity<?> getResponseEntity(BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            List<String> errors = new ArrayList<>();
            for (FieldError fieldError : bindingResult.getFieldErrors()) {
                errors.add(fieldError.getField() + ": " + fieldError.getDefaultMessage());
            }
            return ResponseEntity.badRequest().body(errors);
        }
        return null;
    }
}
