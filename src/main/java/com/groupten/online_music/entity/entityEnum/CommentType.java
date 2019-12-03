package com.groupten.online_music.entity.entityEnum;

public enum CommentType {
    SONG(0, "song"),
    SONG_LIST(1, "song list");

    private final int value;
    private final String reasonPhrase;

    private CommentType(int value, String reasonPhrase) {
        this.value = value;
        this.reasonPhrase = reasonPhrase;
    }
}
