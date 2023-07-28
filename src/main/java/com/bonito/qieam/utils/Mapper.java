package com.bonito.qieam.utils;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class Mapper <T,D> {

    /**
     * Convertit un objet persistant op en une ressource dto
     * @param op objet à convertir
     * @return ressource finale
     */
    public T convertToDTO(D op, Class<T> dtoClass) {
        ModelMapper modelMapper = new ModelMapper();
        return modelMapper.map(op, dtoClass);
    }

    /**
     * Convertit une ressource dto en un objet persistant dop
     * @param dto objet à convertir
     * @return obejet persistant
     */
    public D convertToEntity(T dto, Class<D> opClass) {
        ModelMapper modelMapper = new ModelMapper();
        return modelMapper.map(dto, opClass);
    }
}
