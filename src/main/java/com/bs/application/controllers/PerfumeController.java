package com.bs.application.controllers;

import com.bs.application.dtos.PerfumeEntityDto;
import com.bs.application.dtos.Response;
import com.bs.application.services.PerfumeService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("api/perfume")
@CrossOrigin(origins = "*", allowedHeaders = "*")
@AllArgsConstructor
@Slf4j
public class PerfumeController {
    private final PerfumeService perfumeService;

    @GetMapping("/all")
    ResponseEntity<Optional<Response>> getAllPerfumes() {
        log.info("Request received to get all the perfumes");
        return ResponseEntity.ok(perfumeService.getAllPerfumes());
    }

    @GetMapping("/item/{itemNumber}")
    ResponseEntity<Optional<Response>> getPerfumeByItemNumber(@PathVariable("itemNumber") String itemNumber) {
        log.info("Request received to get the perfume by itemNumber: {}", itemNumber);
        return ResponseEntity.ok(perfumeService.getPerfumeByItemNumber(itemNumber));
    }

    @GetMapping("/upc/{upc}")
    ResponseEntity<Optional<Response>> getPerfumeByUpc(@PathVariable("upc") String upc) {
        log.info("Request received to get the perfume by upc: {}", upc);
        return ResponseEntity.ok(perfumeService.getPerfumeByUpc(upc));
    }

    @GetMapping("/country/{countryOfOrigin}")
    ResponseEntity<Optional<Response>> getPerfumeByCountryOfOrigin(@PathVariable("countryOfOrigin") String countryOfOrigin) {
        log.info("Request received to get the perfume by countryOfOrigin: {}", countryOfOrigin);
        return ResponseEntity.ok(perfumeService.getPerfumeByCountryOfOrigin(countryOfOrigin));
    }

    @GetMapping("/brand/{brand}")
    ResponseEntity<Optional<Response>> getPerfumesByBrand(@PathVariable("brand") String brand) {
        log.info("Request received to get the perfume by brand: {}", brand);
        return ResponseEntity.ok(perfumeService.getPerfumesByBrand(brand));
    }

    @GetMapping("/gender/{gender}")
    ResponseEntity<Optional<Response>> getPerfumesByGender(@PathVariable("gender") String gender) {
        log.info("Request received to get the perfume by gender: {}", gender);
        return ResponseEntity.ok(perfumeService.getPerfumesByGender(gender));
    }

    @GetMapping("/price/{startPrice}/{endPrice}")
    ResponseEntity<Optional<Response>> getPerfumesBetweenStartAndEndDate(
            @PathVariable("startPrice") Double startPrice,
            @PathVariable("endPrice") Double endPrice
    ) {
        log.info("Request received to get the perfume by price range: {}-{}", startPrice, endPrice);
        return ResponseEntity.ok(perfumeService.getPerfumesBetweenStartAndEndPrice(startPrice, endPrice));
    }

    @PostMapping
    ResponseEntity<Optional<Response>> registerAndUpdatePerfumeDetails(@RequestBody PerfumeEntityDto perfumeEntityDto) {
        log.info("Request received to add the perfume details: {}", perfumeEntityDto.toString());
        return ResponseEntity.ok(perfumeService.registerAndUpdatePerfumeDetails(perfumeEntityDto));
    }
}
