package com.Tekarch.TafDatastoreService.Controllers;

import com.Tekarch.TafDatastoreService.Models.Users;
import com.Tekarch.TafDatastoreService.Repositories.UserRepository;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@Data
@RequestMapping("/users")
public class UsersController {

    @Autowired
    private final UserRepository userRepository;

    @PostMapping("/register")
    public ResponseEntity<Users> register(@RequestBody Users user){
        try {
            Users createUser = userRepository.save(user);
            return new ResponseEntity<>(createUser, HttpStatus.CREATED);
        } catch (Exception e) {
            throw new DatastoreException("Error creating user",e);
        }
    }

    @GetMapping("/{userId}")
    public ResponseEntity<Optional<Users>> getUserDetails(@PathVariable Long userId) {
        Optional<Users> user=userRepository.findById(userId);
        if (user.isPresent()) return new ResponseEntity<>(user, HttpStatus.OK);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
    }
//

    @GetMapping
    public ResponseEntity<List<Users>> getAllUsers()
    {

        return new ResponseEntity<>(userRepository.findAll(),HttpStatus.OK);
    }

    @PutMapping("/{userId}")
    public ResponseEntity<Users> updateUser(@PathVariable Long userId, @RequestBody Users user){
        Optional<Users> existinguser=userRepository.findById(userId);
        if(existinguser.isPresent()){
            user.setId(userId);
            return new ResponseEntity<>(userRepository.save(user),HttpStatus.OK);
        }
      else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<String> deleteUser(@PathVariable Long userId) {
        if(!userRepository.findById(userId).equals(0L)){
            userRepository.deleteById(userId);
            return ResponseEntity.ok("User Deleted Successfully");
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");

    }

}
