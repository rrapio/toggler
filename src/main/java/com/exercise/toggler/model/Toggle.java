package com.exercise.toggler.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Data
@Table(name = "toggles")
@ApiModel(description = "Details for the Toggles for all services and applications.")
public class Toggle {

    private @Id
    @Column(name = "toggle_identifier")
    @Length(min = 1)
    @ApiModelProperty(notes = "Identifier for the Toggle")
    String toggleIdentifier;

    @ApiModelProperty(notes = "Toggle ON/OFF")
    private boolean value;

    @ApiModelProperty(notes = "Toggle active (available for use) or inactive (unavailable for use)")
    private boolean active;

    public Toggle() {
    }

    Toggle(String toggleIdentifier, boolean value, boolean active) {
        this.toggleIdentifier = toggleIdentifier;
        this.value = value;
        this.active = active;
    }
}
