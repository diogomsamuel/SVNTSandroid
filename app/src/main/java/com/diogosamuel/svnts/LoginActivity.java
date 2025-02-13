package com.diogosamuel.svnts;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.diogosamuel.svnts.utils.UIUtils;
import com.diogosamuel.svnts.utils.ValidationUtils;

public class LoginActivity extends AppCompatActivity {
    private EditText emailInput;
    private EditText passwordInput;
    private Button loginButton;
    private TextView registerLink;
    private DatabaseHelper dbHelper;
    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Inicializar componentes
        emailInput = findViewById(R.id.emailInput);
        passwordInput = findViewById(R.id.passwordInput);
        loginButton = findViewById(R.id.loginButton);
        registerLink = findViewById(R.id.registerLink);
        dbHelper = new DatabaseHelper(this);
        sessionManager = new SessionManager(this);

        // Se já estiver logado, ir direto para MainActivity
        if (sessionManager.isLoggedIn()) {
            startActivity(new Intent(LoginActivity.this, MainActivity.class));
            finish();
            return;
        }

        // Configurar listeners
        loginButton.setOnClickListener(v -> attemptLogin());
        registerLink.setOnClickListener(v -> startActivity(new Intent(LoginActivity.this, RegisterActivity.class)));
    }

    private void attemptLogin() {
        String email = emailInput.getText().toString().trim();
        String password = passwordInput.getText().toString().trim();

        // Validação de campos
        if (!ValidationUtils.isValidEmail(email)) {
            emailInput.setError("Email inválido");
            return;
        }

        if (!ValidationUtils.isValidPassword(password)) {
            passwordInput.setError("Senha deve ter pelo menos 6 caracteres");
            return;
        }

        // Mostrar loading
        UIUtils.showLoading(this, "Fazendo login...");

        // Verificar credenciais
        if (dbHelper.verifyUser(email, password)) {
            // Login bem sucedido
            long userId = dbHelper.getUserId(email);
            sessionManager.createLoginSession(userId);
            
            UIUtils.hideLoading();
            startActivity(new Intent(LoginActivity.this, MainActivity.class));
            finish();
        } else {
            // Login falhou
            UIUtils.hideLoading();
            UIUtils.showError(findViewById(android.R.id.content), 
                "Email ou senha incorretos");
        }
    }
} 