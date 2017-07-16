package com.budgeez.security.service;

import com.budgeez.model.entities.dao.User;
import com.budgeez.model.entities.external.EGeneralResponse;
import com.budgeez.model.enumerations.ResponseStatus;
import com.budgeez.model.interfaces.*;
import com.budgeez.model.repository.ActivationTokenRepository;
import com.budgeez.model.repository.VerificationTokenRepository;
import com.budgeez.security.entities.ActivationToken;
import com.budgeez.security.entities.Token;
import com.budgeez.security.entities.VerificationToken;
import com.budgeez.security.interfaces.IVerificationService;
import com.budgeez.security.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class VerificationService implements IVerificationService {

    @Autowired
    private ActivationTokenRepository activationTokenRepository;

    @Autowired
    private VerificationTokenRepository verificationTokenRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ITextHelper textHelper;

    @Autowired
    IMailingService mailingService;

    @Autowired
    IMailingHelper mailingHelper;

    @Autowired
    private IDateHelper dateHelper;

    @Autowired
    private IExceptionMessagesHelper exceptionMessagesHelper;

    //TODO parameters
    private boolean isAccountActivationEnabled = true;
    private boolean isEmailVerificationEnabled = true;

    public EGeneralResponse createAccountActivationToken(User user){
        EGeneralResponse response;
        if(isAccountActivationEnabled){
            if(!user.isActivated()){
                ActivationToken generatedToken = new ActivationToken(textHelper.generateToken(user), user);
                ActivationToken existingToken = activationTokenRepository.findByUser(user);
                if(existingToken != null){
                    activationTokenRepository.delete(existingToken.getId());
                }
                activationTokenRepository.save(generatedToken);
                mailingService.sendHtmlMessage(mailingHelper.generateAccountActivationMessage(generatedToken.getToken(), user));
                response = new EGeneralResponse(ResponseStatus.SUCCESS,
                        exceptionMessagesHelper.getLocalizedMessage("activation.pending.confirmation.title"),
                        exceptionMessagesHelper.getLocalizedMessage("activation.pending.confirmation.body"));
            }else{
                response = new EGeneralResponse(ResponseStatus.WARNING,
                        exceptionMessagesHelper.getLocalizedMessage("activation.already.activated.title"),
                        exceptionMessagesHelper.getLocalizedMessage("activation.already.activated.body"));
            }
        }else{
            if(!user.isActivated()){
                activateUser(user);
                response = new EGeneralResponse(ResponseStatus.SUCCESS,
                        exceptionMessagesHelper.getLocalizedMessage("activation.successfully.activated.title"),
                        exceptionMessagesHelper.getLocalizedMessage("activation.successfully.activated.body"));
            }else{
                response = new EGeneralResponse(ResponseStatus.WARNING,
                        exceptionMessagesHelper.getLocalizedMessage("activation.activation.disabled.title"),
                        exceptionMessagesHelper.getLocalizedMessage("activation.activation.disabled.body"));
            }
        }
        return response;
    }

    public EGeneralResponse createEmailVerificationToken(User user, String email){
        EGeneralResponse response;
        if(isEmailVerificationEnabled){
            VerificationToken generatedToken = new VerificationToken(textHelper.generateToken(user), user, email, new Date());
            VerificationToken existingToken = verificationTokenRepository.findByUserUuid(user.getUuid());
            if(existingToken != null){
                verificationTokenRepository.delete(existingToken.getId());
            }
            verificationTokenRepository.save(generatedToken);
            mailingService.sendHtmlMessage(mailingHelper.generateEmailVerificationMessage(generatedToken.getToken(), user, email));
            response = new EGeneralResponse(ResponseStatus.SUCCESS,
                    exceptionMessagesHelper.getLocalizedMessage("verification.pending.confirmation.title"),
                    exceptionMessagesHelper.getLocalizedMessage("verification.pending.confirmation.body"));
        }else{
            changeEmail(user, email);
            response = new EGeneralResponse(ResponseStatus.SUCCESS,
                    exceptionMessagesHelper.getLocalizedMessage("verification.email.successfully.changed.title"),
                    exceptionMessagesHelper.getLocalizedMessage("verification.email.successfully.changed.body"));
        }
        return response;
    }

    public EGeneralResponse confirmAccount(Token token){
        EGeneralResponse response;
        User user = userRepository.findByUuid(token.getUserUuid());
        ActivationToken existingToken = activationTokenRepository.findByToken(token);
        if(user == null){
            response = new EGeneralResponse(ResponseStatus.ERROR,
                    exceptionMessagesHelper.getLocalizedMessage("activation.activation.user-cant-be-found.title"),
                    exceptionMessagesHelper.getLocalizedMessage("activation.activation.user-cant-be-found.body"));
        }else if (user.isActivated()) {
            response = new EGeneralResponse(ResponseStatus.WARNING,
                    exceptionMessagesHelper.getLocalizedMessage("activation.already.activated.title"),
                    exceptionMessagesHelper.getLocalizedMessage("activation.already.activated.body"));
        }else if (existingToken == null){
            response = new EGeneralResponse(ResponseStatus.ERROR,
                    exceptionMessagesHelper.getLocalizedMessage("activation.activation.token-cant-be-found.title"),
                    exceptionMessagesHelper.getLocalizedMessage("activation.activation.token-cant-be-found.body"));
        }else{
            activateUser(user);
            activationTokenRepository.delete(existingToken);
            response = new EGeneralResponse(ResponseStatus.SUCCESS,
                    exceptionMessagesHelper.getLocalizedMessage("activation.successfully.activated.title"),
                    exceptionMessagesHelper.getLocalizedMessage("activation.successfully.activated.body"));
        }
        return response;
    }

    public EGeneralResponse verifyEmail(Token token){
        EGeneralResponse response;
        User user = userRepository.findByUuid(token.getUserUuid());
        VerificationToken existingToken = verificationTokenRepository.findByToken(token);
        if(user == null){
            response = new EGeneralResponse(ResponseStatus.ERROR,
                    exceptionMessagesHelper.getLocalizedMessage("activation.activation.user-cant-be-found.title"),
                    exceptionMessagesHelper.getLocalizedMessage("activation.activation.user-cant-be-found.body"));
        }else if (existingToken == null){
            response = new EGeneralResponse(ResponseStatus.ERROR,
                    exceptionMessagesHelper.getLocalizedMessage("activation.activation.token-cant-be-found.title"),
                    exceptionMessagesHelper.getLocalizedMessage("activation.activation.token-cant-be-found.body"));
        }else{
            changeEmail(user, existingToken.getEmail());
            user.setUsername(existingToken.getEmail());
            verificationTokenRepository.delete(existingToken);
            if(!user.isActivated()){
                createAccountActivationToken(user);
            }
            response = new EGeneralResponse(ResponseStatus.SUCCESS,
                    exceptionMessagesHelper.getLocalizedMessage("verification.email.successfully.changed.title"),
                    exceptionMessagesHelper.getLocalizedMessage("verification.email.successfully.changed.body"));
        }
        return response;
    }

    public void declineEmailToken(Token token){

    }

    private void activateUser(User user) {
        userRepository.activateUser(user.getId());
    }

    private void changeEmail(User user, String email) {
        userRepository.updateEmail(email, user.getId());
    }

}
