package com.mmcop.controller;

import com.mmcop.predict.PredictProfile;
import com.mmcop.predict.WrapHeader;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.ServletContextAware;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.ServletContext;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.LinkedHashMap;

@Controller
public class PredictController implements ServletContextAware {
    private ServletContext servletContext;
    @Autowired
    public void setServletContext(ServletContext context) {
        this.servletContext  = context;
    }

    @RequestMapping(value="predict", method = RequestMethod.POST)
    public ModelAndView MainAccess(@RequestParam("file") CommonsMultipartFile file,
                                   @RequestParam("paste")String paste, @RequestParam("type")String type, Model map){
        try {
            String root_path = this.servletContext.getRealPath("/");
            String file_name = root_path+"upload/input.txt";
            if (!paste.isEmpty()){
                /*write paste content*/
                BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(file_name));
                bufferedWriter.write(paste);
                bufferedWriter.flush();
                bufferedWriter.close();
            } else if (!file.isEmpty()) {
			    /*write upload content*/
                file.getFileItem().write(new File(file_name));
            } else {
                map.addAttribute("error", "Please paste or upload your profiles.");
                return new ModelAndView("index");
            }

            /*generate root/type/test_arff/input.arff, and delete input.txt*/
            LinkedHashMap<String, ArrayList<String>> result;
            PredictProfile predictProfile = new PredictProfile();
            result = predictProfile.predict(type, root_path, file_name);
            if (result == null) {
                map.addAttribute("error", "Your data contains illegal characters.");
                return new ModelAndView("index");
            }
            map.addAttribute("head", new WrapHeader().wrap(type));

            return new ModelAndView("index", "map", result);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ModelAndView("index");
    }

    /*
    * @function: download miRNA and DNAm example file
    * */
    @RequestMapping(value="download")
    public ResponseEntity<byte[]> DownloadExample(String type){
        try {
            String fileName = "miRNA.txt";
            if (type.equals("miRNA")) {
                fileName = "miRNA.txt";
            } else if (type.equals("DNAm")) {
                fileName = "DNAm.txt";
            } else if (type.equals("DNAmodel")) {
                fileName = "DNAm-models.7z";
            } else if (type.equals("miRNAmodel")) {
                fileName = "miR-models.7z";
            } else if (type.equals("SelectedCpGs")) {
                fileName = "Selected.CpGs.byMRMD.7z";
            } else if (type.equals("SelectedmiRNAs")) {
                fileName = "Selected.miRNAs.7z";
            } else if (type.equals("annotationCpGs")) {
                fileName = "annotationCpGs_Infinium450K.7z";
            }
            /*bytes file*/
            String pathName = this.servletContext.getRealPath("/")+"download/"+fileName;
            byte[] bytes = FileUtils.readFileToByteArray(new File(pathName));
            /*http wrap*/
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            headers.setContentDispositionFormData("attachment",
                    new String(fileName.getBytes("gb2312"),"iso-8859-1"));
            return new ResponseEntity<>(bytes, headers, HttpStatus.OK);
        } catch (Exception ignored) {
            return null;
        }
    }
}
