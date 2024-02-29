package com.example.scansaga;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import java.io.Serializable;

public class AddUserFragment extends DialogFragment {

    interface AddUserDialogListener {
        void addUser(User user);
        void editUser(User user);
        void deleteUser(User user);
    }

    private User userToEdit;

    static AddUserFragment newInstance(User user) {
        Bundle args = new Bundle();
        args.putSerializable("user", user);
        AddUserFragment fragment = new AddUserFragment();
        fragment.setArguments(args);
        return fragment;
    }

    private AddUserDialogListener listener;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof AddUserDialogListener) {
            listener = (AddUserDialogListener) context;
        } else {
            throw new RuntimeException(context + " must implement AddUserDialogListener");
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.fragment_add_user, null);

        EditText editFirstName = view.findViewById(R.id.edit_text_firstname);
        EditText editLastName = view.findViewById(R.id.edit_text_lastname);
        EditText editEmail = view.findViewById(R.id.edit_text_email);
        EditText editPhone = view.findViewById(R.id.edit_text_phone);
        Button deleteButton = view.findViewById(R.id.button_delete);

        Bundle args = getArguments();
        if (args != null) {
            userToEdit = (User) args.getSerializable("user");

            if (userToEdit != null) {
                editFirstName.setText(userToEdit.getFirstname());
                editLastName.setText(userToEdit.getLastname());
                editEmail.setText(userToEdit.getEmail());
                editPhone.setText(userToEdit.getPhone());
            }
        }

        deleteButton.setOnClickListener(v -> {
            if (userToEdit != null) {
                listener.deleteUser(userToEdit);
                dismiss();
            } else {
                new AlertDialog.Builder(requireContext())
                        .setTitle("Empty User List")
                        .setMessage("Sorry, can't delete any users. The user list is empty.")
                        .setPositiveButton("OK", null)
                        .show();
            }
        });

        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        return builder
                .setView(view)
                .setTitle("Add/Edit a User")
                .setNegativeButton("Cancel", null)
                .setPositiveButton("Add", (dialog, which) -> {
                    String firstName = editFirstName.getText().toString();
                    String lastName = editLastName.getText().toString();
                    String email = editEmail.getText().toString();
                    String phone = editPhone.getText().toString();

                    if (firstName.isEmpty() || lastName.isEmpty() || email.isEmpty() || phone.isEmpty()) {
                        new AlertDialog.Builder(requireContext())
                                .setTitle("Error! Incomplete input fields")
                                .setMessage("Please fill in all details of the user")
                                .setPositiveButton("OK", null)
                                .show();
                    } else {
                        if (userToEdit != null) {
                            userToEdit.setFirstname(firstName);
                            userToEdit.setLastname(lastName);
                            userToEdit.setEmail(email);
                            userToEdit.setPhone(phone);
                            listener.editUser(userToEdit);
                        } else {
                            listener.addUser(new User(lastName, firstName, email, phone));
                        }
                    }
                })
                .create();
    }
}
