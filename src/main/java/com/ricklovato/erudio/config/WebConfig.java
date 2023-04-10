package com.ricklovato.erudio.config;

import com.ricklovato.erudio.serialization.converter.YamlJackson2HttpMessageConverter;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.ContentNegotiationConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    // Define o tipo de mídia para YAML
    private static final MediaType MEDIA_TYPE_APPLICATION_YML = MediaType.valueOf("application/x-yaml");

    // Sobrescreve o método extendMessageConverters da interface WebMvcConfigurer
    @Override
    public void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
        // Chama a implementação padrão do método
        WebMvcConfigurer.super.extendMessageConverters(converters);
        // Adiciona um conversor personalizado para converter objetos Java em YAML e vice-versa
        converters.add(new YamlJackson2HttpMessageConverter());
    }

    // Sobrescreve o método configureContentNegotiation da interface WebMvcConfigurer
    @Override
    public void configureContentNegotiation(ContentNegotiationConfigurer configurer) {
        // Chama a implementação padrão do método
        WebMvcConfigurer.super.configureContentNegotiation(configurer);

        // Define as opções de negociação de conteúdo
        configurer
                // Define que a negociação de conteúdo será feita via cabeçalho da requisição
                .favorParameter(false)
                // Define que a negociação de conteúdo não irá ignorar o cabeçalho Accept enviado pelo cliente
                .ignoreAcceptHeader(false)
                // Define que a negociação de conteúdo não irá se basear nas extensões registradas
                .useRegisteredExtensionsOnly(false)
                // Define o tipo de mídia padrão para JSON
                .defaultContentType(MediaType.APPLICATION_JSON)
                // Define os tipos de mídia aceitos, com seus respectivos tipos de mídia correspondentes
                .mediaType("json", MediaType.APPLICATION_JSON)
                .mediaType("xml", MediaType.APPLICATION_XML)
                .mediaType("x-yaml", MEDIA_TYPE_APPLICATION_YML);

    }
}
