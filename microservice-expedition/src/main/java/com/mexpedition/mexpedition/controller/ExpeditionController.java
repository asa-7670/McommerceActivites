package com.mexpedition.mexpedition.controller;

import com.mexpedition.mexpedition.Constants.Etat;
import com.mexpedition.mexpedition.dao.ExpeditionDao;
import com.mexpedition.mexpedition.exceptions.ExpeditionException;
import com.mexpedition.mexpedition.exceptions.ExpeditionNotFoundException;
import com.mexpedition.mexpedition.model.Expedition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;
import java.util.Optional;
import java.util.function.Predicate;

@RestController
public class ExpeditionController {

    private ExpeditionDao expeditionDao;
    @Autowired
    public ExpeditionController(ExpeditionDao expeditionDao) {
        this.expeditionDao = expeditionDao;
    }

    @PostMapping(value = "/expedition")
    public ResponseEntity<Expedition> addExpedition(@RequestBody Expedition expedition){
        return Optional.ofNullable(expedition)
                .filter((expeditionPredicate()))
                .filter(etatExpeditionPredicate())
                .map(expd->{
                    //SAVE
                    Optional.ofNullable(expeditionDao.save(expd))
                            .orElseThrow(()-> new ExpeditionException("Impossible d'ajouter l'expédition"));
                    return new ResponseEntity<>(expd, HttpStatus.CREATED);
                })
                .orElseThrow(()-> new IllegalArgumentException("Expedition est requis."));
    }

    @GetMapping(value = "/expedition/{id}")
    public Expedition getExpeditionbyId(@PathVariable Integer id){
        return Optional.ofNullable(id)
                .map(i->{
                    return expeditionDao.findById(id)
                            .orElseThrow(()-> new ExpeditionNotFoundException("Aucune Expédition trouvé avec l'id "+i));
                })
                .orElseThrow(()-> new IllegalArgumentException("L'id de l'expédition est requis"));
    }

    @PutMapping(value="/expedition")
    public boolean updateExpedition(@RequestBody Expedition expedition){
        return Optional.ofNullable(expedition)
                .filter(expeditionPredicate())
                .filter(etatExpeditionPredicate())
                .map(expd ->{
                    //UPDATE
                    Optional.ofNullable(expeditionDao.save(expd))
                            .orElseThrow(()-> new ExpeditionException("Impossible de mettre à jour l'expédition"));
                    return Boolean.TRUE;
                })
                .orElseThrow(()-> new IllegalArgumentException("Expedition est requis."));
    }

    private Predicate<Expedition> expeditionPredicate(){
        return expd -> Objects.nonNull(expd.getId()) && Objects.nonNull(expd.getIdCommande());
    }

    private Predicate<Expedition> etatExpeditionPredicate(){
        return expd-> Objects.nonNull(expd.getEtat()) && stateSpecification(expd.getEtat());

    }
    private boolean stateSpecification(Integer state){
        return Etat.EN_PREPARATION.getValue().equals(state) || Etat.EXPEDIEE.getValue().equals(state) || Etat.LIVREE.getValue().equals(state);
    }
}
