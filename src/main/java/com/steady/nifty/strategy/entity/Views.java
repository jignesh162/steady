package com.steady.nifty.strategy.entity;

public class Views {
    // Fields needed to create entity
    public interface Create {
    }

    // Fields needed to update entity (= additional id field)
    public interface Update extends Create {
    }

    // All the fields of the entity excluding children
    public interface Default extends Update {
    }

    // All the fields of the entity including children
    public interface Child extends Default {
    }

    // All the fields of the entity including parent
    public interface Parent extends Default {
    }

    // All the fields of the entity including parentId
    public interface ParentId extends Default {
    }
}
