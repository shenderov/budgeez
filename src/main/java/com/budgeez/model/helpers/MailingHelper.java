package com.budgeez.model.helpers;

import com.budgeez.model.entities.dao.User;
import com.budgeez.model.entities.internal.MessageWrapper;
import com.budgeez.model.interfaces.IExceptionMessagesHelper;
import com.budgeez.model.interfaces.IMailingHelper;
import com.budgeez.security.entities.Token;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Base64;
import java.util.Scanner;

public class MailingHelper implements IMailingHelper {

    @Autowired
    private Environment env;

    @Autowired
    private IExceptionMessagesHelper exceptionMessagesHelper;

    private String homepage;

    public MessageWrapper generateAccountActivationMessage(Token token, User user){
        String template = getTemplateByName("activate_account_template.html");
        template = template.replace("_#_TITLE_#_", exceptionMessagesHelper.getLocalizedMessage("mail.activation.pending.confirmation.title"));
        String tokenLink = getTokenLink(getHomepage(), "/#/confirm", token);
        template = template.replace("_#_HOMEPAGE_#_", getHomepage());
        template = template.replace("_#_NAME_#_", user.getName());
        template = template.replace("_#_LINK_#_", tokenLink);
        return new MessageWrapper(user.getUsername(), "Please confirm your email address for registration at Budgeez", template);
    }

    public MessageWrapper generateEmailVerificationMessage(Token token, User user, String email){
        String template = getTemplateByName("verify_email_template.html");
        template = template.replace("_#_TITLE_#_", "Verify Your Email");
        String tokenLink = getTokenLink(getHomepage(), "/#/verify", token);
        template = template.replace("_#_HOMEPAGE_#_", getHomepage());
        template = template.replace("_#_NAME_#_", user.getName());
        template = template.replace("_#_LINK_#_", tokenLink);
        return new MessageWrapper(email, "Please verify your email address in order to complete operation", template);
    }

    @Override
    public MessageWrapper generatePasswordResetPassword(Token token, User user) {
        String template = getTemplateByName("reset_password_template.html");
        template = template.replace("_#_TITLE_#_", "Password reset instructions");
        String tokenLink = getTokenLink(getHomepage(), "/#/resetPassword", token);
        template = template.replace("_#_HOMEPAGE_#_", getHomepage());
        template = template.replace("_#_NAME_#_", user.getName());
        template = template.replace("_#_LINK_#_", tokenLink);
        return new MessageWrapper(user.getUsername(), "Please, reset your password", template);
    }

    private String getTemplateByName(String templateName) {
        String template = null;
        try {
            ClassLoader classLoader = getClass().getClassLoader();
            template = new Scanner(new File(classLoader.getResource("email_templates/" + templateName).getFile())).useDelimiter("\\Z").next();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return template;
    }

    private String getHomepage (){
        if (homepage == null)
            homepage= "http://" + env.getProperty("budgeez.settings.general.hostname");
        return homepage;
    }

    private String getTokenLink (String homepage, String api, Token token){
        return homepage + api + "?token=" + Base64.getEncoder().encodeToString((token.getTokenUuid() + "##" + token.getUserUuid()).getBytes());
    }


}
