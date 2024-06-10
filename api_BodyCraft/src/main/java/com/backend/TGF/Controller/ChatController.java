package com.backend.TGF.Controller;

import com.backend.TGF.dto.MessageDTO;
import com.backend.TGF.model.entity.Message;
import com.backend.TGF.model.services.Message.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@CrossOrigin(origins = {"*"})
@RestController
@RequestMapping("/api")
public class ChatController {

    @Autowired
    private MessageService messageService;

    @PostMapping("/sendMessage")
    public MessageDTO sendMessage(@RequestBody MessageDTO messageDTO) {
        Message message = new Message();
        message.setContent(messageDTO.getContent());
        message.setSender(messageDTO.getSender());
        message.setTimestamp(LocalDateTime.now());

        messageService.saveMessage(message);

        return messageDTO;
    }

    @GetMapping("/messages")
    public List<Message> getMessages() {
        return messageService.getAllMessages();
    }
}