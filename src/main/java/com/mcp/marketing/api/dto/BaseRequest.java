package com.mcp.marketing.api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class BaseRequest {

    @NotBlank(message = "product is required")
    private String product;

    @NotBlank(message = "audience is required")
    private String audience;

    @NotBlank(message = "brand_voice is required")
    private String brandVoice;

    @NotBlank(message = "goals is required")
    private String goals;

    @NotBlank(message = "language is required")
    @Pattern(regexp = "(?i)pt-br|en-us|es-es", message = "language must be 'pt-BR' or 'en-US' or 'es-ES'")
    private String language;

}
