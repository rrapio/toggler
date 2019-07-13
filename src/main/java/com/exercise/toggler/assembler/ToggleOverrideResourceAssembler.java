package com.exercise.toggler.assembler;

import com.exercise.toggler.model.ToggleOverride;
import com.exercise.toggler.controller.ToggleOverrideController;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.ResourceAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

@Component
public class ToggleOverrideResourceAssembler implements ResourceAssembler<ToggleOverride, Resource<ToggleOverride>> {

    @Override
    public Resource<ToggleOverride> toResource(ToggleOverride toggleOverride) {

        return new Resource<>(toggleOverride,
                linkTo(methodOn(ToggleOverrideController.class).one(toggleOverride.getId())).withSelfRel(),
                linkTo(methodOn(ToggleOverrideController.class).all()).withRel("toggles-overrides")
        );

    }
}
