package com.exercise.toggler.controller;

import com.exercise.toggler.assembler.ToggleExclusionResourceAssembler;
import com.exercise.toggler.model.ToggleExclusion;
import com.exercise.toggler.repository.ToggleExclusionRepository;
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
@Api(value = "Exclusions from Toggles", tags = {"ToggleExclusionController"})
public class ToggleExclusionController {

    private final ToggleExclusionRepository toggleExclusionRepository;
    private final ToggleExclusionResourceAssembler assembler;


    ToggleExclusionController(ToggleExclusionRepository toggleExclusionRepository, ToggleExclusionResourceAssembler assembler) {
        this.toggleExclusionRepository = toggleExclusionRepository;
        this.assembler = assembler;
    }

    @ApiOperation(value = "View a list of Exclusions of Services and Applications from Toggles")
    @GetMapping("/toggles-exclusions")
    public Resources<Resource<ToggleExclusion>> all() {

        List<Resource<ToggleExclusion>> togglesExclusions =
                toggleExclusionRepository.findAll().stream().map(assembler::toResource)
                        .collect(Collectors.toList());

        return new Resources<>(togglesExclusions,
                linkTo(methodOn(ToggleExclusionController.class).all()).withSelfRel());
    }

    @ApiOperation(value = "Get an Exclusion of a Service or Application from a Toggle")
    @GetMapping("/toggles-exclusions/{id}")
    public Resource<ToggleExclusion> one(@PathVariable Long id) {
        return assembler.toResource(toggleExclusionRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "id " + id + " Not Found")));
    }

    public ToggleExclusion getByService(@PathVariable String toggleIdentifier,
                                        @RequestHeader(name = "serviceIdentifier") String serviceIdentifier,
                                        @RequestHeader(name = "serviceVersion") String serviceVersion) {

        return toggleExclusionRepository
                .findToggleExclusionByToggleIdentifierAndServiceIdentifierAndServiceVersionAndActive(
                        toggleIdentifier, serviceIdentifier, serviceVersion, true);
    }


    @ApiOperation(value = "Add an Exclusion of a Service or Application from a Toggle")
    @PostMapping("/toggles-exclusions")
    ResponseEntity<Resource<ToggleExclusion>> newToggleExclusion(@RequestBody ToggleExclusion toggleExclusion) {

        if (toggleExclusionRepository
                .existsByToggleIdentifierAndServiceIdentifierAndServiceVersion(
                        toggleExclusion.getToggleIdentifier(),
                        toggleExclusion.getServiceIdentifier(),
                        toggleExclusion.getServiceVersion()
                )) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Exclusion already exists");
        }

        try {

            ToggleExclusion newToggleExclusion = toggleExclusionRepository.save(toggleExclusion);

            return ResponseEntity
                    .created(linkTo(methodOn(ToggleExclusionController.class).one(newToggleExclusion.getId())).toUri())
                    .body(assembler.toResource(newToggleExclusion));

        } catch (Exception exc) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Error while trying to save: " +
                    exc.getMessage());
        }
    }

    @ApiOperation(value = "Activate (enable) an Exclusion of a service or application from a Toggle")
    @PutMapping("/toggles-exclusions/{id}/activate")
    ResponseEntity<ResourceSupport> activate(@PathVariable Long id) {

        ToggleExclusion toggleExclusion =
                toggleExclusionRepository.findById(id)
                        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "id " + id + " Not Found"));

        boolean toggleExclusionIsInactive = !toggleExclusion.isActive();

        if (toggleExclusionIsInactive) {
            toggleExclusion.setActive(true);

            return ResponseEntity.ok(assembler.toResource(toggleExclusionRepository.save(toggleExclusion)));
        }

        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED)
                .body(new VndErrors.VndError("Method now allowed",
                        "You can't activate a toggle exclusion that is already active"));

    }

    @ApiOperation(value = "Deactivate (disable) an Exclusion of a service or application from a Toggle")
    @DeleteMapping("/toggles-exclusions/{id}/deactivate")
    ResponseEntity<ResourceSupport> deactivate(@PathVariable Long id) {

        ToggleExclusion toggleExclusion =
                toggleExclusionRepository.findById(id)
                        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "id " + id + " Not Found"));

        if (toggleExclusion.isActive()) {
            toggleExclusion.setActive(false);

            return ResponseEntity.ok(assembler.toResource(toggleExclusionRepository.save(toggleExclusion)));
        }

        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED)
                .body(new VndErrors.VndError("Method now allowed",
                        "You can't deactivate a toggle exclusion that is already inactive"));
    }
}
