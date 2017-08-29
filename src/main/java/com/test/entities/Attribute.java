package com.test.entities;

import com.test.jsontype.JsonBinaryType;
import com.test.jsontype.JsonStringType;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.TypeDefs;
import org.hibernate.mapping.Collection;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@TypeDefs({
        @TypeDef(name = "json", typeClass = JsonStringType.class),
        @TypeDef(name = "jsonb", typeClass = JsonBinaryType.class)
})

@Entity
@Table(name="`Attributes`")
public class Attribute extends AbstractEntity{

    private String name;
    private String type;

    public ArrayList<String> getData() {
        return data;
    }

    public void setData(ArrayList<String> data) {
        this.data = data;
    }

    @Type(type = "jsonb")
    @Column(columnDefinition = "json")
    private ArrayList<String> data;

    @OneToMany(mappedBy = "attr", fetch = FetchType.LAZY)
    private List<Value> Values;

    protected Attribute(){};

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
