package android.test.cicerocipriano.helloworld.pratica1_2;

import static android.text.TextUtils.split;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    EditText screen;
    Button zero, one, two, three, four, five, six, seven, eight, nine;
    Button plus, minus, times, divide, dot, backspace, clear, equal;
    String screenText = "";
    boolean opDone = false, error = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        screen = findViewById(R.id.textBox);
        zero = findViewById(R.id.btn0);
        one = findViewById(R.id.btn1);
        two = findViewById(R.id.btn2);
        three = findViewById(R.id.btn3);
        four = findViewById(R.id.btn4);
        five = findViewById(R.id.btn5);
        six = findViewById(R.id.btn6);
        seven = findViewById(R.id.btn7);
        eight = findViewById(R.id.btn8);
        nine = findViewById(R.id.btn9);
        plus = findViewById(R.id.btnPlus);
        minus = findViewById(R.id.btnMinus);
        times = findViewById(R.id.btnTimes);
        divide = findViewById(R.id.btnDivide);
        dot = findViewById(R.id.btnDot);
        backspace = findViewById(R.id.btnBackspace);
        clear = findViewById(R.id.btnClear);
        equal = findViewById(R.id.btnEqual);
        zero.setOnClickListener(v -> onPressedButton(false, 0));
        one.setOnClickListener(v -> onPressedButton(false, 1));
        two.setOnClickListener(v -> onPressedButton(false, 2));
        three.setOnClickListener(v -> onPressedButton(false, 3));
        four.setOnClickListener(v -> onPressedButton(false, 4));
        five.setOnClickListener(v -> onPressedButton(false, 5));
        six.setOnClickListener(v -> onPressedButton(false, 6));
        seven.setOnClickListener(v -> onPressedButton(false, 7));
        eight.setOnClickListener(v -> onPressedButton(false, 8));
        nine.setOnClickListener(v -> onPressedButton(false, 9));
        plus.setOnClickListener(v -> onPressedButton(true, 0));
        minus.setOnClickListener(v -> onPressedButton(true, 1));
        times.setOnClickListener(v -> onPressedButton(true, 2));
        divide.setOnClickListener(v -> onPressedButton(true, 3));
        dot.setOnClickListener(v -> onPressedButton(true, 4));
        backspace.setOnClickListener(v -> onPressedButton(true, 5));
        clear.setOnClickListener(v -> onPressedButton(true, 6));
        equal.setOnClickListener(v -> onPressedButton(true, 7));
        screen.setText("0");
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void onPressedButton(Boolean op, int id){
        if(op) {
            operation(id);
            return;
        }
        number(id);
    }

    private void operation(int id){
        if(id >= 0 && id < 4){
            if(id == 0) screenText = screenText + "+";
            else if (id == 1) screenText = screenText + "-";
            else if (id == 2) screenText = screenText + "*";
            else screenText = screenText + "/";
            screen.setText(screenText);
        } else if(id == 4) dot();
        else if(id == 5 && !screenText.isEmpty()){
            screenText = screenText.substring(0, screenText.length() - 1);
            if(screenText.isEmpty()) screen.setText("0");
            else screen.setText(screenText);
        }else if(id == 6){
            screenText = "";
            screen.setText("0");
        }else equal();
    }

    private void dot(){
        if(screenText.isEmpty()) return;
        int idx = 0;
        for(int i = 0; i < screenText.length(); i++)
            if(screenText.charAt(i) == '+' || screenText.charAt(i) == '-' || screenText.charAt(i) == '*' || screenText.charAt(i) == '/')
                idx = i;
        if(idx == screenText.length() - 1 && idx > 0) return;
        if(idx == 0){
            if(!screenText.contains(".")){
                screenText = screenText + ".";
                screen.setText(screenText);
            }
        }else{
            String aux = screenText.substring(idx + 1, screenText.length() - 1);
            if(!(aux.isEmpty() || aux.contains("."))){
                screenText = screenText + ".";
                screen.setText(screenText);
            }
        }
    }

    private void equal(){
        if (screenText.isEmpty()) return;
        boolean[] appears = {false, false, false, false};
        int ops = 0, idx = -1;
        String[] aux = {"\\+", "-", "\\*", "/"};
        for(int i = 0; i < screenText.length(); i++) {
            if (screenText.charAt(i) == '+'){
                appears[0] = true;
                idx = 0;
            } else if(screenText.charAt(i) == '-'){
                appears[1] = true;
                idx = 1;
            } else if(screenText.charAt(i) == '*'){
                appears[2] = true;
                idx = 2;
            } else if(screenText.charAt(i) == '/'){
                appears[3] = true;
                idx = 3;
            }
        }
        for(int i = 0; i < 4; i++){
            if(appears[i]) ops++;
            if(ops > 1){
                mistake();
                return;
            }
        }
        if(ops == 0) return;
        for(int i = 0; i < screenText.length() - 1; i++){
            if(screenText.charAt(i) == screenText.charAt(i + 1) &&
               (screenText.charAt(i) == '+' ||
                screenText.charAt(i) == '-' ||
                screenText.charAt(i) == '*' ||
                screenText.charAt(i) == '/')){
                mistake();
                return;
            }
        }
        String[] tokens = screenText.split(aux[idx]);
        double[] numbers = new double[tokens.length];
        for(int i = 0; i < tokens.length; i++)
            numbers[i] = Double.parseDouble(tokens[i]);
        double result = numbers[0];
        if(idx == 0)
            for(int i = 1; i < numbers.length; i++)
                result += numbers[i];
        else if(idx == 1)
            for(int i = 1; i < numbers.length; i++)
                result -= numbers[i];
        else if(idx == 2)
            for(int i = 1; i < numbers.length; i++)
                result *= numbers[i];
        else
            for(int i = 1; i < numbers.length; i++){
                if(numbers[i] == 0) {
                    mistake();
                    return;
                }
                result /= numbers[i];
            }
        screenText = Double.toString(result);
        screen.setText(screenText);
        opDone = true;
    }

    private void mistake(){
        screenText = "";
        screen.setText("ERROR");
        opDone = error = true;
    }

    private void number(int id){
        if(opDone){
            opDone = false;
            if(error ||
               !(screenText.charAt(screenText.length() - 1) == '+' ||
               screenText.charAt(screenText.length() - 1) == '-' ||
               screenText.charAt(screenText.length() - 1) == '*' ||
               screenText.charAt(screenText.length() - 1) == '/')){
                screenText = "";
                error = false;
            }
        }
        add_number(id);
    }

    private void add_number(int id){
        if(id == 0) screenText = screenText + "0";
        else if (id == 1) screenText = screenText + "1";
        else if (id == 2) screenText = screenText + "2";
        else if (id == 3) screenText = screenText + "3";
        else if (id == 4) screenText = screenText + "4";
        else if (id == 5) screenText = screenText + "5";
        else if (id == 6) screenText = screenText + "6";
        else if (id == 7) screenText = screenText + "7";
        else if (id == 8) screenText = screenText + "8";
        else screenText = screenText + "9";
        screen.setText(screenText);
    }
}