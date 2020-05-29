package xyz.unterumarmung.serialization.dto;

import javax.xml.bind.annotation.*;

@XmlType(name = "Goat")
@XmlAccessorType(XmlAccessType.NONE)
public class GoatDto {
    @XmlAttribute(required = true)
    public int initialSteps;
    @XmlElement(required = true)
    public PointDto position;
}
