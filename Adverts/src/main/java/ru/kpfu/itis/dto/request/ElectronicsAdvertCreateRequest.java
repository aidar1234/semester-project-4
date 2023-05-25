package ru.kpfu.itis.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;
import ru.kpfu.itis.validation.annotation.ElectronicsAdvertFilesConstraint;
import ru.kpfu.itis.validation.annotation.ElectronicsAdvertPriceConstraint;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@ElectronicsAdvertPriceConstraint
@ElectronicsAdvertFilesConstraint
public class ElectronicsAdvertCreateRequest {

    private MultipartFile[] files;

    @NotBlank(message = "Не может быть пустым")
    @Size(max = 64, message = "Максимальная длина названия 32")
    private String name;

    @NotBlank
    @Min(value = 0, message = "Не может быть меньше нуля")
    private String price;

    @NotBlank(message = "Не может быть пустым")
    @Size(max = 12, message = "Максимальная длина номера 12")
    @Pattern(regexp = "(\\+7\\d{10})|(8\\d{10})", message = "Не соответсвует номеру")
    private String phone;

    @NotBlank(message = "Не может быть пустым")
    @Size(max = 32, message = "Максимальная длина населённого пункта 32")
    private String locality;

    @NotBlank
    @Size(max = 1024, message = "Максимальная длина описания 1024")
    private String description;
}
