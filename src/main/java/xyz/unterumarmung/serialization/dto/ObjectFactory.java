package xyz.unterumarmung.serialization.dto;

import xyz.unterumarmung.Level;
import xyz.unterumarmung.serialization.dto.LevelDto;

import javax.xml.XMLConstants;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;

@XmlRegistry
public class ObjectFactory {
    private static final QName Q_NAME = new QName(XMLConstants.NULL_NS_URI, "Level");

    @XmlElementDecl(name = "Level")
    public JAXBElement<LevelDto> create(LevelDto dto) {
        return new JAXBElement<>(Q_NAME, LevelDto.class, null, dto);
    }
}
