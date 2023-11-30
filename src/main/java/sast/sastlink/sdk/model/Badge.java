package sast.sastlink.sdk.model;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Badge {
    String title;
    String description;
    LocalDateTime date;

}
