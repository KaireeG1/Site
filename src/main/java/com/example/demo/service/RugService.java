package com.example.demo.service;

import com.example.demo.model.Rug;
import com.example.demo.repository.RugRepo;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;
import java.util.UUID;
import java.util.function.BiFunction;
import java.util.function.Function;

import static com.example.demo.constant.Constant.PHOTO_DIRECTORY;
import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;
@Service
@RequiredArgsConstructor
@Transactional(rollbackOn = Exception.class)
public class RugService {
    private final RugRepo rugRepo;

    public Page<Rug> getAllRugs(int page, int size) {
        return rugRepo.findAll(PageRequest.of(page, size, Sort.by("name")));
    }

    public Rug getRug(String id) {
        return rugRepo.findById(id).orElseThrow(() -> new RuntimeException("Rug not found"));
    }

    public String uploadImage(String id, MultipartFile file) {
        Rug rug = getRug(id);
        String photoUrl = photoFunction.apply(id, file);
        rug.setPhotoUrl(photoUrl);
        rugRepo.save(rug);
        return photoUrl;
    }

    public Rug createRug(Rug rug) {
        return rugRepo.save(rug);
    }


    public void deleteRug(UUID id) {
        Rug rug = rugRepo.findById(id).orElseThrow(() -> new RuntimeException("Rug not found"));
        rugRepo.deleteById(id);
    }

    private final Function<String, String> fileExtension = filename -> Optional.of(filename).filter(name -> name.contains("."))
            .map(name -> "." + name.substring(filename.lastIndexOf(".") + 1)).orElse(".png");


    private final BiFunction<String, MultipartFile, String> photoFunction = (id, image) -> {
        String  fileName = id + fileExtension.apply(image.getOriginalFilename());

        try {
            Path fileStorageLocation = Paths.get(PHOTO_DIRECTORY).toAbsolutePath().normalize();
            if (!Files.exists(fileStorageLocation)) {
                Files.createDirectories(fileStorageLocation);
            }
            Files.copy(image.getInputStream(), fileStorageLocation.resolve(fileName), REPLACE_EXISTING);
            return ServletUriComponentsBuilder
                    .fromCurrentContextPath().path("/rugs/photo/" +fileName).toUriString();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    };

}
