package com.backend.TGF.model.services.Comprable;

import com.backend.TGF.model.entity.Comprable;
import com.backend.TGF.model.repository.IComidaRepository;
import com.backend.TGF.model.repository.IComprableRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ComprableService implements IComprableService{
    @Autowired
    IComprableRepository repository;

    @Override
    public List<Comprable> findAll() {
        return (List<Comprable>) repository.findAll();
    }

    @Override
    public List<Comprable> findByNameStartingWith(String nombre) {
        return repository.findByTituloStartingWith(nombre);
    }
}
