package com.example.demo.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class UploadController {

	@Value("${output.directory}")
	private String output_directory;

	@GetMapping("/")
	public ModelAndView index() {

		ModelAndView mav = new ModelAndView();
		mav.addObject("isSelectFlag", false);
		mav.setViewName("index");
		return mav;
	}

	@PostMapping("/upload")
	public ModelAndView fileUpload(@RequestParam("file1") MultipartFile file1,
			@RequestParam("file2") MultipartFile file2,
			@RequestParam(value = "isSelectFlag", defaultValue = "false") boolean isSelectFlag,
			RedirectAttributes redirectAttributes) {

		System.out.println("file1: " + file1.getOriginalFilename()+"	size: "+file1.getSize());
		System.out.println("file2: " + file2.getOriginalFilename()+"	size: "+file2.getSize());
		System.out.println("isSelectFlag: " + isSelectFlag);
		System.out.println("output_directory: " + output_directory);
		
		
		ModelAndView mav = new ModelAndView();
		mav.addObject("isSelectFlag", isSelectFlag);

		if (file1.isEmpty()) {
			mav.addObject("message", "Please select a file1 to upload");
			mav.setViewName("index");
			return mav;
		}

		if (file2.isEmpty()) {
			mav.addObject("message", "Please select a file2 to upload");
			mav.setViewName("index");
			return mav;
		}

		try {

			Thread.sleep(2000);

			// Get the file and save it somewhere
			byte[] bytes1 = file1.getBytes();
			Path path1 = Paths.get(output_directory + file1.getOriginalFilename());
			Files.write(path1, bytes1);
			
			byte[] bytes2 = file2.getBytes();
			Path path2 = Paths.get(output_directory + file2.getOriginalFilename());
			Files.write(path2, bytes2);
			
			Path path3 = path2;

			redirectAttributes.addFlashAttribute("message1", "File1 generated successfully - '"+path1+"'");
			redirectAttributes.addFlashAttribute("message2", "File2 generated successfully - '"+path2+"'");
			redirectAttributes.addFlashAttribute("message3", "File3 generated successfully - '"+path3+"'");

		} catch (Exception e) {
			redirectAttributes.addFlashAttribute("error", "Error while processing - "+e.toString());
			e.printStackTrace();
		} 

		mav.setViewName("redirect:/");
		return mav;
	}
}
