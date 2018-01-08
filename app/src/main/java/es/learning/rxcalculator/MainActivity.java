package es.learning.rxcalculator;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnTextChanged;
import io.reactivex.Observable;
import io.reactivex.subjects.PublishSubject;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.txtA)
    protected EditText txtA;

    @BindView(R.id.txtB)
    protected EditText txtB;

    @BindView(R.id.lblSum)
    protected TextView lblSum;

    @BindView(R.id.lblMultiply)
    protected TextView lblMultiply;

    private PublishSubject<Double> subjectA = PublishSubject.create();
    private PublishSubject<Double> subjectB = PublishSubject.create();

    private Observable<Double> sumObservable = null;
    private Observable<Double> muliplyObservable = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        this.sumObservable = Observable.combineLatest(subjectA, subjectB, (a, b) -> a + b);
        this.sumObservable.subscribe((Double sum) -> {
            Log.d("RX", "Sum " + sum);
            this.lblSum.setText(sum.toString());
        }, (Throwable throwable) -> {
            Log.e("RX", Log.getStackTraceString(throwable));
        });

        this.muliplyObservable = Observable.combineLatest(subjectA, subjectB, (a, b) -> a * b);
        this.muliplyObservable.subscribe((Double multi) -> {
            Log.d("RX", "Multiply " + multi);
            this.lblMultiply.setText(multi.toString());
        }, (Throwable throwable) -> {
            Log.e("RX", Log.getStackTraceString(throwable));
        });

        this.subjectA.onNext(0d);
        this.subjectB.onNext(0d);
    }

    @OnTextChanged(R.id.txtA)
    protected void txtAChanged() {

        try {
            Log.d("RX", "txtA : " + this.txtA.getText().toString());
            this.subjectA.onNext(Double.parseDouble(this.txtA.getText().toString()));
        } catch (Exception e) {

        }
    }

    @OnTextChanged(R.id.txtB)
    protected void txtBChanged() {

        try {
            Log.d("RX", "txtB : " + this.txtB.getText().toString());
            this.subjectB.onNext(Double.parseDouble(this.txtB.getText().toString()));
        } catch (Exception e) {

        }
    }
}
