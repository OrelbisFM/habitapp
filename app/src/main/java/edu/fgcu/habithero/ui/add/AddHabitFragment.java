package edu.fgcu.habithero.ui.add;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.text.TextUtils;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;

import edu.fgcu.habithero.R;
import edu.fgcu.habithero.vm.HabitViewModel;

public class AddHabitFragment extends Fragment {
    private TextInputEditText inputTitle, inputNote;
    private AutoCompleteTextView freqAuto;
    private HabitViewModel vm;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_add_habit, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        inputTitle = view.findViewById(R.id.inputTitle);
        inputNote = view.findViewById(R.id.inputNote);
        freqAuto = view.findViewById(R.id.freqAutoComplete);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(requireContext(),
                R.array.frequency_options, android.R.layout.simple_list_item_1);
        freqAuto.setAdapter(adapter);

        vm = new ViewModelProvider(requireActivity()).get(HabitViewModel.class);

        view.findViewById(R.id.btnSave).setOnClickListener(v -> {
            String title = inputTitle.getText() != null ? inputTitle.getText().toString().trim() : "";
            String note = inputNote.getText() != null ? inputNote.getText().toString().trim() : "";
            String freqLabel = freqAuto.getText() != null ? freqAuto.getText().toString().trim() : "";

            if (TextUtils.isEmpty(title)) {
                inputTitle.setError(getString(R.string.error_required));
                return;
            }
            if (TextUtils.isEmpty(freqLabel)) {
                Snackbar.make(view, getString(R.string.error_required), Snackbar.LENGTH_SHORT).show();
                return;
            }

            String freqConst = "DAILY";
            if (freqLabel.equalsIgnoreCase("Weekdays")) freqConst = "WEEKDAYS";
            else if (freqLabel.equalsIgnoreCase("Weekends")) freqConst = "WEEKENDS";

            vm.addHabit(title, note, freqConst);
            Snackbar.make(view, getString(R.string.snack_saved), Snackbar.LENGTH_SHORT).show();
            NavHostFragment.findNavController(this).navigate(R.id.action_add_to_list);
        });
    }
}
