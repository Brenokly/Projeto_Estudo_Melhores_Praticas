package br.com.myproject.projeto_para_estudo.infrastructure.adapter.in.rest;

import org.springframework.web.bind.annotation.RestController;

/*
 * - infrastructure.adapter.in (Adaptadores de Entrada)
 * O que é?
    * Classes que recebem estímulos do mundo exterior e os traduzem em chamadas para a camada de application.
 * Qual seu trabalho?
    * Adaptar um protocolo específico (ex: HTTP, Linha de Comando) para uma chamada de método Java.
    * rest.controller: Recebe uma requisição HTTP, converte o JSON (usando um DTO) para um objeto de comando e chama o serviço da application.
    * cli: Lê argumentos da linha de comando e chama o serviço da application.
 */

@RestController
public class UsuarioAdapterInRest {

}
