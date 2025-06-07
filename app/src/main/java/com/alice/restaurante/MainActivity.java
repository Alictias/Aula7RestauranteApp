package com.alice.restaurante;

import android.os.Bundle;
import android.util.Log;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Arrays;

import retrofit2.*;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {
    EditText editMesaId, editCliente, editProdutos, editValor;
    TextView textResultado;
    Button btnAtualizar;
    MesaService service;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editMesaId = findViewById(R.id.editMesaId);
        textResultado = findViewById(R.id.textResultado);

        editCliente = findViewById(R.id.editCliente);
        editProdutos = findViewById(R.id.editProdutos);
        editValor = findViewById(R.id.editValor);
        btnAtualizar = findViewById(R.id.btnAtualizarMesa);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://10.0.2.2:8080/restaurante/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        service = retrofit.create(MesaService.class);

        findViewById(R.id.btnConsultar).setOnClickListener(v -> consultarMesa());
        findViewById(R.id.btnAbrir).setOnClickListener(v -> abrirMesa());
        findViewById(R.id.btnFechar).setOnClickListener(v -> fecharMesa());
        btnAtualizar.setOnClickListener(v -> atualizarMesa());

        habilitarCampos(false);
    }

    void habilitarCampos(boolean habilitar) {
        editCliente.setEnabled(habilitar);
        editProdutos.setEnabled(habilitar);
        editValor.setEnabled(habilitar);
        btnAtualizar.setEnabled(habilitar);
    }

    void consultarMesa() {
        String textoId = editMesaId.getText().toString().trim();
        if (textoId.isEmpty()) {
            textResultado.setText("Informe o ID da mesa.");
            return;
        }

        int id = Integer.parseInt(textoId);
        service.getMesa(id).enqueue(new Callback<Mesa>() {
            public void onResponse(Call<Mesa> call, Response<Mesa> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Mesa m = response.body();

                    boolean mesaAberta = "livre".equalsIgnoreCase(m.getEstado());
                    habilitarCampos(true);

                    editCliente.setText(m.getCliente());
                    editProdutos.setText(String.join(", ", m.getProdutos()));
                    editValor.setText(String.valueOf(m.getValorConta()));

                    textResultado.setText(
                            "Mesa: " + m.getId() +
                                    "\nEstado: " + m.getEstado() +
                                    "\nCliente: " + m.getCliente() +
                                    "\nProdutos: " + m.getProdutos() +
                                    "\nValor: R$" + m.getValorConta()
                    );
                } else {
                    textResultado.setText("Erro ao buscar mesa");
                }
            }

            public void onFailure(Call<Mesa> call, Throwable t) {
                textResultado.setText("Falha: " + t.getMessage());
            }
        });
    }

    void abrirMesa() {
        String textoId = editMesaId.getText().toString().trim();
        if (textoId.isEmpty()) {
            textResultado.setText("Informe o ID da mesa.");
            return;
        }

        int id = Integer.parseInt(textoId);
        Mesa novaMesa = new Mesa();
        novaMesa.setId(id);
        novaMesa.setEstado("ocupada");
        novaMesa.setCliente("");
        novaMesa.setProdutos(Arrays.asList());
        novaMesa.setValorConta(0.01);

        service.abrirMesa(id, novaMesa).enqueue(new Callback<Void>() {
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    textResultado.setText("Mesa aberta com sucesso!");
                    novaMesa.setEstado("livre");
                    habilitarCampos(true);

                    editCliente.setText("");
                    editProdutos.setText("");
                    editValor.setText("0.0");
                } else {
                    textResultado.setText("Erro ao abrir mesa");
                }
            }

            public void onFailure(Call<Void> call, Throwable t) {
                textResultado.setText("Falha: " + t.getMessage());
            }
        });
    }

    void atualizarMesa() {
        String textoId = editMesaId.getText().toString().trim();
        if (textoId.isEmpty()) {
            textResultado.setText("Informe o ID da mesa.");
            return;
        }

        int id = Integer.parseInt(textoId);

        Mesa m = new Mesa();
        m.setId(id);
        m.setEstado("ocupada");
        m.setCliente(editCliente.getText().toString());
        m.setProdutos(Arrays.asList(editProdutos.getText().toString().split("\\s*,\\s*")));

        try {
            m.setValorConta(Double.parseDouble(editValor.getText().toString()));
        } catch (NumberFormatException e) {
            textResultado.setText("Valor inválido.");
            return;
        }

        service.abrirMesa(id, m).enqueue(new Callback<Void>() {
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    textResultado.setText("Dados da mesa atualizados com sucesso!");
                } else {
                    textResultado.setText("Erro ao atualizar dados da mesa.");
                }
            }

            public void onFailure(Call<Void> call, Throwable t) {
                textResultado.setText("Falha: " + t.getMessage());
            }
        });
    }

    void fecharMesa() {
        String textoId = editMesaId.getText().toString().trim();
        if (textoId.isEmpty()) {
            textResultado.setText("Informe o ID da mesa.");
            return;
        }

        int id = Integer.parseInt(textoId);

        service.fecharMesa(id).enqueue(new Callback<Void>() {
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    textResultado.setText("Mesa fechada com sucesso!");
                    habilitarCampos(false);
                } else {
                    textResultado.setText("Erro ao fechar mesa");
                }
            }

            public void onFailure(Call<Void> call, Throwable t) {
                textResultado.setText("Falha: " + t.getMessage());
            }
        });
    }

}
