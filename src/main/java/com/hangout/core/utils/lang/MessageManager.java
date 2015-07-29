package com.hangout.core.utils.lang;

import java.util.List;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

public class MessageManager {

    private final Locale locale;
    private final ResourceBundle bundle;

    public MessageManager(String language) {
        this(language, "messages");
    }

    public MessageManager(String language, String resourceName) {
        this.locale = new Locale(language);
        this.bundle = ResourceBundle.getBundle(resourceName, this.locale, new UTF8Control());
    }

    public ResourceBundle getResourceBundle() {
        return bundle;
    }

    public String getString(String key) {
        String value;
        try{
            value = bundle.getString(key);
        } catch(MissingResourceException e){
            return key;
        }
        if (value == null) {
            return key;
        }
        return value;
    }

    public Locale getLocale() {
        return locale;
    }

    public List<String> getStringList(String key) {
        return null;
    }
}
