package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Data;


import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.Set;

@Data
@Builder
public class Film {
    private int id;

    @NotBlank
    private String name;
    @Size(min = 1, max = 200)
    private String description;
    @NotNull
    private LocalDate releaseDate;
    @Min(1)
    private long duration; // minutes

    private Set<Integer> likesByUsers;

    private Set<Genre> genres;

    private Rating mpa;

    public void addLike(Integer userId) {
        this.likesByUsers.add(userId);
    }

    public void deleteLike(Integer userId) {
        this.likesByUsers.remove(userId);
    }

}
