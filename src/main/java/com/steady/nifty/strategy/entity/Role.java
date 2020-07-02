package com.steady.nifty.strategy.entity;

public enum Role {
    // Note - when updating Roles remember to update the security meta-annotations
    // (IsXXX) in the security package.
    ROLE_USER, //
    ROLE_ADMIN, //
    ROLE_READONLY
}
