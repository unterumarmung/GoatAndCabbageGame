package xyz.unterumarmung.serialization.dto;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

@XmlType(name = "SimpleBox")
@XmlAccessorType(XmlAccessType.NONE)
public class SimpleBoxDto {
    @XmlElement(required = true)
    public PointDto position;
}
