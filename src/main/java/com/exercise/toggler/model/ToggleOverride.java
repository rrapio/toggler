package com.exercise.toggler.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.Size;

@Entity
@Data
@Table(name = "toggles_overrides")
@ApiModel(description = "Details for Overrides of services and applications for Toggles.")
public class ToggleOverride {

    private @Id
    @GeneratedValue
    @ApiModelProperty(notes = "The database generated ID for this Override")
    Long id;

    @ApiModelProperty(notes = "Toggle identifier of this Override")
    @Size(min = 1)
    private @Column(name = "toggle_identifier")
    String toggleIdentifier;

    @ApiModelProperty(notes = "Service identifier overridden")
    @Size(min = 1)
    private @Column(name = "service_identifier")
    String serviceIdentifier;

    @ApiModelProperty(notes = "Service version overridden")
    private @Column(name = "service_version")
    String serviceVersion;

    @ApiModelProperty(notes = "Toggle ON/OFF of this Override")
    private boolean value;

    @ApiModelProperty(notes = "Toggle Override is active (available for use) or inactive (unavailable for use)")
    private boolean active;

    ToggleOverride() {
    }

    ToggleOverride(Long id, String toggleIdentifier, String serviceIdentifier, String serviceVersion,
                   boolean value, boolean active) {
        this.id = id;
        this.toggleIdentifier = toggleIdentifier;
        this.serviceIdentifier = serviceIdentifier;
        this.serviceVersion = serviceVersion;
        this.value = value;
        this.active = active;
    }
}
