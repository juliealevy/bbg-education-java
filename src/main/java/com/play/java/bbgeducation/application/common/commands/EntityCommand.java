package com.play.java.bbgeducation.application.common.commands;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EntityCommand {
    private String name;
    private String description;

}
