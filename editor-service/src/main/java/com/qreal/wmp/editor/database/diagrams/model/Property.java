package com.qreal.wmp.editor.database.diagrams.model;

import com.qreal.wmp.thrift.gen.TProperty;
import lombok.Data;

import java.io.Serializable;

/** Property of an entity.*/
@Data
public class Property implements Serializable {
    private String propertyId;

    private String name;

    private String value;

    private String type;

    private Double xPosition;

    private Double yPosition;

    public Property() { }

    /** Constructor-converter from Thrift TProperty to Property.*/
    public Property(TProperty tProperty) {
        if (tProperty.isSetPropertyId()) {
            propertyId = tProperty.getPropertyId();
        }

        if (tProperty.isSetName()) {
            name = tProperty.getName();
        }

        if (tProperty.isSetValue()) {
            value = tProperty.getValue();
        }

        if (tProperty.isSetType()) {
            type = tProperty.getType();
        }

        if (tProperty.isSetX()) {
            xPosition = tProperty.getX();
        }

        if (tProperty.isSetY()) {
            yPosition = tProperty.getY();
        }
    }

    /** Converter from Property to Thrift TProperty.*/
    public TProperty toTProperty() {
        TProperty tProperty = new TProperty();
        if (value != null) {
            tProperty.setValue(value);
        }

        if (name != null) {
            tProperty.setName(name);
        }

        if (type != null) {
            tProperty.setType(type);
        }

        if (propertyId != null) {
            tProperty.setPropertyId(propertyId);
        }

        if (xPosition != null) {
            tProperty.setX(xPosition);
        }

        if (yPosition != null) {
            tProperty.setY(yPosition);
        }

        return tProperty;
    }
}
