package com.alice.restaurante;

import retrofit2.Call;
import retrofit2.http.*;

public interface MesaService {
    @GET("/restaurante/mesa/{id}")
    Call<Mesa> getMesa(@Path("id") int id);

    @PUT("/restaurante/mesa/{id}")
    Call<Void> abrirMesa(@Path("id") int id, @Body Mesa mesa);

    @DELETE("/restaurante/mesa/{id}")
    Call<Void> fecharMesa(@Path("id") int id);
}

