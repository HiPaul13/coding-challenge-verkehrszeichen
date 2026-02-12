package com.hitzl.challenge.backend.observation;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ObservationRepository extends JpaRepository<ObservationEntity, Long> {
}

