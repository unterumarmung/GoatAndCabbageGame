package xyz.unterumarmung.serialization.dto;

import javax.xml.bind.annotation.*;

@XmlRootElement
@XmlType(name = "Level")
@XmlAccessorType(XmlAccessType.NONE)
public class LevelDto {
    @XmlAttribute(required = true)
    public int id;
    @XmlElement()
    public String description;
    @XmlElement(required = true)
    public FieldDto gameField;
}
