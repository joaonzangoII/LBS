package tut.lbs.locationbasedsystem;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import tut.lbs.locationbasedsystem.managers.Session;
import tut.lbs.locationbasedsystem.utils.Constant;

public class BaseActivity  extends AppCompatActivity {
    public <T> void goToActivity(final Class<T> clazz) {
        final Intent intent = new Intent(BaseActivity.this, clazz);
        startActivity(intent);
    }

    public <T> void goToActivity(final Class<T> clazz,
                                 final boolean finish) {
        final Intent intent = new Intent(BaseActivity.this, clazz);
        finish();
        startActivity(intent);
    }

    public <T> void goToActivity(final Class<T> clazz,
                                 final Bundle bundle) {
        final Intent intent = new Intent(BaseActivity.this, clazz);
        intent.putExtra(Constant.KEY_DATA, bundle);
        startActivity(intent);
    }

    public <T> void goToActivity(final Class<T> clazz,
                                 final Bundle bundle,
                                 final boolean finish) {
        final Intent intent = new Intent(BaseActivity.this, clazz);
        intent.putExtra(Constant.KEY_DATA, bundle);
        finish();
        startActivity(intent);
    }

    public void logout(final Session session) {
        session.setLogin(false);
        session.setLoggedinUser(null);
        session.setTrips("[]");
        goToActivity(LoginActivity.class);
    }
}
