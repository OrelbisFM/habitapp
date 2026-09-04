package edu.fgcu.habithero.ui.list;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.content.DialogInterface;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.android.material.snackbar.Snackbar;

import edu.fgcu.habithero.R;
import edu.fgcu.habithero.data.Habit;
import edu.fgcu.habithero.vm.HabitViewModel;

public class HabitListFragment extends Fragment implements HabitAdapter.HabitItemListener {
    private HabitViewModel vm;
    private HabitAdapter adapter;
    private TextView tvEmpty;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_habit_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        RecyclerView recycler = view.findViewById(R.id.recyclerHabits);
        tvEmpty = view.findViewById(R.id.tvEmpty);
        view.findViewById(R.id.fabAdd).setOnClickListener(v ->
                NavHostFragment.findNavController(this).navigate(R.id.action_list_to_add));

        adapter = new HabitAdapter(this);
        recycler.setLayoutManager(new LinearLayoutManager(requireContext()));
        recycler.setAdapter(adapter);

        vm = new ViewModelProvider(requireActivity()).get(HabitViewModel.class);
        vm.getHabits().observe(getViewLifecycleOwner(), habits -> {
            adapter.submitList(habits);
            tvEmpty.setVisibility((habits == null || habits.isEmpty()) ? View.VISIBLE : View.GONE);
        });
    }

    @Override
    public void onToggleToday(Habit habit, View anchorView) {
        boolean allowed = vm.toggleToday(habit);
        if (!allowed) {
            Snackbar.make(anchorView, getString(R.string.snack_today_not_allowed), Snackbar.LENGTH_SHORT).show();
            return;
        }
        Snackbar.make(anchorView, getString(R.string.snack_completed), Snackbar.LENGTH_SHORT).show();

        int index = adapter.getCurrentList().indexOf(habit);
        if (index != -1) {
            adapter.notifyItemChanged(index);
        }
    }
    @Override
    public void onLongPress(Habit habit) {
        new AlertDialog.Builder(requireContext())
                .setTitle(habit.title)
                .setMessage("Delete this habit?")
                .setPositiveButton("Delete", (dialog, which) -> vm.delete(habit))
                .setNegativeButton("Cancel", null)
                .show();
    }
}
