package com.ssss.dto;


import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class ShowExceptionDto {

    int status;

    String message;

}
