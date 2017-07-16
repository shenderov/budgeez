package com.budgeez.model.interfaces;

import com.budgeez.model.entities.internal.MessageWrapper;

public interface IMailingService {

    void sendHtmlMessage(MessageWrapper messageWrapper);

    void sendHtmlMessages(Iterable<MessageWrapper> messages);

    void sendTextMessage(MessageWrapper messageWrapper);

    void sendTextMessages(Iterable<MessageWrapper> messages);
}
