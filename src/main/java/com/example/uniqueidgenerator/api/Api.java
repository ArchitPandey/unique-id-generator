package com.example.uniqueidgenerator.api;

import com.example.uniqueidgenerator.model.UniqueId;
import com.example.uniqueidgenerator.service.UniqueIdGenerator;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/v1/id-generator")
@RestController
public class Api {

    UniqueIdGenerator uniqueIdGenerator;

    public Api(UniqueIdGenerator uniqueIdGenerator) {
        this.uniqueIdGenerator = uniqueIdGenerator;
    }

    @GetMapping("/id")
    ResponseEntity<UniqueId> uniqueId() {
        UniqueId id = this.uniqueIdGenerator.generateId();
        return new ResponseEntity<>(id, HttpStatus.OK);
    }

}
