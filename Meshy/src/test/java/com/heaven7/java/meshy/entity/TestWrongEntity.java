package com.heaven7.java.meshy.entity;

import com.heaven7.java.meshy.anno.Inherit;
import com.heaven7.java.meshy.anno.MethodMember;

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
