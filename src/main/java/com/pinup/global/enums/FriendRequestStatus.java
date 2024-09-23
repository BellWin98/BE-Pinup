package com.pinup.global.enums;

public enum FriendRequestStatus {
    PENDING, ACCEPTED, REJECTED;

    public String getValue() {
        return this.name().toLowerCase();
    }
}
