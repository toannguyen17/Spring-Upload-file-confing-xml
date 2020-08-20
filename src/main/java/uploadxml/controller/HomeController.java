package uploadxml.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import org.springframework.web.multipart.support.ByteArrayMultipartFileEditor;
import uploadxml.model.MyUploadFrom;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/")
public class HomeController {
    @InitBinder
    public void initBinder(WebDataBinder dataBinder){
        Object target = dataBinder.getTarget();
        if (target == null){
            return;
        }

        System.out.println(target);
        if (target.getClass() == MyUploadFrom.class){
            dataBinder.registerCustomEditor(byte[].class, new ByteArrayMultipartFileEditor());
        }
    }

    @GetMapping
    public String doGet(){
        return "index";
    }

    @PostMapping
    public String doPost(HttpServletRequest request, Model model, @ModelAttribute MyUploadFrom myUploadFrom){
        return this.doUpload(request, model, myUploadFrom);
    }



    @GetMapping("multi")
    public String doGetMulti(){
        return "uploadMultiFile";
    }

    @PostMapping("multi")
    public String doPostMulti(HttpServletRequest request, Model model, @ModelAttribute MyUploadFrom myUploadFrom){
        return this.doUpload(request, model, myUploadFrom);
    }

    private String doUpload(HttpServletRequest request, Model model, MyUploadFrom myUploadFrom) {
        String description = myUploadFrom.getDescription();
        String uploadRootPath = request.getServletContext().getRealPath("upload");

        File uploadRootDir = new File(uploadRootPath);

        if (!uploadRootDir.exists()){
            uploadRootDir.mkdirs();
        }

        CommonsMultipartFile[] fileDatas = myUploadFrom.getFileDatas();

        Map<File, String> uploadFiles = new HashMap<>();

        for (CommonsMultipartFile fileData: fileDatas){
            String name = fileData.getOriginalFilename();
            System.out.println("Client File Name = " + name);

            if (name != null && name.length() > 0){
                File serverFile = new File(uploadRootDir.getAbsolutePath() + File.separator + name);

                try {
                    BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(serverFile));
                    stream.write(fileData.getBytes());
                    stream.close();
                    uploadFiles.put(serverFile, name);
                    System.out.println("Write file: " + serverFile);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        model.addAttribute("description", description);
        model.addAttribute("uploadedFiles", uploadFiles);

        System.out.println(uploadFiles);

        return "uploadResult";
    }
}
