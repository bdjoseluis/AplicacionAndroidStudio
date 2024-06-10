package com.backend.TGF.model.services.Message;


import com.backend.TGF.model.entity.Message;
import com.backend.TGF.model.repository.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MessageService implements IMessageService {
    @Autowired
    private MessageRepository messageRepository;

    public Message saveMessage(Message message) {
        return messageRepository.save(message);
    }

    public List<Message> getAllMessages() {
        return (List<Message>) messageRepository.findAll();
    }
}
