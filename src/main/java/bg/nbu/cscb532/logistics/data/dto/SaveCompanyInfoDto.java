package bg.nbu.cscb532.logistics.data.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class SaveCompanyInfoDto {
    @NotNull
    @Size(min = 3, max = 50)
    private String companyName;

    @NotNull
    @Size(min = 3, max = 50)
    private String phoneNumber;
}
