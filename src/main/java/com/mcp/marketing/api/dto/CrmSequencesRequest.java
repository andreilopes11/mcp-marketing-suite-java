package com.mcp.marketing.api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.List;

/**
 * Request DTO for CRM Sequences generation
 * <p>
 * Generates email/SMS sequences for lead nurturing and conversion
 */
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class CrmSequencesRequest extends BaseRequest {

    private Integer sequenceLength; // Number of touchpoints
    private List<String> channels; // email, sms, whatsapp
    private String conversionGoal;

}
