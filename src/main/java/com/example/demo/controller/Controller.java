package com.example.demo.controller;

import com.example.demo.model.Rug;
import com.example.demo.service.RugService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.UUID;

import static com.example.demo.constant.Constant.PHOTO_DIRECTORY;
import static org.springframework.http.MediaType.*;

@RestController
@RequestMapping("/rugs")
@RequiredArgsConstructor
public class Controller {
    private final RugService rugService;

    @PostMapping
    public ResponseEntity<Rug> createRug(@RequestBody Rug rug) {
        return ResponseEntity.created(URI.create("")).body(rugService.createRug(rug));
    }
    @GetMapping
    public ResponseEntity<Page<Rug>> getRugs(@RequestParam(value = "page",defaultValue = "0") int page,
                                             @RequestParam(value = "size",defaultValue = "10")int size) {
        return ResponseEntity.ok().body(rugService.getAllRugs(page, size));
    }
    @GetMapping("/{id}")
    public ResponseEntity<Rug> getRug(@PathVariable(value = "id")String id) {
        return ResponseEntity.ok().body(rugService.getRug(id));
    }
    @PutMapping("/photo")
    public ResponseEntity<String> updatePhoto(@RequestParam("id")String id, @RequestParam("file") MultipartFile file) {
        return ResponseEntity.ok().body(rugService.uploadImage(id, file));
    }

    @GetMapping(path = "/photo/{fileName}",produces = {IMAGE_JPEG_VALUE ,IMAGE_PNG_VALUE ,IMAGE_GIF_VALUE})
    public byte[] getPhoto(@PathVariable("fileName")String filename) throws IOException {
        return Files.readAllBytes(Paths.get(PHOTO_DIRECTORY + filename));
    }


// finish this method
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteRug(@PathVariable("id") UUID id) {
        rugService.deleteRug(id);
        return ResponseEntity.ok().build();
    }


}


