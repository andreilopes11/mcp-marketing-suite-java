package com.mcp.marketing.api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.List;

/**
 * Request DTO for SEO Plan generation
 * <p>
 * Generates comprehensive SEO strategy including keywords, content plan, and optimization
 */
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class SeoPlanRequest extends BaseRequest {

    private List<String> keywords;
    private String domain;
    private Integer monthlyBudget;

}
