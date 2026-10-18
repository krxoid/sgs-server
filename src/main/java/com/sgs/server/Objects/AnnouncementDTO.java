package com.sgs.server.Objects;

import lombok.Data;

@Data
public class AnnouncementDTO {

    public String title;
    public String content;
    public String[] classes;
    public Byte[] sections;
}
