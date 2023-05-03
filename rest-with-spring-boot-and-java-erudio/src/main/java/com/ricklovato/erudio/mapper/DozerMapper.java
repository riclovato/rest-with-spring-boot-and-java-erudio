package com.ricklovato.erudio.mapper;

import com.github.dozermapper.core.DozerBeanMapperBuilder;
import com.github.dozermapper.core.Mapper;

import java.util.ArrayList;
import java.util.List;

public class DozerMapper {

    // Instância do Mapper, responsável por fazer o mapeamento entre objetos
    private static Mapper mapper = DozerBeanMapperBuilder.buildDefault();

    // Método que recebe um objeto de origem e uma classe de destino e retorna o objeto mapeado
    public static <O, D> D parseObject(O origin, Class<D> destination){
        return mapper.map(origin,destination);
    }

    // Método que recebe uma lista de objetos de origem e uma classe de destino e retorna uma lista de objetos mapeados
    public static <O, D> List<D> parseListObject(List<O> origin, Class<D> destination){
        List<D> destinantionObjects = new ArrayList<D>();
        for (O o: origin) {
            destinantionObjects.add(mapper.map(o,destination));
        }
        return destinantionObjects;
    }
}
