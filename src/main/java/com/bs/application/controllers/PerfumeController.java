package com.bs.application.controllers;

import com.bs.application.dtos.PerfumeEntityDto;
import com.bs.application.dtos.Response;
import com.bs.application.services.PerfumeService;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.helpers.Util;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Objects;
import java.util.Optional;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/perfume")
@PreAuthorize("hasAnyRole('ADMIN', 'USER')")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class PerfumeController {
    private final PerfumeService perfumeService;

    @GetMapping("/all")
    @PreAuthorize("hasAnyAuthority('admin:read', 'user:read')")
    ResponseEntity<Optional<Response>> getAllPerfumes() {
        log.info("Request received to get all the perfumes");
        return ResponseEntity.ok(perfumeService.getAllPerfumes());
    }

    @GetMapping("/item/{itemNumber}")
    @PreAuthorize("hasAnyAuthority('admin:read', 'user:read')")
    ResponseEntity<Optional<Response>> getPerfumeByItemNumber(@PathVariable("itemNumber") String itemNumber) {
        log.info("Request received to get the perfume by itemNumber: {}", itemNumber);
        return ResponseEntity.ok(perfumeService.getPerfumeByItemNumber(itemNumber));
    }

    @GetMapping("/upc/{upc}")
    @PreAuthorize("hasAnyAuthority('admin:read', 'user:read')")
    ResponseEntity<Optional<Response>> getPerfumeByUpc(@PathVariable("upc") String upc) {
        log.info("Request received to get the perfume by upc: {}", upc);
        return ResponseEntity.ok(perfumeService.getPerfumeByUpc(upc));
    }

    @GetMapping("/country/{countryOfOrigin}")
    @PreAuthorize("hasAnyAuthority('admin:read', 'user:read')")
    ResponseEntity<Optional<Response>> getPerfumeByCountryOfOrigin(@PathVariable("countryOfOrigin") String countryOfOrigin) {
        log.info("Request received to get the perfume by countryOfOrigin: {}", countryOfOrigin);
        return ResponseEntity.ok(perfumeService.getPerfumeByCountryOfOrigin(countryOfOrigin));
    }

    @GetMapping("/brand/{brand}")
    @PreAuthorize("hasAnyAuthority('admin:read', 'user:read')")
    ResponseEntity<Optional<Response>> getPerfumesByBrand(@PathVariable("brand") String brand) {
        log.info("Request received to get the perfume by brand: {}", brand);
        return ResponseEntity.ok(perfumeService.getPerfumesByBrand(brand));
    }

    @GetMapping("/gender/{gender}")
    @PreAuthorize("hasAnyAuthority('admin:read', 'user:read')")
    ResponseEntity<Optional<Response>> getPerfumesByGender(@PathVariable("gender") String gender) {
        log.info("Request received to get the perfume by gender: {}", gender);
        return ResponseEntity.ok(perfumeService.getPerfumesByGender(gender));
    }

    @GetMapping("/price/{startPrice}/{endPrice}")
    @PreAuthorize("hasAnyAuthority('admin:read', 'user:read')")
    ResponseEntity<Optional<Response>> getPerfumesBetweenStartAndEndDate(
            @PathVariable("startPrice") Double startPrice,
            @PathVariable("endPrice") Double endPrice
    ) {
        log.info("Request received to get the perfume by price range: {}-{}", startPrice, endPrice);
        return ResponseEntity.ok(perfumeService.getPerfumesBetweenStartAndEndPrice(startPrice, endPrice));
    }

    @PostMapping
    @PreAuthorize("hasAuthority('admin:write')")
    ResponseEntity<Optional<Response>> registerAndUpdatePerfumeDetails(@RequestBody PerfumeEntityDto perfumeEntityDto) {
        log.info("Request received to add the perfume details: {}", perfumeEntityDto.toString());
        return ResponseEntity.ok(perfumeService.registerAndUpdatePerfumeDetails(perfumeEntityDto));
    }

    @PostMapping(value = "/bulk_upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasAuthority('admin:write')")
    ResponseEntity<Response> postPerfumeFromExcel(@RequestBody MultipartFile file) {
        if (file == null || file.isEmpty()) {
            return ResponseEntity.badRequest().body(Response.builder().status("BAD REQUEST").error("File provide file").build());
        } else if (!file.isEmpty() || !Objects.requireNonNull(file.getContentType()).equalsIgnoreCase("text/csv")) {
            return ResponseEntity.badRequest().body(Response.builder().status("BAD REQUEST").error("File should be in csv format").build());
        }
        log.info("Request Received to post perfume form excel");
        perfumeService.bulkUploadPerfumes(file);
            return null;
    }
}
