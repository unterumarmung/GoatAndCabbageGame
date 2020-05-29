package xyz.unterumarmung.serialization.dto;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

@XmlType(name = "Point")
@XmlAccessorType(XmlAccessType.NONE)
public class PointDto {
    @XmlAttribute(required = true)
    public int x;
    @XmlAttribute(required = true)
    public int y;
}
