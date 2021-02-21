package prj1.stu_1505005.Utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * SharedPreferences Keys:
 * "login" for control login - log out
 * "id"
 * "dn"
 * "un"
 * "pw"
 * "shash"
 * "phone"
 * "country"
 */
public class SharedPrefs {
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor sharedPreferencesEdit;

    public SharedPrefs(Context context) {
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        sharedPreferencesEdit = sharedPreferences.edit();
    }

    public void save(String key, String val)
    {
        sharedPreferencesEdit.putString(key, val);
        sharedPreferencesEdit.commit();
    }

    public String get(String key)
    {
        return sharedPreferences.getString(key, "");
    }

    public void clearStoredData(){
        sharedPreferencesEdit.clear().commit();
    }

    public void setLogin(){
        sharedPreferencesEdit.putInt("login", 1);
        sharedPreferencesEdit.commit();
    }

    public boolean isLoggedIn() {
        return (sharedPreferences.getInt("login", 0) == 1);
    }
}
