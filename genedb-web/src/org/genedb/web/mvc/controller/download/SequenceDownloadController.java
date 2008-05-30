package org.genedb.web.mvc.controller.download;

import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.support.SessionStatus;

@Controller
@RequestMapping("/SequenceDownload")
public class SequenceDownloadController {
	    
	@RequestMapping(method=RequestMethod.POST)
		public String processSubmit(
				@ModelAttribute("seqDownloadBean") SequenceDownloadBean bean, 
				BindingResult result, 
				SessionStatus status 
				) {
		
		if (result.hasErrors()) {
			return "err/formtest";
		}
		
		return "err/formok";
	}
	
}
