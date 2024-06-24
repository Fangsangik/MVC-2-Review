package hello.upload.file;

import hello.upload.domain.UploadFile;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Component
public class FileStore {
    
    @Value("${file.dir}")
    private String fileDir;
    
    public String getFullPath(String fileName) {
        return fileDir + fileName;
    }

    //멀티파트파일을 만들고 -> uploadFile로 지정
    public List<UploadFile> storeFiles(List<MultipartFile> multipartFiles) throws IOException {
        List<UploadFile> storeRst = new ArrayList<>();

        for (MultipartFile multipartFile : multipartFiles) {
           if (!multipartFile.isEmpty()){
               UploadFile uploadFile = storeFile(multipartFile);
               storeRst.add(uploadFile);
           }
        }

        return storeRst;
    }

    public UploadFile storeFile(MultipartFile multipartFile) throws IOException {
        if (multipartFile.isEmpty()){
            return null;
        }

        String originalFilename = multipartFile.getOriginalFilename();

        //서버에 저장하는 파일 명 (확장자를 가져오고 싶을때)
        String storeFileName = createStoreFileName(originalFilename);
        multipartFile.transferTo(new File(getFullPath(storeFileName)));
        return new UploadFile(originalFilename, storeFileName);
    }

    //저장할 file 명 (UUID 사용해서 이름 충돌 되는 경우 막음)
   private String createStoreFileName(String originalFilename) {
       String ext = extractExt(originalFilename);
       String uuid = UUID.randomUUID().toString();
       return uuid + "." + ext;
   }

   //확장자 까지 가져오는 method
   private String extractExt(String originalFilename) {
       int pos = originalFilename.lastIndexOf(".");
       return originalFilename.substring(pos + 1);
   }
}

