package com.exercise.toggler.repository;

import com.exercise.toggler.model.ToggleExclusion;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ToggleExclusionRepository extends JpaRepository<ToggleExclusion, Long> {
    ToggleExclusion findToggleExclusionByToggleIdentifierAndServiceIdentifierAndServiceVersionAndActive
            (String toggleIdentifier, String serviceIdentifier, String serviceVersion, boolean active);

    boolean existsByToggleIdentifierAndServiceIdentifierAndServiceVersion
            (String toggleIdentifier, String serviceIdentifier, String serviceVersion);
}
