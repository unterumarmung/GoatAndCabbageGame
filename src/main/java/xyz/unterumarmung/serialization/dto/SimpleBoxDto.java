package xyz.unterumarmung.serialization.dto;

import javax.xml.bind.annotation.*;

@XmlType(name = "SimpleBox")
@XmlAccessorType(XmlAccessType.NONE)
public class SimpleBoxDto {
    @XmlElement(required = true)
    public PointDto position;
}
