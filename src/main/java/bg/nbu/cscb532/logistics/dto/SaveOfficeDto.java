package bg.nbu.cscb532.logistics.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.validator.constraints.Length;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class SaveOfficeDto {
    private Long id;

    @NotEmpty
    @Length(min = 2, max = 50)
    private String name;

    @NotEmpty
    @Length(min = 2, max = 50)
    private String address;

    @NotNull
    private Long cityId;
}
