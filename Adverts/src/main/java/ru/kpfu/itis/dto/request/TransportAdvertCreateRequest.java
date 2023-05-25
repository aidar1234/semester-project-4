package ru.kpfu.itis.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;
import ru.kpfu.itis.validation.annotation.TransportAdvertFilesConstraint;
import ru.kpfu.itis.validation.annotation.TransportAdvertKindConstraint;

import javax.validation.constraints.*;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@TransportAdvertKindConstraint
@TransportAdvertFilesConstraint
public class TransportAdvertCreateRequest {

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

    @NotBlank(message = "Не может быть пустым")
    @Size(max = 32, message = "Максимальная длина бренда 32")
    private String brand;

    private String isNew;

    private String isRegistered;

    private String kind;

    @NotNull(message = "Не может быть пустым")
    @Min(value = 0, message = "Не может быть меньше нуля")
    @Max(value = Integer.MAX_VALUE, message = "Не может быть таким большим")
    private Integer mileage;

    @NotBlank
    @Size(max = 1024, message = "Максимальная длина описания 1024")
    private String description;
}
