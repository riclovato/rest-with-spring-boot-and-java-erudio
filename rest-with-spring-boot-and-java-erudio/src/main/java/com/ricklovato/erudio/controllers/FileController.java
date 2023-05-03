package com.ricklovato.erudio.controllers;

import com.ricklovato.erudio.data.vo.v1.UploadFileResponseVO;

import com.ricklovato.erudio.services.FileStorageService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@Tag(name = "File endPoint")
@RestController
@RequestMapping("/api/file/v1")
public class FileController {

    private Logger logger = Logger.getLogger(FileController.class.getName());

    @Autowired
    private FileStorageService fileStorageService;
    @PostMapping("/uploadFile")
    public UploadFileResponseVO uploadFile(@RequestParam("file")MultipartFile file){
       logger.info("Storing file to disk");
        var fileName = fileStorageService.storeFile(file);
        String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/api/file/v1/downloadFile")
                .path(fileName)
                .toUriString();
        return new UploadFileResponseVO(fileName,fileDownloadUri,file.getContentType(),file.getSize());
    }

    @PostMapping("/uploadMultipleFiles")
    public List<UploadFileResponseVO> uploadMultipleFiles(@RequestParam("files")MultipartFile[] files){
        logger.info("Storing files to disk");
        //para cada item que receber vai iterar sobre o array e chamar o método uploadFile
        return Arrays.asList(files)
                .stream().map(file -> uploadFile(file))
                .collect(Collectors.toList());
    }

    //.+ -> recebe o nome do arquivo, vai ter um . e algo depois desse .
    @GetMapping("/downloadFile/{fileName:.+}")
    public ResponseEntity<Resource> downloadFile(@PathVariable String fileName, HttpServletRequest request){
        logger.info("downloading a file");
        Resource resource = fileStorageService.loadFileAsResource(fileName);
       String contentType = "";
       try{
           contentType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
       }catch (Exception e){
           logger.info("Could not determine file type!");
       }
       if(contentType.isBlank()){
           //contentType mais genérico que existe
           contentType = "application/octet-stream";

       }
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(
                        HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; fileName=\"" + resource.getFilename() + "\"")
                .body(resource);
    }

}
