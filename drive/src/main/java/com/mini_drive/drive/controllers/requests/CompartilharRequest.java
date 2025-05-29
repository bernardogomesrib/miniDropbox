package com.mini_drive.drive.controllers.requests;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CompartilharRequest {
    @NotNull
    private String id;
    @NotNull
    @Email
    private String email;
}
