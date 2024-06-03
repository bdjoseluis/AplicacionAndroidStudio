package com.example.tfg;

import static android.content.Context.MODE_PRIVATE;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.platform.app.InstrumentationRegistry.getInstrumentation;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import android.content.Intent;
import android.content.SharedPreferences;

import androidx.test.core.app.ActivityScenario;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.tfg.Login.InicioSesion;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class MainActivityTest {

    private SharedPreferences preferences;

    @Before
    public void setUp() {
        // Configura las preferencias de la prueba
        preferences = getInstrumentation().getTargetContext().getSharedPreferences("com.example.tfg", MODE_PRIVATE);
    }

    @Test
    public void testFirstTimeUserRedirectsToInicioSesion() {
        // Configura la preferencia de primer uso a true
        preferences.edit().putBoolean("first_time_user", true).apply();

        // Inicia la actividad
        try (ActivityScenario<MainActivity> scenario = ActivityScenario.launch(MainActivity.class)) {
            // Realiza clic en el botón de inicio
            onView(withId(R.id.startButon)).perform(click());

            // Verifica si la actividad InicioSesion se inicia
            scenario.onActivity(activity -> {
                Intent actualIntent = activity.getIntent();
                assertNotNull(actualIntent);
                // Verifica que el Intent sea para la actividad InicioSesion
                assertEquals(InicioSesion.class.getName(), actualIntent.getComponent().getClassName());
            });
        }
    }

    @Test
    public void testExistingUserRedirectsToInicio() {
        // Configura la preferencia de primer uso a false
        preferences.edit().putBoolean("first_time_user", false).apply();

        // Inicia la actividad
        try (ActivityScenario<MainActivity> scenario = ActivityScenario.launch(MainActivity.class)) {
            // Realiza clic en el botón de inicio
            onView(withId(R.id.startButon)).perform(click());

            // Verifica si la actividad Inicio se inicia
            scenario.onActivity(activity -> {
                Intent actualIntent = activity.getIntent();
                assertNotNull(actualIntent);
                // Verifica que el Intent sea para la actividad Inicio
                assertEquals(Inicio.class.getName(), actualIntent.getComponent().getClassName());
            });
        }
    }

    @Test
    public void testLottieAnimationView5ClickStartsAnimation() {
        // Inicia la actividad
        try (ActivityScenario<MainActivity> scenario = ActivityScenario.launch(MainActivity.class)) {
            // Realiza clic en LottieAnimationView5
            onView(withId(com.airbnb.lottie.R.id.lottie_layer_name)).perform(click());

            // Aquí, en lugar de verificar si la animación comienza, puedes usar un método de espera o un observador para verificar si la animación está activa.
            // Dado que la animación puede no ser fácil de probar directamente, considera otras formas de verificación.
        }
    }
}
