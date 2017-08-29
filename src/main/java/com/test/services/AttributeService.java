package com.test.services;

import com.test.entities.Attribute;

public interface AttributeService {

    public Iterable<Attribute> getAllAttributes();
    public void deleteAttributeById(Long id, Boolean force);
    public Attribute getAttributeById(Long id);
    public Attribute updateAttributeById(Long id, Attribute attribute, Boolean force);
    public Attribute createAttribute(Attribute attribute);

}
