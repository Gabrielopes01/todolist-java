package br.com.crmsolucoes.todolist.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/primeiraRota")
// http://localhost:8080
public class PrimeiraController {
    
    /**
     * GET - Buscar Informação 
     * POST - Adicionar Informação
     * PUT - Alterar Informação
     * DELETE - Remover Informação
     * PATCH - Alterar somente uma parte da Informação
     */

    @GetMapping("")
    public String primeiraMensagem() {
        return "Hello";
    }
}
