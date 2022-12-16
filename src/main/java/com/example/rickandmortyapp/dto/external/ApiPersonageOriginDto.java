package com.example.rickandmortyapp.dto.external;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApiPersonageOriginDto {
    private String name;
    private String url;
}
