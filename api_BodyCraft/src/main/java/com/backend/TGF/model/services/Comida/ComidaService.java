package com.backend.TGF.model.services.Comida;

import com.backend.TGF.model.entity.Comida;
import com.backend.TGF.model.repository.IComidaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ComidaService implements IComidaService{
    @Autowired
    IComidaRepository repository;

    @Override
    public List<Comida> saveAll(List<Comida> comidas) {
        return (List<Comida>) repository.saveAll(comidas);
    }

    @Override
    public void deleteById(Long comidaId) {
        repository.deleteById(comidaId);
    }

    @Override
    public Comida findById(Long comidaId) {
        return repository.findById(comidaId).orElseThrow();
    }

    @Override
    public void save(Comida comida) {
        repository.save(comida);
    }

    @Override
    public List<Comida> findAll() {
        return (List<Comida>) repository.findAll();
    }

    @Override
    public boolean existsById(Long comidaId) {
        return repository.existsById(comidaId);
    }
}
