package xyz.unterumarmung.serialization.dto;

import javax.xml.bind.annotation.*;

@XmlType(name = "MetalBox")
@XmlAccessorType(XmlAccessType.NONE)
public class MetalBoxDto {
    @XmlElement(required = true)
    public PointDto position;
}