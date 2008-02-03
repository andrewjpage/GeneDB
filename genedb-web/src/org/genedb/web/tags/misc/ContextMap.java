package org.genedb.web.tags.misc;

import static javax.servlet.jsp.PageContext.REQUEST_SCOPE;

import java.io.IOException;

import javax.servlet.jsp.JspContext;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.SimpleTagSupport;

import org.genedb.web.gui.ImageInfo;
import org.genedb.web.mvc.controller.GeneDBWebUtils;
import org.gmod.schema.sequence.Feature;
import org.springframework.context.ApplicationContext;
import org.springframework.web.util.WebUtils;

public class ContextMap extends SimpleTagSupport {
	
	private Feature gene;
	
	@Override
    public void doTag() throws JspException, IOException {
        String prefix = getContextPathFromJspContext(getJspContext());
		ImageInfo info = GeneDBWebUtils.drawContextMap(gene, prefix);
	}
	
	private String getContextPathFromJspContext(JspContext context) {
		String prefix = (String) context.getAttribute(WebUtils.FORWARD_CONTEXT_PATH_ATTRIBUTE, REQUEST_SCOPE);
        if (!prefix.equals("/")) {
        	prefix += "/";
        }
		return prefix;
	}
	
	public void setGene(Feature gene) {
		this.gene = gene;
	}
}
