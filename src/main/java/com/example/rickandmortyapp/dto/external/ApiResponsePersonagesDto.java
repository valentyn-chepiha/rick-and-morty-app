package com.example.rickandmortyapp.dto.external;

import lombok.Data;

@Data
public class ApiResponsePersonagesDto {
    private ApiInfoDto info;
    private ApiPersonageDto[] results;
}
