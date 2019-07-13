package com.exercise.toggler.repository;

import com.exercise.toggler.model.ToggleOverride;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ToggleOverrideRepository extends JpaRepository<ToggleOverride, Long> {

    ToggleOverride findToggleOverrideByToggleIdentifierAndServiceIdentifierAndServiceVersionAndActive
            (String toggleIdentifier, String serviceIdentifier, String serviceVersion, boolean active);

    boolean existsByToggleIdentifierAndServiceIdentifierAndServiceVersion
            (String toggleIdentifier, String serviceIdentifier, String serviceVersion);
}
