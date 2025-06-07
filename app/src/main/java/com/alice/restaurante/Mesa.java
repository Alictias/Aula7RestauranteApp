package com.alice.restaurante;

import java.util.List;

public class Mesa {
    private int id;
    private String estado;
    private String cliente;
    private List<String> produtos;
    private double valorConta;

    public int getId() { return id; }
    public String getEstado() { return estado; }
    public String getCliente() { return cliente; }
    public List<String> getProdutos() { return produtos; }
    public double getValorConta() { return valorConta; }

    public void setId(int id) { this.id = id; }
    public void setEstado(String estado) { this.estado = estado; }
    public void setCliente(String cliente) { this.cliente = cliente; }
    public void setProdutos(List<String> produtos) { this.produtos = produtos; }
    public void setValorConta(double valorConta) { this.valorConta = valorConta; }
}
