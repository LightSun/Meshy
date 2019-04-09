package com.heaven7.java.message.protocal;

import com.heaven7.java.message.protocal.anno.FieldMember;

import java.util.Objects;

/**
 * @author heaven7
 */
public class TestEntity {

    @FieldMember
    private byte arg1;
    @FieldMember
    private short arg2;

    @FieldMember
    private int arg3;
    @FieldMember
    private long arg4;
    @FieldMember
    private boolean arg5;

    @FieldMember
    private float arg6;
    @FieldMember
    private double arg7;
    @FieldMember
    private String arg8;

    public float getArg6() {
        return arg6;
    }
    public void setArg6(float arg6) {
        this.arg6 = arg6;
    }

    public double getArg7() {
        return arg7;
    }

    public void setArg7(double arg7) {
        this.arg7 = arg7;
    }

    public String getArg8() {
        return arg8;
    }

    public void setArg8(String arg8) {
        this.arg8 = arg8;
    }

    public byte getArg1() {
        return arg1;
    }

    public void setArg1(byte arg1) {
        this.arg1 = arg1;
    }

    public short getArg2() {
        return arg2;
    }

    public void setArg2(short arg2) {
        this.arg2 = arg2;
    }

    public int getArg3() {
        return arg3;
    }

    public void setArg3(int arg3) {
        this.arg3 = arg3;
    }

    public long getArg4() {
        return arg4;
    }

    public void setArg4(long arg4) {
        this.arg4 = arg4;
    }

    public boolean isArg5() {
        return arg5;
    }

    public void setArg5(boolean arg5) {
        this.arg5 = arg5;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TestEntity that = (TestEntity) o;
        return arg1 == that.arg1 &&
                arg2 == that.arg2 &&
                arg3 == that.arg3 &&
                arg4 == that.arg4 &&
                arg5 == that.arg5 &&
                Float.compare(that.arg6, arg6) == 0 &&
                Double.compare(that.arg7, arg7) == 0 &&
                Objects.equals(arg8, that.arg8);
    }

    @Override
    public int hashCode() {
        return Objects.hash(arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8);
    }
}
