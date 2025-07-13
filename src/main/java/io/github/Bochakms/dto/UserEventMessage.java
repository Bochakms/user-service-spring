package io.github.Bochakms.dto;

import io.github.Bochakms.model.UserEvent;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserEventMessage {
    private UserEvent event;
    private String email;
}