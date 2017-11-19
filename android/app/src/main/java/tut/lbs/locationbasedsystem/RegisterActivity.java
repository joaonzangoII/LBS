package tut.lbs.locationbasedsystem;

import android.os.Handler;
import android.os.Message;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Toast;

import tut.lbs.locationbasedsystem.managers.Session;
import tut.lbs.locationbasedsystem.requests.AuthRequest;
import tut.lbs.locationbasedsystem.utils.Constant;

public class RegisterActivity extends BaseActivity {
    private AutoCompleteTextView edtName;
    private AutoCompleteTextView edtIdNumber;
    private AutoCompleteTextView edtEmail;
    private AutoCompleteTextView edtContact;
    private AutoCompleteTextView edtAddress;
    private AutoCompleteTextView edtPassword;
    private AutoCompleteTextView edtConfirmPassword;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        setTitle("Registration");
        final Session session = new Session(this);
        // image = (ImageView) findViewById(R.id.image);
        edtName = (AutoCompleteTextView) findViewById(R.id.name);
        edtIdNumber = (AutoCompleteTextView) findViewById(R.id.id_number);
        edtEmail  = (AutoCompleteTextView) findViewById(R.id.email);
        edtContact = (AutoCompleteTextView) findViewById(R.id.contact);
        edtAddress = (AutoCompleteTextView) findViewById(R.id.address);
        edtPassword = (AutoCompleteTextView) findViewById(R.id.password);
        edtConfirmPassword = (AutoCompleteTextView) findViewById(R.id.confirm_password);

        final String[] sexosArray = new String[]{"Masculino",
                "Feminino"};
        final ArrayAdapter<String> adapter =
                new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, sexosArray);

        if (savedInstanceState != null) {
            //if there is a bundle, use the saved image resource (if one is there)
            // thumbnail = (Bitmap) savedInstanceState.getParcelable("BitmapImage");
            // image.setImageBitmap(thumbnail);
            //textTargetUri.setText(savedInstanceState.getString("path_to_picture"));
        }

        //final ImageButton btnUploadImage = (ImageButton) findViewById(R.id.upload_image);
        final Button btnRegister = (Button) findViewById(R.id.btn_sign_up);
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                final String name = edtName.getText().toString();
                final String id_number = edtIdNumber.getText().toString();
                final String email = edtEmail.getText().toString();
                final String contact = edtContact.getText().toString();
                final String address = edtAddress.getText().toString();
                final String password = edtPassword.getText().toString();
                final String confirmPassword = edtConfirmPassword.getText().toString();
                final String encodemage = ""; //ImageUtil.convert(thumbnail);

                AuthRequest.register(session,
                        RegisterActivity.this,
                        name,
                        id_number,
                        email,
                        contact,
                        address,
                        password,
                        confirmPassword,
                        encodemage,
                        requestHandler);
            }
        });


       /* btnUploadImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                selectImage();
            }
        });*/
    }


    final Handler requestHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message message) {
            final Bundle data = message.getData();
            final boolean success = data.getBoolean(Constant.KEY_SUCCESS);
            if (success) {
                Toast.makeText(RegisterActivity.this,
                        "You have been successfully registered!",
                        Toast.LENGTH_SHORT)
                        .show();
                goToActivity(LoginActivity.class);
            }
            return false;
        }
    });
}
