package com.heaven7.java.message.protocol.entity;

import com.heaven7.java.message.protocol.anno.Inherit;
import com.heaven7.java.message.protocol.anno.MethodMember;

public class TestWrongEntity {

    private String name;

    // for test wrong only have get . no set
   // @MethodMember
    public void setName(String name){
        this.name = name;
    }
    @Inherit
    @MethodMember(MethodMember.TYPE_GET)
    public String getName(){
        return name;
    }

}
