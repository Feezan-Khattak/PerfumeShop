package com.bs.application.repos;

import com.bs.application.entities.Perfume;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PerfumeRepo extends JpaRepository<Perfume, Long> {

    Optional<Perfume> findByItemNumber(String itemNumber);

    Optional<Perfume> findByUpc(String upc);

    Optional<List<Perfume>> findByCountryOfOrigin(String countryOfOrigin);

    Optional<List<Perfume>> findByBrandName(String brandName);

    Optional<List<Perfume>> findByGender(String gender);

    Optional<List<Perfume>> findByPriceBetween(Double startPrice, Double endDate);

    Optional<Perfume> findByItemNumberAndUpc(String itemNumber, String upc);
}
