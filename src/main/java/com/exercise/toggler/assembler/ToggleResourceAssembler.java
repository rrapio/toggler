package com.exercise.toggler.assembler;

import com.exercise.toggler.controller.ToggleOverrideController;
import com.exercise.toggler.model.Toggle;
import com.exercise.toggler.controller.ToggleController;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.ResourceAssembler;
import org.springframework.hateoas.mvc.ControllerLinkBuilder;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

@Component
public class ToggleResourceAssembler implements ResourceAssembler<Toggle, Resource<Toggle>> {

    @Override
    public Resource<Toggle> toResource(Toggle toggle) {

        // Unconditional links to single-item resource and aggregate root

        return new Resource<>(toggle,
                ControllerLinkBuilder.linkTo(methodOn(ToggleOverrideController.class)
                        .getByService(toggle.getToggleIdentifier(), "", "")).withSelfRel(),
                linkTo(methodOn(ToggleController.class).all()).withRel("toggles")
        );
    }
}
