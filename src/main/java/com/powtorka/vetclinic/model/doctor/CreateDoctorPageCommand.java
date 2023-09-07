package com.powtorka.vetclinic.model.doctor;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateDoctorPageCommand {

//    @Min(value = 0, message = "PAGE_NOT_NEGATIVE")
    private int pageNumber = 0;
//    @Min(value = 1, message = "PAGE_SIZE_NOT_LESS_THAN_ONE")
    private int pageSize = 5;
//    @ValueOfEnum(enumClass = Sort.Direction.class, message = "INVALID_SORT_DIRECTION")
    private String sortDirection = "ASC";
//    @Pattern(regexp = "id|name|email|rate|grade", message = "INVALID_SORT_BY_VALUE")
    private String sortBy = "id";

}
