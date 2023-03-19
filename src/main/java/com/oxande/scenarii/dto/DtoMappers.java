package com.oxande.scenarii.dto;

import com.oxande.scenarii.model.DBFilm;
import org.apache.commons.beanutils.BeanUtils;

import java.lang.reflect.InvocationTargetException;

public final class DtoMappers {

    static void tryToCopy(Object dest, Object source) {
        try {
            BeanUtils.copyProperties(dest, source);
        } catch (IllegalAccessException | InvocationTargetException ex) {
            throw new UnsupportedOperationException("Can not copy " + source + " to " + dest, ex);
        }
    }

    public static FilmDto filmToDto(DBFilm film) {
        FilmDto dto = new FilmDto();
        tryToCopy(dto, film);
        dto.setUserOwnner(film.getOwner().getLogin());
        return dto;
    }
}
