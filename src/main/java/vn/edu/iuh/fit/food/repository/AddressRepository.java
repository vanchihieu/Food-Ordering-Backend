package vn.edu.iuh.fit.food.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import vn.edu.iuh.fit.food.model.Address;

public interface AddressRepository extends JpaRepository<Address, Long> {

}