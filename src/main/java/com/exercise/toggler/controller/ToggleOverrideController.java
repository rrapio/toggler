package com.exercise.toggler.controller;

import com.exercise.toggler.assembler.ToggleOverrideResourceAssembler;
import com.exercise.toggler.model.ToggleOverride;
import com.exercise.toggler.repository.ToggleOverrideRepository;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
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
@Api(value = "Overrides for Toggles", tags = {"ToggleOverrideController"})
public class ToggleOverrideController {

    private final ToggleOverrideRepository toggleOverrideRepository;
    private final ToggleOverrideResourceAssembler assembler;
    private final ToggleController toggleController;

    ToggleOverrideController(ToggleOverrideRepository toggleOverrideRepository,
                             ToggleOverrideResourceAssembler assembler,
                             ToggleController toggleController) {
        this.toggleOverrideRepository = toggleOverrideRepository;
        this.assembler = assembler;
        this.toggleController = toggleController;
    }

    @ApiOperation(value = "View a list of available Overrides of services and applications for Toggles")
    @GetMapping("/toggles-overrides")
    public Resources<Resource<ToggleOverride>> all() {

        List<Resource<ToggleOverride>> togglesOverrides = toggleOverrideRepository.findAll()
                .stream()
                .map(assembler::toResource)
                .collect(Collectors.toList());

        return new Resources<>(togglesOverrides,
                linkTo(methodOn(ToggleOverrideController.class).all()).withSelfRel());

    }

    @ApiOperation(value = "Get an Override of service or application for Toggle")
    @GetMapping("/toggles-overrides/{id}")
    public Resource<ToggleOverride> one(@PathVariable Long id) {
        return assembler.toResource(toggleOverrideRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "id " + id + " Not Found")));

    }

    @ApiOperation(value = "Add a new Override of service or application for a Toggle")
    @PostMapping("/toggles-overrides")
    ResponseEntity<Resource<ToggleOverride>> newToggleOverride(@RequestBody ToggleOverride toggleOverride) {

        if (toggleOverrideRepository
                .existsByToggleIdentifierAndServiceIdentifierAndServiceVersion(
                        toggleOverride.getToggleIdentifier(),
                        toggleOverride.getServiceIdentifier(),
                        toggleOverride.getServiceVersion()
                )) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Override already exists");
        }

        try {

            ToggleOverride newToggleOverride = toggleOverrideRepository.save(toggleOverride);


            return ResponseEntity
                    .created(linkTo(methodOn(ToggleOverrideController.class).one(newToggleOverride.getId())).toUri())
                    .body(assembler.toResource(newToggleOverride));

        } catch (Exception exc) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Error while trying to save: " +
                    exc.getMessage());
        }
    }

    @ApiOperation(value = "Deactivate (disable) a Override of service or application for a Toggle")
    @DeleteMapping("/toggles-overrides/{id}/deactivate")
    ResponseEntity<ResourceSupport> deactivate(@PathVariable Long id) {

        ToggleOverride toggleOverride =
                toggleOverrideRepository.findById(id)
                        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "id " + id + " Not Found"));

        if (toggleOverride.isActive()) {
            toggleOverride.setActive(false);

            return ResponseEntity.ok(assembler.toResource(toggleOverrideRepository.save(toggleOverride)));
        }

        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED)
                .body(new VndErrors.VndError("Method not allowed",
                        "You can't deactivate a toggle override that is already inactive"));
    }

    @ApiOperation(value = "Activate (enable) a Override of service or application for a Toggle")
    @PutMapping("/toggles-overrides/{id}/activate")
    ResponseEntity<ResourceSupport> activate(Long id) {
        ToggleOverride toggleOverride =
                toggleOverrideRepository.findById(id)
                        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "id " + id + " Not Found"));

        boolean toggleOverrideIsInactive = !toggleOverride.isActive();

        if (toggleOverrideIsInactive) {
            toggleOverride.setActive(true);

            return ResponseEntity.ok(assembler.toResource(toggleOverrideRepository.save(toggleOverride)));
        }

        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED)
                .body(new VndErrors.VndError("Method not allowed",
                        "You can't activate a toggle override that is already active"));
    }


    @ApiOperation(value = "Turns a Toggle ON, checking first for the presence of an Override for the informed service or application",
            tags = "ToggleController")
    @PutMapping("/toggles/{toggleIdentifier}/on")
    ResponseEntity<?> turnOn(@PathVariable String toggleIdentifier,
                             @RequestHeader(name = "serviceIdentifier") String serviceIdentifier,
                             @RequestHeader(name = "serviceVersion") String serviceVersion) {

        ToggleOverride toggleOverride =
                toggleOverrideRepository
                        .findToggleOverrideByToggleIdentifierAndServiceIdentifierAndServiceVersionAndActive(
                                toggleIdentifier, serviceIdentifier, serviceVersion, true);

        boolean toggleOverrideNotFound = (toggleOverride == null);

        if (toggleOverrideNotFound) {
            return toggleController.turnOn(toggleIdentifier);
        }

        toggleOverride.setValue(true);

        return ResponseEntity.ok(assembler.toResource(toggleOverrideRepository.save(toggleOverride)));
    }

    @ApiOperation(value = "Turns a Toggle OFF, checking first for the presence of an Override for the informed service or application",
            tags = "ToggleController")
    @PutMapping("/toggles/{toggleIdentifier}/off")
    ResponseEntity<?> turnOff(@PathVariable String toggleIdentifier,
                              @RequestHeader(name = "serviceIdentifier") String serviceIdentifier,
                              @RequestHeader(name = "serviceVersion") String serviceVersion) {

        ToggleOverride toggleOverride =
                toggleOverrideRepository
                        .findToggleOverrideByToggleIdentifierAndServiceIdentifierAndServiceVersionAndActive(
                                toggleIdentifier, serviceIdentifier, serviceVersion, true);

        boolean toggleOverrideNotFound = (toggleOverride == null);

        if (toggleOverrideNotFound) {
            return toggleController.turnOff(toggleIdentifier);
        }

        toggleOverride.setValue(true);

        return ResponseEntity.ok(assembler.toResource(toggleOverrideRepository.save(toggleOverride)));
    }

    @ApiOperation(value = "Get a Toggle, checking first for the presence of an Override for the informed service or application",
            tags = "ToggleController")
    @GetMapping("/toggles/{toggleIdentifier}")
    public Resource<?> getByService(@PathVariable String toggleIdentifier,
                                    @RequestHeader(name = "serviceIdentifier") String serviceIdentifier,
                                    @RequestHeader(name = "serviceVersion") String serviceVersion) {

        ToggleOverride toggleOverride =
                toggleOverrideRepository
                        .findToggleOverrideByToggleIdentifierAndServiceIdentifierAndServiceVersionAndActive(
                                toggleIdentifier, serviceIdentifier, serviceVersion, true);

        boolean toggleOverrideNotFound = (toggleOverride == null);

        if (toggleOverrideNotFound) {
            return toggleController.one(toggleIdentifier, serviceIdentifier, serviceVersion);
        }

        return assembler.toResource(toggleOverride);
    }


}
