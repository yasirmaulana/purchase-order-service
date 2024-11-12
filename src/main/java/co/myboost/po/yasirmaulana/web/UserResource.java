package co.myboost.po.yasirmaulana.web;

import co.myboost.po.yasirmaulana.domain.User;
import co.myboost.po.yasirmaulana.dto.ResultPageResponseDTO;
import co.myboost.po.yasirmaulana.dto.UserCreateUpdateRequestDTO;
import co.myboost.po.yasirmaulana.dto.UserListResponseDTO;
import co.myboost.po.yasirmaulana.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/v1/user")
@AllArgsConstructor
public class UserResource {

    private final UserService userService;

    @PostMapping
    public ResponseEntity<Void> createNewUser(@RequestBody UserCreateUpdateRequestDTO dto) {
        userService.createUser(dto);
        return ResponseEntity.created(URI.create("/v1/user")).build();
    }

    @PutMapping("/{userId}")
    public ResponseEntity<Void> updateUser(@PathVariable int userId, @RequestBody UserCreateUpdateRequestDTO dto) {
        userService.updateUser(userId, dto);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> deleteUser(@PathVariable int userId) {
        userService.deleteUser(userId);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<ResultPageResponseDTO<UserListResponseDTO>> findUserList(
            @RequestParam(name = "page", required = true, defaultValue = "0") Integer pages,
            @RequestParam(name = "limit", required = true, defaultValue = "10") Integer limit,
            @RequestParam(name = "sortBy", required = true, defaultValue = "firstName") String sortBy,
            @RequestParam(name = "direction", required = true, defaultValue = "asc") String direction,
            @RequestParam(name = "firstName", required = false) String firstName) {
        return ResponseEntity.ok().body(userService.getUserList(pages, limit, sortBy, direction, firstName));
    }

    @GetMapping("/{userId}")
    public ResponseEntity<User> findUserById(@PathVariable int userId) {
        return ResponseEntity.ok().body(userService.getUserById(userId));
    }

}
