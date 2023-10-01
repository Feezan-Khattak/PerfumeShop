package com.bs.application.services;

import com.bs.application.dtos.PerfumeEntityDto;
import com.bs.application.dtos.Response;
import com.bs.application.entities.Perfume;
import com.bs.application.repos.PerfumeRepo;
import com.bs.application.utils.ResponseUtil;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.stereotype.Service;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Slf4j
@AllArgsConstructor
public class PerfumeService {
    private final PerfumeRepo perfumeRepo;
    private final ModelMapper modelMapper;
    private final ResponseUtil responseUtil;

    public Optional<Response> getAllPerfumes() {
        Response response;
        List<Perfume> perfumes = perfumeRepo.findAll();
        if (!perfumes.isEmpty()) {
            log.info("collecting all perfumes details...");
            Type type = new TypeToken<List<PerfumeEntityDto>>() {
            }.getType();
            List<List<PerfumeEntityDto>> allPerfumes = modelMapper.map(perfumes, type);
            response = responseUtil.generateSuccessResponse(allPerfumes);
        } else {
            log.info("No perfume found in Database");
            response = responseUtil.generateFailureResponse("No perfume found in database");
        }
        return Optional.of(response);
    }

    public Optional<Response> getPerfumeByItemNumber(String itemNumber) {
        Response response;
        Optional<Perfume> perfume = perfumeRepo.findByItemNumber(itemNumber);
        if (perfume.isPresent()) {
            log.info("Collecting Perfume based on Item number: {}", itemNumber);
            Type type = new TypeToken<PerfumeEntityDto>() {
            }.getType();
            PerfumeEntityDto perfumeDto = modelMapper.map(perfume.get(), type);
            response = responseUtil.generateSuccessResponse(perfumeDto);
        } else {
            log.info("No perfume found based on item number: {}", itemNumber);
            response = responseUtil.generateFailureResponse("No perfume found based on item number: " + itemNumber);
        }
        return Optional.of(response);
    }

    public Optional<Response> getPerfumeByUpc(String upc) {
        Response response;
        Optional<Perfume> perfume = perfumeRepo.findByUpc(upc);
        if (perfume.isPresent()) {
            log.info("Collecting Perfume based on upc: {}", upc);
            Type type = new TypeToken<PerfumeEntityDto>() {
            }.getType();
            PerfumeEntityDto perfumeDto = modelMapper.map(perfume.get(), type);
            response = responseUtil.generateSuccessResponse(perfumeDto);
        } else {
            log.info("No perfume found based on upc: {}", upc);
            response = responseUtil.generateFailureResponse("No perfume found based on upc: " + upc);
        }
        return Optional.of(response);
    }

    public Optional<Response> getPerfumeByCountryOfOrigin(String countryOfOrigin) {
        Response response;
        Optional<List<Perfume>> perfume = perfumeRepo.findByCountryOfOrigin(countryOfOrigin);
        if (perfume.isPresent()) {
            log.info("Collecting Perfume based on countryOfOrigin: {}", countryOfOrigin);
            Type type = new TypeToken<List<PerfumeEntityDto>>() {
            }.getType();
            List<PerfumeEntityDto> perfumeDto = modelMapper.map(perfume.get(), type);
            response = responseUtil.generateSuccessResponse(perfumeDto);
        } else {
            log.info("No perfume found based on countryOfOrigin: {}", countryOfOrigin);
            response = responseUtil.generateFailureResponse("No perfume found based on countryOfOrigin: " + countryOfOrigin);
        }
        return Optional.of(response);
    }

    public Optional<Response> getPerfumesByBrand(String brand) {
        Response response;
        Optional<List<Perfume>> perfume = perfumeRepo.findByBrandName(brand);
        if (perfume.isPresent()) {
            log.info("Collecting Perfume based on brand: {}", brand);
            Type type = new TypeToken<List<PerfumeEntityDto>>() {
            }.getType();
            List<PerfumeEntityDto> perfumeDto = modelMapper.map(perfume.get(), type);
            response = responseUtil.generateSuccessResponse(perfumeDto);
        } else {
            log.info("No perfume found based on brand: {}", brand);
            response = responseUtil.generateFailureResponse("No perfume found based on brand: " + brand);
        }
        return Optional.of(response);
    }

