package com.example.myapplication.gameModule;

public class Player_progress {
    int score,Level;
    String playerType;
    public Player_progress() { }

    public Player_progress(int score, int level, String playerType) {
        this.score = score;
        Level = level;
        this.playerType = playerType;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getLevel() {
        return Level;
    }

    public void setLevel(int level) {
        Level = level;
    }

    public String getPlayerType() {
        return playerType;
    }

    public void setPlayerType(String playerType) {
        this.playerType = playerType;
    }
}
