package com.mcp.marketing.api.dto;

import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.List;

/**
 * Request DTO for Ads generation
 * <p>
 * Generates ads for multiple platforms (Google, Meta, LinkedIn)
 */
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class AdsRequest extends BaseRequest {

    private List<String> platforms; // google, meta, linkedin
    private String budget;
    private String duration;

}
