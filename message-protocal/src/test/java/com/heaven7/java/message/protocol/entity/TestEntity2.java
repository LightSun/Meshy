package com.heaven7.java.message.protocol.entity;

import com.heaven7.java.message.protocol.anno.FieldMembers;
import com.heaven7.java.message.protocol.anno.Inherit;

import java.util.Objects;

/**
 * @author heaven7
 */
@FieldMembers
public class TestEntity2 extends TestEntity {

    Byte argt1;
    Short argt2;
    Integer argt3;
    Long argt4;
    Float argt5;

    @Inherit
    Double argt6;

    Boolean argt7;
    Character argt8;

    public Byte getArgt1() {
        return argt1;
    }

    public void setArgt1(Byte argt1) {
        this.argt1 = argt1;
    }

    public Short getArgt2() {
        return argt2;
    }

    public void setArgt2(Short argt2) {
        this.argt2 = argt2;
    }

    public Integer getArgt3() {
        return argt3;
    }

    public void setArgt3(Integer argt3) {
        this.argt3 = argt3;
    }

    public Long getArgt4() {
        return argt4;
    }

    public void setArgt4(Long argt4) {
        this.argt4 = argt4;
    }

    public Float getArgt5() {
        return argt5;
    }

    public void setArgt5(Float argt5) {
        this.argt5 = argt5;
    }

    public Double getArgt6() {
        return argt6;
    }

    public void setArgt6(Double argt6) {
        this.argt6 = argt6;
    }

    public Boolean getArgt7() {
        return argt7;
    }

    public void setArgt7(Boolean argt7) {
        this.argt7 = argt7;
    }

    public Character getArgt8() {
        return argt8;
    }

    public void setArgt8(Character argt8) {
        this.argt8 = argt8;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        TestEntity2 that = (TestEntity2) o;
        return Objects.equals(argt1, that.argt1) &&
                Objects.equals(argt2, that.argt2) &&
                Objects.equals(argt3, that.argt3) &&
                Objects.equals(argt4, that.argt4) &&
                Objects.equals(argt5, that.argt5) &&
                Objects.equals(argt6, that.argt6) &&
                Objects.equals(argt7, that.argt7) &&
                Objects.equals(argt8, that.argt8);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), argt1, argt2, argt3, argt4, argt5, argt6, argt7, argt8);
    }
}
