package com.example.tvdapp.home.turnover;

public class TurnoverViewEntity {
    public int colorId;
    public String title;
    public String value;
    public int imageId;

    public TurnoverViewEntity(int colorId, String title, String value, int imageId) {
        this.colorId = colorId;
        this.title = title;
        this.value = value;
        this.imageId = imageId;
    }
}
