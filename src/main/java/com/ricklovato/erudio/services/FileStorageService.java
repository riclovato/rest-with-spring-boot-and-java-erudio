package com.ricklovato.erudio.services;

import com.ricklovato.erudio.config.FileStorageConfig;
import com.ricklovato.erudio.exceptions.FileStorageException;
import com.ricklovato.erudio.exceptions.MyFileNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Service
public class FileStorageService {
//serviço responsável por gravar arquivo em disco

    //caminho completo até a pasta onde os arquivos serão salvos
    private final Path fileStorageLocation;

    @Autowired
    public FileStorageService(FileStorageConfig fileStorageConfig) {
       //normalize para transformar um path do java.nio
        Path path = Paths.get(fileStorageConfig.getUploadDir()).toAbsolutePath().normalize();

        this.fileStorageLocation = path;

        try{
            //se o diretório existir ele usa, se não existir ele cria
            Files.createDirectories(this.fileStorageLocation);

        }catch (Exception e){
            throw new FileStorageException("Could not create the directory where the uploaded files will be stored!"
                    ,e);
        }
    }
    public String storeFile(MultipartFile file){
        //obtem o nome do arquivo
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());
        try{
            //confere se a extensão é válida
            if(fileName.contains("..")){
                throw new FileStorageException("Sorry! File Name contains invalid path sequence : "+ fileName);
            }
           //caminho completo como nome do arquivo
            //para salvar em outros locais como banco de dados,nuvem etc
            // modificar as duas linhas abaixo
            Path targetLocation = this.fileStorageLocation.resolve(fileName);
            Files.copy(file.getInputStream(),targetLocation, StandardCopyOption.REPLACE_EXISTING);
            return fileName;
        }catch (Exception e){
            throw new FileStorageException("Could not store file " + fileName +". Please try again!");
        }
    }
    public Resource loadFileAsResource(String fileName){

        try{
           //obtem caminho até o arquivo
            Path filePath = this.fileStorageLocation.resolve(fileName).normalize();
           //obtem o resource apartir desse arquivo
            Resource resource = new UrlResource(filePath.toUri());
            if(resource.exists()) return resource;
            else {
                throw new MyFileNotFoundException("File not found: "+ fileName);
            }
        }catch (Exception e){
            throw new MyFileNotFoundException("File not found: "+ fileName,e);
        }
    }
}
