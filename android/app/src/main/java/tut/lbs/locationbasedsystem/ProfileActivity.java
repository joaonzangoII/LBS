package tut.lbs.locationbasedsystem;

import android.os.Handler;
import android.os.Message;
import android.os.Bundle;
import android.support.v7.widget.AppCompatTextView;
import android.util.Log;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;

import com.google.firebase.iid.FirebaseInstanceId;

import de.hdodenhof.circleimageview.CircleImageView;
import tut.lbs.locationbasedsystem.adapters.GlideAdapter;
import tut.lbs.locationbasedsystem.managers.Session;
import tut.lbs.locationbasedsystem.models.User;
import tut.lbs.locationbasedsystem.requests.AuthRequest;
import tut.lbs.locationbasedsystem.utils.Constant;
import tut.lbs.locationbasedsystem.utils.MyInstanceIDListenerService;

public class ProfileActivity extends BaseActivity {
    private static String TAG = ProfileActivity.class.getName();

    private Session sessionManager;
    private CircleImageView profileImage;
    private AutoCompleteTextView edtName;
    private AutoCompleteTextView edtIdNumber;
    private AutoCompleteTextView edtEmail;
    private AutoCompleteTextView edtContact;
    private AutoCompleteTextView edtAddress;

    private AppCompatTextView txtName;
    private AppCompatTextView txtIdNumber;
    private AppCompatTextView txtContact;
    private AppCompatTextView txtAddress;
    private AppCompatTextView txtEmail;
    private AppCompatTextView txtToken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        sessionManager = new Session(ProfileActivity.this);
        final User user = sessionManager.getLoggedInUser();
        // Get token
        final String token = FirebaseInstanceId.getInstance().getToken();
        // Log and toast
        final String msg = getString(R.string.msg_token_fmt, token);
        Log.e(TAG, msg);

        txtName = (AppCompatTextView) findViewById(R.id.name);
        txtIdNumber = (AppCompatTextView) findViewById(R.id.id_number);
        txtContact = (AppCompatTextView) findViewById(R.id.contact);
        txtAddress = (AppCompatTextView) findViewById(R.id.address);
        txtEmail = (AppCompatTextView) findViewById(R.id.email);
        txtToken = (AppCompatTextView) findViewById(R.id.token);

        edtName = (AutoCompleteTextView) findViewById(R.id.edt_name);
        edtIdNumber = (AutoCompleteTextView) findViewById(R.id.edt_id_number);
        edtEmail = (AutoCompleteTextView) findViewById(R.id.edt_email);
        edtContact = (AutoCompleteTextView) findViewById(R.id.edt_contact);
        edtAddress = (AutoCompleteTextView) findViewById(R.id.edt_address);

        txtName.setText(user.name);
        txtIdNumber.setText(user.id_number);
        txtContact.setText(user.contact);
        txtEmail.setText(user.email);
        txtAddress.setText(user.address);
        txtToken.setText(token);

        edtName.setText(user.name);
        edtIdNumber.setText(user.id_number);
        edtEmail.setText(user.email);
        edtContact.setText(user.contact);
        edtAddress.setText(user.address);

        profileImage = (CircleImageView) findViewById(R.id.profile_image);
        GlideAdapter.setImage(ProfileActivity.this,
                user.image,
                profileImage);

        final Button btnUpdateProfile = (Button) findViewById(R.id.update_profile);
        btnUpdateProfile.setOnClickListener(new View.OnClickListener() {
            public void onClick(final View view) {
                AuthRequest.update(sessionManager,
                        ProfileActivity.this,
                        edtName.getText().toString(),
                        edtIdNumber.getText().toString(),
                        edtEmail.getText().toString(),
                        edtContact.getText().toString(),
                        edtAddress.getText().toString(),
                        String.valueOf(user.id),
                        requestHandler);
            }
        });



    }


    private Handler requestHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(final Message message) {
            final Bundle data = message.getData();
            final boolean error = data.getBoolean(Constant.KEY_SUCCESS);
            if (!error) {
                final User user = sessionManager.getLoggedInUser();
                GlideAdapter.setImage(ProfileActivity.this,
                        user.image,
                        profileImage);
                edtName.setText(user.name);
                edtIdNumber.setText(user.id_number);
                edtEmail.setText(user.email);
                edtContact.setText(user.contact);
                edtAddress.setText(user.address);

                txtName.setText(user.name);
                txtIdNumber.setText(user.id_number);
                txtEmail.setText(user.email);
                txtContact.setText(user.contact);
                txtAddress.setText(user.address);
            }

            return false;
        }
    });
}
