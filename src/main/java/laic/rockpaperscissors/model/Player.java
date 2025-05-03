package laic.rockpaperscissors.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import laic.rockpaperscissors.model.enums.Move;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Player {
    private String name;
    private Move move;
}
