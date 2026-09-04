package edu.fgcu.habithero.ui.login;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.text.TextUtils;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.google.android.material.textfield.TextInputEditText;

import edu.fgcu.habithero.R;

public class LoginFragment extends Fragment {
    private TextInputEditText inputName;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_login, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        inputName = view.findViewById(R.id.inputName);
        view.findViewById(R.id.btnContinue).setOnClickListener(v -> {
            String name = inputName.getText() != null ? inputName.getText().toString().trim() : "";
            if (TextUtils.isEmpty(name)) {
                inputName.setError(getString(R.string.error_required));
                return;
            }
            SharedPreferences sp = requireActivity().getSharedPreferences("habit_prefs", getContext().MODE_PRIVATE);
            sp.edit().putString("user_name", name).apply();
            NavHostFragment.findNavController(this).navigate(R.id.action_login_to_list);
        });
    }
}
