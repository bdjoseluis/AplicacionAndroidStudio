package com.backend.TGF.model.services.Message;

import com.backend.TGF.model.entity.Message;

import java.util.List;

public interface IMessageService {
    public Message saveMessage(Message message);
    public List<Message> getAllMessages();
}
