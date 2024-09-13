package com.gdscGCC.ghostform.Repository;

import com.gdscGCC.ghostform.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findById(String _id);
    Optional<User> findByName(String _name);
    Optional<User> findByEmail(String _email);
}
