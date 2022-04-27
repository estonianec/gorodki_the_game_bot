package pro.sky.telegrambot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import pro.sky.telegrambot.model.Rating;

import java.util.List;


public interface RatingRepository extends JpaRepository<Rating, Long> {

    Rating getByChatId(long chatId);

    @Transactional
    @Modifying
    @Query(value = "INSERT into rating(chat_id, games, name, points) VALUES (?1,1,?2,0)", nativeQuery = true)
    void insertNewUser(long chatId, String name);

    @Transactional
    @Modifying
    @Query(value = "UPDATE rating SET games = games+1 WHERE chat_id = ?1", nativeQuery = true)
    void increaseGameCount(long chatId);

    @Transactional
    @Modifying
    @Query(value = "UPDATE rating SET points = points+?2 WHERE chat_id = ?1", nativeQuery = true)
    void increasePoints(long chatId, int points);

    @Query(value = "SELECT * from rating order by points DESC, games LIMIT 3", nativeQuery = true)
    List<Rating> getRating();

    void getRatingByChatId(long chatId);

    @Query(value = "SELECT * from rating", nativeQuery = true)
    List<Rating> getListForSpam();

}
