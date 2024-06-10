package com.backend.TGF.dto;

import lombok.Data;

import java.util.List;

@Data
public class UserDTORequest
{
    private String username;
    private String password;
    private List<Long> rolIds;
}
