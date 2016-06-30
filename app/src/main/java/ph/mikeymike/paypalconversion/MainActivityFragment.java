package ph.mikeymike.paypalconversion;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.squareup.okhttp.ResponseBody;

import java.io.IOException;
import java.lang.annotation.Annotation;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import network.ApiManager;
import network.response.ConvertResponse;
import network.response.ErrorResponse;
import retrofit.Call;
import retrofit.Callback;
import retrofit.Converter;
import retrofit.Response;
import retrofit.Retrofit;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {

    @Bind(R.id.progressBar)
    ProgressBar mProgressBar;

    @Bind(R.id.et_amount_in)
    EditText etAmountIn;

    @Bind(R.id.et_country_currency_code_out)
    EditText etCountryCodeOut;

    @Bind(R.id.et_country_currency_code_in)
    EditText etCountryCodeIn;

    @Bind(R.id.tv_current_rate)
    TextView tvCurrentRate;

    @Bind(R.id.tv_amount_result)
    TextView tvAmountResult;


    public MainActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_main, container, false);

        ButterKnife.bind(this, view);


        etAmountIn.requestFocus();

        InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(etAmountIn, 0);
        return view;
    }


    @OnClick(R.id.btn_check)
    public void checkConvertedAmount(){

        if (etAmountIn != null) {
            InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(etAmountIn.getWindowToken(), 0);
        }

        mProgressBar.setVisibility(View.VISIBLE);

        String amountIn = etAmountIn.getText().toString();
        String ccIn = etCountryCodeIn.getText().toString().toUpperCase();
        String ccOut = etCountryCodeOut.getText().toString().toUpperCase();

        if(TextUtils.isEmpty(amountIn) || TextUtils.isEmpty(ccIn) || TextUtils.isEmpty(ccOut)){
            mProgressBar.setVisibility(View.GONE);
            Snackbar.make(getView(),"Do you know even know how to convert things?",Snackbar.LENGTH_SHORT)
                    .show();
            return;
        }

        Call<ConvertResponse> call = ApiManager.getApi().getPayPalConvertedAmount("_manage-currency-conversion"
        ,amountIn,ccIn, ccOut.toString(),"BalanceConversion");

        call.enqueue(new Callback<ConvertResponse>() {
            @Override
            public void onResponse(Response<ConvertResponse> response, Retrofit retrofit) {

                mProgressBar.setVisibility(View.GONE);


                if (response != null && !response.isSuccess() && response.errorBody() != null) {
                    Converter<ResponseBody, ErrorResponse> errorConverter =
                            retrofit.responseConverter(ErrorResponse.class, new Annotation[0]);
                    try {
                        ErrorResponse error = errorConverter.convert(response.errorBody());

                        Snackbar.make(getView(),error.getError(),Snackbar.LENGTH_SHORT).show();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    //DO ERROR HANDLING HERE
                    return;
                }

                if(response.isSuccess()){
                        ConvertResponse body = response.body();
                    Animation myAnimation = AnimationUtils.loadAnimation(getActivity(), R.anim.result);
                            tvCurrentRate.setText(body.getExchangeRateFormatted());
                            tvAmountResult.setText(body.getAmountOutFormatted());
                    tvAmountResult.startAnimation(myAnimation);
                }
            }

            @Override
            public void onFailure(Throwable t) {
                t.printStackTrace();
                Log.d("Mikey", t.getMessage());
            }
        });
    }


}
