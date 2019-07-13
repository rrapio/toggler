package com.exercise.toggler.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.Size;

@Entity
@Data
@Table(name = "toggles_exclusions")
@ApiModel(description = "Details for Exclusions of services and applications from Toggles.")
public class ToggleExclusion {

    private @Id
    @GeneratedValue
    @ApiModelProperty(notes = "The database generated ID for this Exclusion")
    Long id;

    @ApiModelProperty(notes = "Toggle identifier of this Exclusion")
    @Size(min = 1)
    private @Column(name = "toggle_identifier")
    String toggleIdentifier;

    @ApiModelProperty(notes = "Service identifier excluded")
    @Size(min = 1)
    private @Column(name = "service_identifier")
    String serviceIdentifier;

    @ApiModelProperty(notes = "Service version excluded")
    private @Column(name = "service_version")
    String serviceVersion;

    @ApiModelProperty(notes = "Exclusion from a Toggle is active (available for use) or inactive (unavailable for use)")
    private boolean active;

    ToggleExclusion() {
    }

    ToggleExclusion(Long id, String toggleIdentifier, String serviceIdentifier, String serviceVersion, boolean active) {
        this.id = id;
        this.toggleIdentifier = toggleIdentifier;
        this.serviceIdentifier = serviceIdentifier;
        this.serviceVersion = serviceVersion;
        this.active = active;
    }

}
