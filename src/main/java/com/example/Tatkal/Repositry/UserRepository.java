package com.example.Tatkal.Repositry;

import com.example.Tatkal.Entity.Booking;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<Booking,Long> {

}
