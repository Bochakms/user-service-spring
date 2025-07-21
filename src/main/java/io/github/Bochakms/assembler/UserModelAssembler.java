package io.github.Bochakms.assembler;

import io.github.Bochakms.dto.UserResponse;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class UserModelAssembler implements RepresentationModelAssembler<UserResponse, EntityModel<UserResponse>> {

    @Override
    public EntityModel<UserResponse> toModel(UserResponse user) {
        return EntityModel.of(user,
            
            linkTo(methodOn(io.github.Bochakms.controller.UserController.class)
                .getUserById(user.getId())).withSelfRel(),

            linkTo(methodOn(io.github.Bochakms.controller.UserController.class)
                .getAllUsers()).withRel("users"),

            linkTo(methodOn(io.github.Bochakms.controller.UserController.class)
                .updateUser(user.getId(), null)).withRel("update"),

            linkTo(methodOn(io.github.Bochakms.controller.UserController.class)
                .deleteUser(user.getId())).withRel("delete")
        );
    }
}