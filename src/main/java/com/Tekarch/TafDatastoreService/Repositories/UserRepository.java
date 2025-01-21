package com.Tekarch.TafDatastoreService.Repositories;

import com.Tekarch.TafDatastoreService.Models.Users;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<Users,Long> {
}
