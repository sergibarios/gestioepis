package gestioepis.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SearchResultDTO {
    private String title;
    private String subtitle;
    private String type;
    private String url;
}
