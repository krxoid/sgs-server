package com.sgs.server.Objects;

import lombok.Data;

import java.util.Map;

@Data
public class HomeworkDTO {
    public String standard;
    public Byte section;
    public Map<String, Object> work;
}
