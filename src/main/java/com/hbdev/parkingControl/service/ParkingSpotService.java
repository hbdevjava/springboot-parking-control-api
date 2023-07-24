package com.hbdev.parkingControl.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hbdev.parkingControl.model.ParkingSpotModel;
import com.hbdev.parkingControl.repository.ParkingSpotRepository;

import jakarta.transaction.Transactional;

@Service
public class ParkingSpotService {

	@Autowired
	ParkingSpotRepository parkingSpotRepository;
	// final ParkingSpotRepository parkingSpotRepository;
	// public ParkingSpotRepository(ParkingSpotRepository parkingSpotRepository) {
	// this.parkingSpotRepository = parkingSpotRepository;
	// }

	@Transactional
	public ParkingSpotModel save(ParkingSpotModel parkingSpotModel) {

		return parkingSpotRepository.save(parkingSpotModel);
	}

	public boolean existesByLicensePlateCar(String licensePlateCar) {
		
		return false;
	}
	
	public boolean existesByParkingSpotNumber(String parkingSpotNumber) {

		return false;
	}

	public boolean existesByApartamentAndBlock(String apartament) {
		
		return false;
	}


}
