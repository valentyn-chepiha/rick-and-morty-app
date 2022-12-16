package com.example.rickandmortyapp.dto.external;

import lombok.Data;

@Data
public class ApiResponseLocationsDto {
    private ApiInfoDto info;
    private ApiLocationDto[] results;
}
