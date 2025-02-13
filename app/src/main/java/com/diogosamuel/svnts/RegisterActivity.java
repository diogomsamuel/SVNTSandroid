package com.diogosamuel.svnts;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import com.diogosamuel.svnts.utils.ValidationUtils;
import com.diogosamuel.svnts.utils.UIUtils;

public class RegisterActivity extends AppCompatActivity {
    private EditText firstNameInput;
    private EditText lastNameInput;
    private EditText emailInput;
    private EditText passwordInput;
    private Spinner genderSpinner;
    private EditText heightInput;
    private EditText weightInput;
    private Button registerButton;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // Inicializar componentes
        initializeViews();
        setupGenderSpinner();
        
        dbHelper = new DatabaseHelper(this);

        registerButton.setOnClickListener(v -> attemptRegister());
    }

    private void initializeViews() {
        firstNameInput = findViewById(R.id.firstNameInput);
        lastNameInput = findViewById(R.id.lastNameInput);
        emailInput = findViewById(R.id.emailRegisterInput);
        passwordInput = findViewById(R.id.passwordRegisterInput);
        genderSpinner = findViewById(R.id.genderSpinner);
        heightInput = findViewById(R.id.heightInput);
        weightInput = findViewById(R.id.weightInput);
        registerButton = findViewById(R.id.registerButton);
    }

    private void setupGenderSpinner() {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.gender_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        genderSpinner.setAdapter(adapter);
    }

    private void attemptRegister() {
        // Obter valores dos campos
        String firstName = firstNameInput.getText().toString().trim();
        String lastName = lastNameInput.getText().toString().trim();
        String email = emailInput.getText().toString().trim();
        String password = passwordInput.getText().toString().trim();
        String gender = genderSpinner.getSelectedItem().toString();
        
        // Validar campos vazios
        if (firstName.isEmpty() || lastName.isEmpty() || email.isEmpty() || 
            password.isEmpty() || heightInput.getText().toString().isEmpty() || 
            weightInput.getText().toString().isEmpty()) {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        // Validar formato do email e senha
        if (!ValidationUtils.isValidEmail(email)) {
            emailInput.setError("Invalid email");
            return;
        }

        if (!ValidationUtils.isValidPassword(password)) {
            passwordInput.setError("Password must be at least 6 characters");
            return;
        }

        double height = Double.parseDouble(heightInput.getText().toString());
        double weight = Double.parseDouble(weightInput.getText().toString());

        // Mostrar loading
        UIUtils.showLoading(this, "Creating account...");

        // Criar usuário no banco de dados
        long userId = dbHelper.createUser(email, password, firstName, lastName, gender, height, weight);

        if (userId != -1) {
            // Registro bem sucedido
            UIUtils.hideLoading();
            startActivity(new Intent(RegisterActivity.this, MainActivity.class));
            finish();
        } else {
            // Falha no registro
            UIUtils.hideLoading();
            UIUtils.showError(findViewById(android.R.id.content), 
                "Error creating account. Please try again.");
        }
    }

    private boolean saveUserToDatabase(User user, String password) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(DatabaseHelper.COLUMN_EMAIL, user.getEmail());
        values.put(DatabaseHelper.COLUMN_PASSWORD, password); // Em produção, deve ser hash
        values.put(DatabaseHelper.COLUMN_FIRST_NAME, user.getFirstName());
        values.put(DatabaseHelper.COLUMN_LAST_NAME, user.getLastName());
        values.put(DatabaseHelper.COLUMN_GENDER, user.getGender());
        values.put(DatabaseHelper.COLUMN_HEIGHT, user.getHeight());
        values.put(DatabaseHelper.COLUMN_WEIGHT, user.getWeight());

        long newRowId = db.insert(DatabaseHelper.TABLE_USERS, null, values);
        return newRowId != -1;
    }
} 