package pl.lodz.it.sitodruk.utils;

import java.util.Locale;
import java.util.ResourceBundle;

public class MessageProvider {

    public static String getTranslatedText(String key, String language) {
        Locale locale;
        if(language == null){
            locale = new Locale("pl");
        }
        else if(language.isBlank())
            locale = new Locale("pl");
        else locale = new Locale(language);
        return ResourceBundle.getBundle("i18n/messages", locale).getString(key);
    }
}