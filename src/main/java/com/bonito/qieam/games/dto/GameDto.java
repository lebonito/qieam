package com.bonito.qieam.games.dto;

import lombok.*;

import java.util.Objects;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GameDto {

    private Long id;

    private String title;

    private String cover;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GameDto gameDto = (GameDto) o;
        return Objects.equals(id, gameDto.id) && Objects.equals(title, gameDto.title) && Objects.equals(cover, gameDto.cover);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, cover);
    }
}
