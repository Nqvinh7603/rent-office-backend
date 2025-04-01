/*******************************************************************************
 * Class        ：RichContent
 * Created date ：2025/03/31
 * Lasted date  ：2025/03/31
 * Author       ：vinhNQ2
 * Change log   ：2025/03/31：01-00 vinhNQ2 create a new
 ******************************************************************************/
package com.nqvinh.rentofficebackend.application.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

/**
 * RichContent
 *
 * @author vinhNQ2
 * @version 01-00
 * @since 01-00
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RichContent {
    @JsonProperty("richContent")
    List<List<RichContentItem>> richContent;
}