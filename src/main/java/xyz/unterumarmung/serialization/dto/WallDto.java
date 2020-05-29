package xyz.unterumarmung.serialization.dto;

import javax.xml.bind.annotation.*;

@XmlType(name = "Wall")
@XmlAccessorType(XmlAccessType.NONE)
public class WallDto {
    @XmlElement(required = true)
    public PointDto position;
}
