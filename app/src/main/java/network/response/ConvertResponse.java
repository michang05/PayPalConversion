package network.response;

import org.simpleframework.xml.Element;

/**
 * Created by Mikey on 12/16/2015.
 */
public class ConvertResponse {


    @Element(name = "amount_out_formatted")
    private String amountOutFormatted;

    @Element(name = "from_currency_code")
    private String fromCurrencyCode;

    @Element(name = "exchange_rate_formatted")
    private String exchangeRateFormatted;

    public String getAmountOutFormatted() {
        return amountOutFormatted;
    }

    public void setAmountOutFormatted(String amountOutFormatted) {
        this.amountOutFormatted = amountOutFormatted;
    }

    public String getFromCurrencyCode() {
        return fromCurrencyCode;
    }

    public void setFromCurrencyCode(String fromCurrencyCode) {
        this.fromCurrencyCode = fromCurrencyCode;
    }

    public String getExchangeRateFormatted() {
        return exchangeRateFormatted;
    }

    public void setExchangeRateFormatted(String exchangeRateFormatted) {
        this.exchangeRateFormatted = exchangeRateFormatted;
    }
}
