package com.backend.TGF.mappers;

import com.backend.TGF.model.entity.Role;
import com.backend.TGF.model.repository.IRoleRepository;
import org.modelmapper.AbstractConverter;

import java.util.List;

public class RolesListConverter extends AbstractConverter<List<Long>, List<Role>>
{
    private final IRoleRepository roleRepository;

    public RolesListConverter(IRoleRepository roleRepository)
    {
        this.roleRepository = roleRepository;
    }

    @Override
    protected List<Role> convert(List<Long> longs)
    {
        return (List<Role>) roleRepository.findAllById(longs);
    }
}
