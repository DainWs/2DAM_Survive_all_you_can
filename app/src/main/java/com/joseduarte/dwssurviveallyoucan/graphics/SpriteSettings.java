package com.joseduarte.dwssurviveallyoucan.graphics;

public class SpriteSettings {
    private int spriteSheetColumns;
    private int spriteSheetRows;
    private int currentSpriteCol = 0;
    private int currentSpriteRow = 0;
    private int spriteWidth = 0;
    private int spriteHeight = 0;

    private boolean updated = true;

    public SpriteSettings(int colCount, int rowCount) {
        spriteSheetColumns = colCount;
        spriteSheetRows = rowCount;
        updated = false;
    }

    public SpriteSettings(int spriteSheetWidth, int spriteSheetHeight, int colCount, int rowCount) {
        spriteSheetColumns = colCount;
        spriteSheetRows = rowCount;

        this.spriteWidth = spriteSheetWidth / colCount;
        this.spriteHeight = spriteSheetHeight / rowCount;
    }

    public void update(int spriteSheetWidth, int spriteSheetHeight) {
        if (!updated) {
            this.spriteWidth = spriteSheetWidth / spriteSheetColumns;
            this.spriteHeight = spriteSheetHeight / spriteSheetRows;
            updated = true;
        }
    }

    public void nextLine() {
        currentSpriteRow++;
        if(currentSpriteRow >= spriteSheetRows) currentSpriteRow = 0;
    }

    public void setLine(int newLine) {
        currentSpriteRow = newLine;
        if(currentSpriteRow >= spriteSheetRows || currentSpriteRow < 0 ) currentSpriteRow = 0;
    }

    public int nextSpriteCol() {
        currentSpriteCol++;
        if(currentSpriteCol >= spriteSheetColumns) currentSpriteCol = 0;
        return currentSpriteCol;
    }

    public int getCurrentSpriteLine() {
        return currentSpriteRow;
    }

    public int getSpriteWidth() {
        return spriteWidth;
    }

    public int getSpriteHeight() {
        return spriteHeight;
    }

}