    public Optional<Response> getPerfumesByGender(String gender) {
        Response response;
        Optional<List<Perfume>> perfume = perfumeRepo.findByGender(gender);
        if (perfume.isPresent()) {
            log.info("Collecting Perfume based on gender: {}", gender);
            Type type = new TypeToken<List<PerfumeEntityDto>>() {
            }.getType();
            List<PerfumeEntityDto> perfumeDto = modelMapper.map(perfume.get(), type);
            response = responseUtil.generateSuccessResponse(perfumeDto);
        } else {
            log.info("No perfume found based on gender: {}", gender);
            response = responseUtil.generateFailureResponse("No perfume found based on gender: " + gender);
        }
        return Optional.of(response);
    }

    public Optional<Response> getPerfumesBetweenStartAndEndPrice(Double startPrice, Double endPrice) {
        Response response;
        Optional<List<Perfume>> perfume = perfumeRepo.findByPriceBetween(startPrice, endPrice);
        if (perfume.isPresent()) {
            log.info("Collecting Perfume based on start: {} and end price: {}", startPrice, endPrice);
            Type type = new TypeToken<List<PerfumeEntityDto>>() {
            }.getType();
            List<PerfumeEntityDto> perfumeDto = modelMapper.map(perfume.get(), type);
            response = responseUtil.generateSuccessResponse(perfumeDto);
        } else {
            log.info("No perfume found based on start: {} and end price: {}", startPrice, endPrice);
            response = responseUtil.generateFailureResponse("No perfume found based on start: " + startPrice + " and end price: " + endPrice);
        }
        return Optional.of(response);
    }

    @Transactional
    public Optional<Response> registerAndUpdatePerfumeDetails(PerfumeEntityDto perfumeEntityDto) {
        Response response;
        Perfume savedPerfume;
        try {
            Optional<Perfume> perfume = perfumeRepo.findByItemNumberAndUpc(perfumeEntityDto.getItemNumber(), perfumeEntityDto.getUpc());
            if (perfume.isPresent()) {
                log.info("Perfume already present based on itemNumber {} and upc {}", perfumeEntityDto.getItemNumber(), perfumeEntityDto.getUpc());
                savedPerfume = updatePerfumeDetails(perfumeEntityDto, perfume.get());
            } else {
                log.info("Saving perfume details...");
                savedPerfume = savePerfumeDetails(perfumeEntityDto);
            }
            Type type = new TypeToken<PerfumeEntityDto>() {
            }.getType();
            PerfumeEntityDto mappedPerfume = modelMapper.map(savedPerfume, PerfumeEntityDto.class);
            response = responseUtil.generateSuccessResponse(mappedPerfume);
        } catch (Exception er) {
            er.printStackTrace();
            log.info("Failed to save and update the perfume Details");
            response = responseUtil.generateFailureResponse("Failed to save and update the perfume Details");
        }
        return Optional.of(response);
    }

    private Perfume updatePerfumeDetails(PerfumeEntityDto perfumeEntityDto, Perfume perfume) {
        perfumeEntityDto.setId(perfume.getId());
        perfumeEntityDto.getBrand().setId(perfume.getBrand().getId());
        perfumeEntityDto.setItemNumber(perfume.getItemNumber());
        perfumeEntityDto.setUpc(perfume.getUpc());
        perfumeEntityDto.setUuid(perfume.getUuid());
        Perfume mappedPerfume = modelMapper.map(perfumeEntityDto, Perfume.class);
        return perfumeRepo.save(mappedPerfume);
    }

    private Perfume savePerfumeDetails(PerfumeEntityDto perfumeEntityDto) {
        perfumeEntityDto.setUuid(UUID.randomUUID().toString());
        Perfume mappedPerfume = modelMapper.map(perfumeEntityDto, Perfume.class);
        return perfumeRepo.save(mappedPerfume);
    }
}
