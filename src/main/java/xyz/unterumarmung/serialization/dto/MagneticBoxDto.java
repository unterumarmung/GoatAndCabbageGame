package xyz.unterumarmung.serialization.dto;

import xyz.unterumarmung.model.objects.MagneticBox.Alignment;

import javax.xml.bind.annotation.*;

import static xyz.unterumarmung.model.objects.MagneticBox.Alignment.VERTICAL_NORTH_HORIZONTAL_SOUTH;

@XmlType(name = "MagneticBox")
@XmlAccessorType(XmlAccessType.NONE)
public class MagneticBoxDto {
    @XmlElement(required = true)
    public PointDto position;

    @XmlAttribute()
    public Alignment alignment = VERTICAL_NORTH_HORIZONTAL_SOUTH;
}