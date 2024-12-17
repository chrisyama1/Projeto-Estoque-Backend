package com.br.api.Estoque.Modelo;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Max;


//CLASSE/TABELA PRODUTOS
//AQUI SE CRIA O OBJETO E TAMBÉM A TABELA UTILIZANDO AS ANOTAÇÕES TABLE E COLUMN
@Entity
@Table(name = "produtos")
public class Produto {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nome;

    @Column(nullable = false)
    private Integer quantidade;

    @Column(nullable = false)
    @Min(1900)
    @Max(2100)
    private Integer validade;

    @Column(nullable = false)
    private String mes;

    // GETTERS E SETTERS
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Integer getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(Integer quantidade) {
        this.quantidade = quantidade;
    }

    public Integer getValidade() {
        return validade;
    }

    public void setValidade(Integer validade) {
        this.validade = validade;
    }

    public String getMes() {
        return mes;
    }

    public void setMes(String mes) {
        this.mes = mes;
    }


}
