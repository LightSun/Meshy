package com.heaven7.java.meshy.entity;

import com.heaven7.java.meshy.anno.FieldMembers;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

/**
 * @author heaven7
 */
@FieldMembers
public class TestComplexEntity {

    private Set<? extends Integer> set;
    private List<? super Integer> list;

    private Map<Integer, ? extends List<Integer>> map;


    public Set<? extends Integer> getSet() {
        return set;
    }
    public void setSet(Set<? extends Integer> set) {
        this.set = set;
    }
    public List<? super Integer> getList() {
        return list;
    }
    public void setList(List<? super Integer> list) {
        this.list = list;
    }
    public Map<Integer, ? extends List<Integer>> getMap() {
        return map;
    }
    public void setMap(Map<Integer, ? extends List<Integer>> map) {
        this.map = map;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TestComplexEntity that = (TestComplexEntity) o;
        return Objects.equals(set, that.set) &&
                Objects.equals(list, that.list) &&
                Objects.equals(map, that.map);
    }

    @Override
    public int hashCode() {

        return Objects.hash(set, list, map);
    }
}
