package co.edu.javeriana.as.personapp.domain;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StudyId implements Serializable {
    @NonNull
    private Integer personId;   
    @NonNull
    private Integer professionId; 
 

}
