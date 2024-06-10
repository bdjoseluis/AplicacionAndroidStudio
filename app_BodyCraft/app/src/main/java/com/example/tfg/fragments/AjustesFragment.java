package com.example.tfg.fragments;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.preference.EditTextPreference;
import androidx.preference.ListPreference;
import androidx.preference.PreferenceFragmentCompat;
import com.example.tfg.Notificaciones.NotificationScheduler;
import com.example.tfg.R;

import java.util.Locale;
import java.util.Calendar;

public class AjustesFragment extends PreferenceFragmentCompat {

  private EditTextPreference pesoPreference;
  private ListPreference generoPreference;
  private EditTextPreference fechaNacimientoPreference;
  private ListPreference objetivosPreference;
  private ListPreference idiomaPreference;

  @Override
  public void onCreatePreferences(Bundle savedInstanceState, @Nullable String rootKey) {
    setPreferencesFromResource(R.xml.preferencias, rootKey);

    // Obtener preferencias
    pesoPreference = findPreference("peso");
    generoPreference = findPreference("genero");
    fechaNacimientoPreference = findPreference("fecha_nacimiento");
    objetivosPreference = findPreference("objetivos");
    idiomaPreference = findPreference("idioma");

    // Obtener preferencias compartidas
    SharedPreferences preferences = getPreferenceManager().getSharedPreferences();

    // Establecer valores predeterminados para idioma y objetivos
    setDefaultPreferences(preferences);

    // Obtener valores iniciales de preferencias
    loadInitialPreferences(preferences);

    // Registrar el listener de cambios de preferencias
    preferences.registerOnSharedPreferenceChangeListener(preferenceChangeListener);
  }

  private void setDefaultPreferences(SharedPreferences preferences) {
    if (idiomaPreference != null) {
      setDefaultValue(preferences, "idioma", "Español, España", idiomaPreference);
    }
    if (objetivosPreference != null) {
      setDefaultValue(preferences, "objetivos", "Perder Peso", objetivosPreference);
    }
  }

  private void setDefaultValue(SharedPreferences preferences, String key, String defaultValue, ListPreference preference) {
    String value = preferences.getString(key, null);
    if (value == null) {
      SharedPreferences.Editor editor = preferences.edit();
      editor.putString(key, defaultValue);
      editor.apply();
      preference.setValue(defaultValue);
    }
  }

  private void loadInitialPreferences(SharedPreferences preferences) {
    // Establecer valores iniciales
    int pesoInicial = preferences.getInt("userpeso", -1);
    if (pesoInicial != -1) {
      pesoPreference.setText(String.valueOf(pesoInicial));
    }

    String generoInicial = preferences.getString("usergenero", "Hombre");
    if (generoPreference != null) {
      generoPreference.setValue(generoInicial);
    }

    int edad = preferences.getInt("useredad", -1);
    if (edad != -1) {
      Calendar calendar = Calendar.getInstance();
      int currentYear = calendar.get(Calendar.YEAR);
      int yearOfBirth = currentYear - edad;

      if (fechaNacimientoPreference != null) {
        fechaNacimientoPreference.setText(String.valueOf(yearOfBirth));
      }
    }
  }

  private final SharedPreferences.OnSharedPreferenceChangeListener preferenceChangeListener = (sharedPreferences, key) -> {
    handlePreferenceChanges(sharedPreferences, key);
  };

  private void handlePreferenceChanges(SharedPreferences sharedPreferences, String key) {
    switch (key) {
      case "peso":
        handlePesoChange(sharedPreferences);
        break;
      case "genero":
        handleGeneroChange(sharedPreferences);
        break;
      case "objetivos":
        handleObjetivosChange(sharedPreferences);
        break;
      case "idioma":
        handleIdiomaChange(sharedPreferences);
        break;
      case "notificaciones":
        handleNotificacionesChange(sharedPreferences);
        break;
      case "hora_notificacion":
      case "intervalo_notificacion":
        NotificationScheduler.scheduleDailyNotification(getContext());
        break;
      default:
        break;
    }
  }

  private void handlePesoChange(SharedPreferences sharedPreferences) {
    String pesoString = sharedPreferences.getString("peso", null);
    int nuevoPeso = -1;
    if (pesoString != null) {
      try {
        nuevoPeso = Integer.parseInt(pesoString);
      } catch (NumberFormatException e) {
        // Manejar el caso de número inválido
      }
    }
    if (pesoPreference != null && nuevoPeso != -1) {
      pesoPreference.setText(String.valueOf(nuevoPeso));
    }
  }

  private void handleGeneroChange(SharedPreferences sharedPreferences) {
    String nuevoGenero = sharedPreferences.getString("genero", "Hombre");
    if (generoPreference != null) {
      generoPreference.setValue(nuevoGenero);
    }
    SharedPreferences.Editor editor = sharedPreferences.edit();
    editor.putString("usergenero", nuevoGenero);
    editor.apply();
  }

  private void handleObjetivosChange(SharedPreferences sharedPreferences) {
    String nuevoObjetivo = sharedPreferences.getString("objetivos", "Perder Peso");
    if (objetivosPreference != null) {
      objetivosPreference.setValue(nuevoObjetivo);
    }
  }

  private void handleNotificacionesChange(SharedPreferences sharedPreferences) {
    boolean notificacionesActivas = sharedPreferences.getBoolean("notificaciones", false);
    if (notificacionesActivas) {
      NotificationScheduler.scheduleDailyNotification(getContext());
    } else {
      NotificationScheduler.cancelDailyNotification(getContext());
    }
  }

  private void handleIdiomaChange(SharedPreferences sharedPreferences) {
    String nuevoIdioma = sharedPreferences.getString("idioma", "Español, España");

    if (nuevoIdioma.equals("Español, España")) {
      setLanguage("es", "ES");
    } else if (nuevoIdioma.equals("Inglés, Inglaterra")) {
      setLanguage("en", "GB");
    }

    restartActivity();
  }

  private void setLanguage(String languageCode, String countryCode) {
    Locale locale = new Locale(languageCode, countryCode);
    Locale.setDefault(locale);

    Configuration configuration = getContext().getResources().getConfiguration();
    configuration.setLocale(locale); // Establece el nuevo idioma en la configuración de la aplicación
    getContext().getResources().updateConfiguration(configuration, getContext().getResources().getDisplayMetrics());
  }


  private void restartActivity() {
    Intent intent = new Intent(getContext(), getActivity().getClass());
    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
    startActivity(intent);
  }

  @Override
  public void onDestroy() {
    super.onDestroy();
    getPreferenceManager().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(preferenceChangeListener);
  }
}
