package com.example.userservice.exceptions;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Error {

    private int statusId;

    private String errorMessage;
}
