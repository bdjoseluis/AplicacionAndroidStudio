package com.backend.TGF.model.services.Comprable;

import com.backend.TGF.model.entity.Comprable;

import java.util.List;

public interface IComprableService {

    List<Comprable> findAll();

    List<Comprable> findByNameStartingWith(String nombre);
}
