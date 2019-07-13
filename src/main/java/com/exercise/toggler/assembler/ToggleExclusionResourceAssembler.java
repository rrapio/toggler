package com.exercise.toggler.assembler;

import com.exercise.toggler.controller.ToggleExclusionController;
import com.exercise.toggler.model.ToggleExclusion;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.ResourceAssembler;
import org.springframework.hateoas.mvc.ControllerLinkBuilder;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

@Component
public class ToggleExclusionResourceAssembler implements ResourceAssembler<ToggleExclusion, Resource<ToggleExclusion>> {

    @Override
    public Resource<ToggleExclusion> toResource(ToggleExclusion toggleExclusion) {

        return new Resource<>(toggleExclusion,
                ControllerLinkBuilder.linkTo(methodOn(ToggleExclusionController.class).one(toggleExclusion.getId())).withSelfRel(),
                linkTo(methodOn(ToggleExclusionController.class).all()).withRel("toggles-exclusion")
        );

    }

}
