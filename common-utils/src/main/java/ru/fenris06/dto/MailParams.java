package ru.fenris06.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MailParams {
    private String id;
    private String emailTo;
}