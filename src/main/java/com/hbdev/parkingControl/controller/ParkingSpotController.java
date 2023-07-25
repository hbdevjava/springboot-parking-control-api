package com.hbdev.parkingControl.controller;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.data.web.SpringDataWebProperties.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hbdev.parkingControl.DTO.ParkingSpotDto;
import com.hbdev.parkingControl.model.ParkingSpotModel;
import com.hbdev.parkingControl.service.ParkingSpotService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/parkingspot")
@CrossOrigin(origins = "*", maxAge = 3600)
public class ParkingSpotController {

	@Autowired
	ParkingSpotService parkingSpotService;

	@PostMapping
	public ResponseEntity<Object> saveParkingSpot(@RequestBody @Valid ParkingSpotDto parkingSpotDto) {

		if (parkingSpotService.existsByLicensePlateCar(parkingSpotDto.getLicensePlateCar())) {
			return ResponseEntity.status(HttpStatus.CONFLICT).body("Conflict: License Plate Car is already in use!");
		}
		if (parkingSpotService.existsByParkingSpotNumber(parkingSpotDto.getParkingSpotNumber())) {
			return ResponseEntity.status(HttpStatus.CONFLICT).body("Conflict: Parking Spot is already in use!");
		}
		if (parkingSpotService.existsByApartmentAndBlock(parkingSpotDto.getApartment(), parkingSpotDto.getBlock())) {
			return ResponseEntity.status(HttpStatus.CONFLICT)
					.body("Conflict: Parking Spot already registered for this apartment/block!");
		}
		var parkingSpotModel = new ParkingSpotModel();
		// -> AQUI CONVERTE DTO EM MODEL
		BeanUtils.copyProperties(parkingSpotDto, parkingSpotModel);
		parkingSpotModel.setRegistrationDate(LocalDateTime.now(ZoneId.of("UTC")));
		return ResponseEntity.status(HttpStatus.CREATED).body(parkingSpotService.save(parkingSpotModel));

	}

	 @GetMapping
	    public ResponseEntity<List<ParkingSpotModel>> getAllParkingSpots(){
	        return ResponseEntity.status(HttpStatus.OK).body(parkingSpotService.findAll());
	    }

	@GetMapping("/{id}")
	public ResponseEntity<Object> getOneParkingSpots(@PathVariable(value = "id") UUID id) {
		Optional<ParkingSpotModel> optional = parkingSpotService.findById(id);
		if (optional.isPresent()) {
			return ResponseEntity.status(HttpStatus.OK).body(optional.get());

		} else {

			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Parking Spot Not Found");
		}
	}

	@PutMapping("/{id}")
	public ResponseEntity<Object> deleteOneParkingSpots(@PathVariable(value = "id") UUID id,
			@RequestBody @Valid ParkingSpotDto parkingSpotDto) {
		Optional<ParkingSpotModel> optional = parkingSpotService.findById(id);
		if (!optional.isPresent()) {

			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Parking Spot  Not Found");
		}
		var parkingSpotModel = optional.get();
		parkingSpotModel.setParkingSpotNumber(parkingSpotDto.getParkingSpotNumber());
		parkingSpotModel.setLicensePlateCar(parkingSpotDto.getLicensePlateCar());
		parkingSpotModel.setBrandCar(parkingSpotDto.getBrandCar());
		parkingSpotModel.setModelCar(parkingSpotDto.getModelCar());
		parkingSpotModel.setColorCar(parkingSpotDto.getColorCar());
		parkingSpotModel.setResponsableName(parkingSpotDto.getResponsableName());
		parkingSpotModel.setApartment(parkingSpotDto.getApartment());
		parkingSpotModel.setBlock(parkingSpotDto.getBlock());
		//OUTRA MANEIRA
		//CRIA UMA NOVA INSTANCIA DE PARKINGSPOTMODEL
//		var parkingSpotModel = new ParkingSpotModel();
		//FAZER A CONVERSAO DE DTO -> MODEL
//		BeanUtils.copyProperties(parkingSpotDto, parkingSpotModel);
		//SET O ID PRA PERMANECER O MESMO ID QUE VEIO DO BANCO DE DADOS
//		parkingSpotModel.setId(optional.get().getId());
		//SET A DATA PRA PERMANCER A MESMA DATA QUE VEIO DO BANCO DE DADOS
//		parkingSpotModel.setRegistrationDate(LocalTime.now(ZoneId.of("UTC")));
		return ResponseEntity.status(HttpStatus.OK).body(parkingSpotService.save(parkingSpotModel));

	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Object> upDateParkingSpots(@PathVariable(value = "id") UUID id) {
		Optional<ParkingSpotModel> optional = parkingSpotService.findById(id);
		if (optional.isPresent()) {
			parkingSpotService.deleteById(id);
			return ResponseEntity.status(HttpStatus.OK).body("Parking Spot deleted successfully");
		} else {

			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Parking Spot Id Not Found");
		}
	}

}
