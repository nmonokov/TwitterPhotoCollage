package com.twitapp.dict;

public enum ImageSize {
    SMALL(73),
    MEDIUM(146),
    BIG(219);

    private int size;

    ImageSize(int size) {
        this.size = size;
    }

    public int getSize() {
        return size;
    }

    public boolean isSmall() {
        return this == SMALL;
    }

    public ImageSize next() {
        return this == BIG ? MEDIUM : SMALL;
    }
}
