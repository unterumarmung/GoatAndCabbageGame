package xyz.unterumarmung.serialization.dto;

import javax.xml.bind.annotation.*;
import java.util.List;

@XmlType(name = "GameField")
@XmlAccessorType(XmlAccessType.NONE)
public class FieldDto {
    @XmlAttribute(required = true)
    public int width;
    @XmlAttribute(required = true)
    public int height;

    @XmlElement(required = true)
    public PointDto exitPoint;

    @XmlElement
    public List<WallDto> wall;
    @XmlElement(required = true)
    public List<GoatDto> goat;
    @XmlElement
    public List<SimpleBoxDto> simpleBox;
    @XmlElement
    public List<MetalBoxDto> metalBox;
    @XmlElement
    public List<MagneticBoxDto> magneticBox;
}
