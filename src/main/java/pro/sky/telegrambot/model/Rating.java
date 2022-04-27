package pro.sky.telegrambot.model;

import javax.persistence.*;
import java.util.Objects;

@Entity
public class Rating {

    @Column(name = "chat_id")
    @Id
    long chatId;
    @Column(name = "points")
    int points;
    @Column(name = "games")
    int games;
    @Column(name = "name")
    String name;

    public Rating() {

    }

    @Override
    public String toString() {
        return "Rating{" +
                "chatId=" + chatId +
                ", points=" + points +
                ", games=" + games +
                ", name='" + name + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Rating rating = (Rating) o;
        return chatId == rating.chatId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(chatId);
    }

    public long getChatId() {
        return chatId;
    }

    public void setChatId(long chatId) {
        this.chatId = chatId;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public int getGames() {
        return games;
    }

    public void setGames(int games) {
        this.games = games;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Rating(long chatId, int points, int games, String name) {
        this.chatId = chatId;
        this.points = points;
        this.games = games;
        this.name = name;
    }
}
