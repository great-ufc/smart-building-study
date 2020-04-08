package great.android.cmu.ubiapp.ui.notifications;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import great.android.cmu.ubiapp.R;
import great.android.cmu.ubiapp.helpers.Keywords;

import static great.android.cmu.ubiapp.MainActivity.sharedPrefs;
import static great.android.cmu.ubiapp.helpers.Keywords.DISTANCE_SWITCH;

public class NotificationsFragment extends Fragment {

    private NotificationsViewModel notificationsViewModel;
    private Switch switchCoap;
    private Switch switchDistance;

    private TextView tvRadius;
    private TextView tvInfoMessage;
    private EditText etRadius;

    private static int radius = 10;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        notificationsViewModel = ViewModelProviders.of(this).get(NotificationsViewModel.class);
        View root = inflater.inflate(R.layout.fragment_notifications, container, false);
        switchDistance = (Switch) root.findViewById(R.id.switchFiltro);
        switchCoap = (Switch) root.findViewById(R.id.switchCoap);
        tvRadius = (TextView) root.findViewById(R.id.textView2);
        tvInfoMessage = (TextView) root.findViewById(R.id.textView4);
        etRadius = (EditText) root.findViewById(R.id.editTextRaio);

        if(sharedPrefs.contains(Keywords.COAP_SWITCH)){
            switchCoap.setChecked(sharedPrefs.getBoolean(Keywords.COAP_SWITCH, false));
            switchDistance.setClickable(true);
        }else{
            switchDistance.setClickable(false);
        }
        if(sharedPrefs.contains(DISTANCE_SWITCH)){
            switchDistance.setChecked(sharedPrefs.getBoolean(Keywords.DISTANCE_SWITCH, false));
            boolean switchDistanceState = sharedPrefs.getBoolean(Keywords.DISTANCE_SWITCH, false);
            if(switchDistanceState){
                tvRadius.setVisibility(View.VISIBLE);
                tvInfoMessage.setVisibility(View.VISIBLE);
                etRadius.setVisibility(View.VISIBLE);

                etRadius.setText(String.valueOf(sharedPrefs.getInt(Keywords.RADIUS, 10)));
            }
        }

        switchCoap.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                sharedPrefs.edit().putBoolean(Keywords.CHANGED, true).commit();
                if(isChecked) {
                    sharedPrefs.edit().putBoolean(Keywords.COAP_SWITCH, true).commit();
                    switchDistance.setClickable(true);
                } else {
                    sharedPrefs.edit().putBoolean(Keywords.COAP_SWITCH, false).commit();
                    switchDistance.setChecked(false);
                    switchDistance.setClickable(false);
                }
            }
        });

        switchDistance.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                sharedPrefs.edit().putBoolean(Keywords.CHANGED, true).commit();
                if(isChecked) {
                    tvRadius.setVisibility(View.VISIBLE);
                    tvInfoMessage.setVisibility(View.VISIBLE);
                    etRadius.setVisibility(View.VISIBLE);
                    sharedPrefs.edit().putBoolean(DISTANCE_SWITCH, true).commit();
                } else {
                    tvRadius.setVisibility(View.INVISIBLE);
                    tvInfoMessage.setVisibility(View.INVISIBLE);
                    etRadius.setVisibility(View.INVISIBLE);
                    sharedPrefs.edit().putBoolean(DISTANCE_SWITCH, false).commit();
                }
            }
        });

        etRadius.addTextChangedListener(new TextWatcher(){
            @Override
            public void afterTextChanged(Editable mEdit){
                if(mEdit.length() != 0 && Integer.parseInt(mEdit.toString()) > 0){
                    sharedPrefs.edit().putBoolean(Keywords.CHANGED, true).commit();
                    radius = Integer.parseInt(mEdit.toString());
                    sharedPrefs.edit().putInt(Keywords.RADIUS, radius).commit();
                }
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after){}
            public void onTextChanged(CharSequence s, int start, int before, int count){}
        });

        return root;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }
}