package com.mlb.pdf2html.controller;

import java.util.HashMap;

import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.mlb.pdf2html.service.Pdf2HtmlService;

@Controller
public class Pdf2HtmlController {
	private static final Logger logger = LoggerFactory.getLogger(Pdf2HtmlController.class);

	@Autowired
	private Pdf2HtmlService pdf2HtmlService;

	/**
	 * @param
	 */
	@RequestMapping(value = "/convert", method = RequestMethod.GET)
	public String convert(HttpSession session) {
		@SuppressWarnings("unchecked")
		HashMap<String, String> fileList = (HashMap<String, String>) session.getAttribute("fileList");
		HashMap<String, Integer> pageInfo = new HashMap<>();
		String pdfdir = session.getServletContext().getRealPath("pdflist/");
		String jspdir = session.getServletContext().getRealPath("htmlList/");

		long startTime = System.currentTimeMillis();
		pageInfo = pdf2HtmlService.convertPdf2Html(pdfdir, jspdir, fileList);
		long endTime = System.currentTimeMillis();
		System.out.println("convert time : " + (endTime - startTime));
		
		session.setAttribute("pageInfo", pageInfo);
		return "redirect:view";
	}

	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String home(HttpSession session, Model model) {
		logger.info(System.currentTimeMillis() + "");

		return "home";
	}

	@RequestMapping(value = "/view", method = RequestMethod.GET)
	public String minimap(HttpSession session, Model model) {
		logger.info(System.currentTimeMillis() + "");

		return "viewer";
	}

	@RequestMapping(value = "/img", method = RequestMethod.GET)
	public String pdf2Img(HttpSession session, Model model) {
		logger.info(System.currentTimeMillis() + "");

		return "pdf2img";
	}

	@RequestMapping(value = "/fileupload", method = RequestMethod.POST)
	public void fileUploadAjax(HttpSession session, @RequestParam(value = "file") MultipartFile[] file) {
		HashMap<String, String> fileList = new HashMap<>();
		String pdfdir = session.getServletContext().getRealPath("pdflist/");
		fileList = pdf2HtmlService.pdfUpload(file, pdfdir);
		session.setAttribute("fileList", fileList);
	}
}
