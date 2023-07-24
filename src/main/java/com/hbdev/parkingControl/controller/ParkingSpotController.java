package com.hbdev.parkingControl.controller;

import java.time.LocalTime;
import java.time.ZoneId;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hbdev.parkingControl.DTO.ParkingSpotDTO;
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
	public ResponseEntity<Object> saveParkingSpot(@RequestBody @Valid   ParkingSpotDTO parkingSpotDTO) {
		if(parkingSpotService.existesByLicensePlateCar(parkingSpotDTO.getLicensePlateCar())){
			return ResponseEntity.status(HttpStatus.CONFLICT).body("Conflict: license Plane Car is already in use");
		}
		if(parkingSpotService.existesByParkingSpotNumber(parkingSpotDTO.getParkingSpotNumber())){
			return ResponseEntity.status(HttpStatus.CONFLICT).body("Conflict: Parking Spot is already in use");
		}
		if(parkingSpotService.existesByApartamentAndBlock(parkingSpotDTO.getApartament())){
			return ResponseEntity.status(HttpStatus.CONFLICT).body("Conflict: Parking Spot already registered fir this apartament/blockin");
		}
		var parkingSpotModel = new ParkingSpotModel();
		//-> AQUI CONVERTE DTO EM MODEL 
		BeanUtils.copyProperties(parkingSpotDTO, parkingSpotModel);
		parkingSpotModel.setRegistrationDate(LocalTime.now(ZoneId.of("UTC")));
		return ResponseEntity.status(HttpStatus.CREATED).body(parkingSpotService.save(parkingSpotModel));
		
	}
	
	
	
	
	
	

}
