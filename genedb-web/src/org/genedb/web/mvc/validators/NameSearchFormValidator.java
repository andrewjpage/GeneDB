package org.genedb.web.mvc.validators;

import org.genedb.web.mvc.controller.NameSearchController.NameLookupBean;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

public class NameSearchFormValidator implements Validator{

    @SuppressWarnings({"unchecked", "unused"})
    public boolean supports(Class clazz) {
        return true;
    }

    public void validate(Object object, Errors errors) {
        NameLookupBean bean = (NameLookupBean) object;

        if(bean.getName() == null && bean.getOrganism() == null) {
            errors.reject("no.params");
        }
    }

}
