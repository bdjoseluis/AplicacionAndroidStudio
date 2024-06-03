package com.example.tfg.Compra;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.tfg.R;
import com.stripe.android.ApiResultCallback;
import com.stripe.android.PaymentIntentResult;
import com.stripe.android.Stripe;
import com.stripe.android.model.ConfirmPaymentIntentParams;
import com.stripe.android.model.PaymentMethodCreateParams;
import com.stripe.android.view.CardInputWidget;

import java.lang.ref.WeakReference;

public class PagosActivity extends AppCompatActivity {

    private Stripe stripe;
    private String paymentIntentClientSecret;
    private EditText editTextCardholderName;
    private EditText editTextCardNumber;
    private EditText editTextExpirationDate;
    private EditText editTextCVV;
    private CardInputWidget cardInputWidget; // Referencia al widget de entrada de tarjeta



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pagos);

        // Inicializa Stripe con tu clave secreta
        stripe = new Stripe(this, "tu_clave_secreta_de_stripe");

        // Obtén el cliente secreto del servidor (debe generarse en tu backend)
        paymentIntentClientSecret = "tu_cliente_secreto_del_servidor";

        // Referencia al widget de entrada de tarjeta
        cardInputWidget = findViewById(R.id.cardInputWidget);

        // Referencia al botón de pago
        Button btnPagar = findViewById(R.id.btnPagar);

        // Configuración del botón de pago
        btnPagar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Crea un PaymentMethod desde el widget de entrada de tarjeta
                PaymentMethodCreateParams params = cardInputWidget.getPaymentMethodCreateParams();
                if (params != null) {
                    ConfirmPaymentIntentParams confirmParams = ConfirmPaymentIntentParams
                            .createWithPaymentMethodCreateParams(params, paymentIntentClientSecret);
                    stripe.confirmPayment(PagosActivity.this, confirmParams);
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        stripe.onPaymentResult(requestCode, data, new PaymentResultCallback(this));
    }

    private class PaymentResultCallback implements ApiResultCallback<PaymentIntentResult> {
        private final WeakReference<PagosActivity> activityRef;

        PaymentResultCallback(PagosActivity activity) {
            activityRef = new WeakReference<>(activity);
        }

        @Override
        public void onSuccess(@NonNull PaymentIntentResult result) {
            PagosActivity activity = activityRef.get();
            if (activity != null) {
                // Procesa el resultado del pago (éxito o error)
                // Puedes mostrar un mensaje al usuario aquí
                Toast.makeText(PagosActivity.this, "Pago Realizado correctamente", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(PagosActivity.this, Compra.class);
                startActivity(intent);
            }
        }

        @Override
        public void onError(@NonNull Exception e) {
            // Maneja errores (por ejemplo, tarjeta inválida, fallo de conexión, etc.)
        }
    }
}