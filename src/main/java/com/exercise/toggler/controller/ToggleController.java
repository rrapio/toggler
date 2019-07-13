package com.exercise.toggler.controller;

import com.exercise.toggler.assembler.ToggleResourceAssembler;
import com.exercise.toggler.model.Toggle;
import com.exercise.toggler.repository.ToggleRepository;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.ResourceSupport;
import org.springframework.hateoas.Resources;
import org.springframework.hateoas.VndErrors;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

@RestController
@Api(value = "Toggles management and search", tags = {"ToggleController"})
public class ToggleController {

    private final ToggleRepository toggleRepository;
    private final ToggleResourceAssembler assembler;
    private final ToggleExclusionController toggleExclusionController;

    ToggleController(ToggleRepository toggleRepository,
                     ToggleResourceAssembler assembler,
                     ToggleExclusionController toggleExclusionController) {
        this.toggleRepository = toggleRepository;
        this.assembler = assembler;
        this.toggleExclusionController = toggleExclusionController;
    }

    @ApiOperation(value = "View a list of available Toggles for all services and applications")
    @GetMapping("/toggles")
    public Resources<Resource<Toggle>> all() {

        List<Resource<Toggle>> toggles =
                toggleRepository.findAll().stream().map(assembler::toResource).collect(Collectors.toList());

        return new Resources<>(toggles,
                linkTo(methodOn(ToggleController.class).all()).withSelfRel());

    }

    // GetMapping toggles/{identifier} is implemented in ToggleOverrideController, so it is possible to check first for
    // the presence of a override. If there's no override, ToggleOverrideController invokes this method.
    Resource<Toggle> one(String toggleIdentifier, String serviceIdentifier, String serviceVersion) {
        if (toggleExclusionController.getByService(toggleIdentifier, serviceIdentifier, serviceVersion) != null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, toggleIdentifier + " Not Found");
        }

        return assembler.toResource(toggleRepository.findById(toggleIdentifier)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, toggleIdentifier + " Not Found")));
    }

    @ApiOperation(value = "Add a Toggle for all services and applications")
    @PostMapping("/toggles")
    ResponseEntity<Resource<Toggle>> newToggle(@RequestBody Toggle toggle) {

        if (toggleRepository.existsById(toggle.getToggleIdentifier())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, toggle.getToggleIdentifier() + " already exists");
        }

        try {
            Toggle newToggle = toggleRepository.save(toggle);

            return ResponseEntity
                    .created(linkTo(methodOn(ToggleOverrideController.class)
                            .getByService(newToggle.getToggleIdentifier(), "", "")).toUri())
                    .body(assembler.toResource(newToggle));
        } catch (Exception exc) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Error while trying to save: " +
                    exc.getMessage());
        }
    }

    @ApiOperation(value = "Activate (enable) a Toggle for all services and applications")
    @PutMapping("/toggles/{identifier}/activate")
    ResponseEntity<ResourceSupport> activate(@PathVariable String toggleIdentifier) {

        Toggle toggle = toggleRepository.findById(toggleIdentifier)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, toggleIdentifier + " Not Found"));

        boolean toggleIsInactive = !toggle.isActive();

        if (toggleIsInactive) {
            toggle.setActive(true);

            return ResponseEntity.ok(assembler.toResource(toggleRepository.save(toggle)));
        }

        return ResponseEntity
                .status(HttpStatus.METHOD_NOT_ALLOWED)
                .body(new VndErrors
                        .VndError("Method not allowed",
                        "You can't activate a toggle that is already active"));
    }

    ResponseEntity<ResourceSupport> turnOn(@PathVariable String toggleIdentifier) {

        Toggle toggle = toggleRepository.findById(toggleIdentifier)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, toggleIdentifier + " Not Found"));

        if (!toggle.isActive()) {
            throw new ResponseStatusException(
                    HttpStatus.METHOD_NOT_ALLOWED, "Toggle " + toggleIdentifier + " Not Active");
        }

        toggle.setValue(true);

        return ResponseEntity.ok(assembler.toResource(toggleRepository.save(toggle)));
    }

    ResponseEntity<ResourceSupport> turnOff(@PathVariable String toggleIdentifier) {

        Toggle toggle = toggleRepository.findById(toggleIdentifier)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, toggleIdentifier + " Not Found"));

        if (!toggle.isActive()) {
            throw new ResponseStatusException(
                    HttpStatus.METHOD_NOT_ALLOWED, "Toggle " + toggleIdentifier + " Not Active");
        }

        toggle.setValue(false);

        return ResponseEntity.ok(assembler.toResource(toggleRepository.save(toggle)));
    }

    @ApiOperation(value = "Deactivate (disable) a Toggle for all services and applications")
    @DeleteMapping("/toggles/{identifier}/deactivate")
    ResponseEntity<ResourceSupport> deactivate(@PathVariable String toggleIdentifier) {

        Toggle toggle = toggleRepository.findById(toggleIdentifier)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, toggleIdentifier + " Not Found"));

        if (toggle.isActive()) {
            toggle.setActive(false);

            return ResponseEntity.ok(assembler.toResource(toggleRepository.save(toggle)));
        }

        return ResponseEntity
                .status(HttpStatus.METHOD_NOT_ALLOWED)
                .body(new VndErrors
                        .VndError("Method not allowed",
                        "You can't deactivate a toggle that is already inactive"));
    }
}
