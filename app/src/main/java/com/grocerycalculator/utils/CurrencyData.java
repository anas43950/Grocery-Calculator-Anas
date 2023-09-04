package com.grocerycalculator.utils;

import com.grocerycalculator.models.Currency;

import java.util.ArrayList;
import java.util.Arrays;

public class CurrencyData {

    static final Currency[] currencies = {
            new Currency("EUR", "Euro", "€", "flag_eur"),
            new Currency("USD", "United States Dollar", "$", "flag_usd"),
            new Currency("GBP", "British Pound", "£", "flag_gbp"),
            new Currency("CZK", "Czech Koruna", "Kč", "flag_czk"),
            new Currency("TRY", "Turkish Lira", "₺", "flag_try"),
            new Currency("AED", "Emirati Dirham", "د.إ", "flag_aed"),
            new Currency("AFN", "Afghanistan Afghani", "؋", "flag_afn"),
            new Currency("ARS", "Argentine Peso", "$", "flag_ars"),
            new Currency("AUD", "Australian Dollar", "$", "flag_aud"),
            new Currency("BBD", "Barbados Dollar", "$", "flag_bbd"),
            new Currency("BDT", "Bangladeshi Taka", " Tk", "flag_bdt"),
            new Currency("BGN", "Bulgarian Lev", "лв", "flag_bgn"),
            new Currency("BHD", "Bahraini Dinar", "BD", "flag_bhd"),
            new Currency("BMD", "Bermuda Dollar", "$", "flag_bmd"),
            new Currency("BND", "Brunei Darussalam Dollar", "$", "flag_bnd"),
            new Currency("BOB", "Bolivia Bolíviano", "$b", "flag_bob"),
            new Currency("BRL", "Brazil Real", "R$", "flag_brl"),
            new Currency("BTN", "Bhutanese Ngultrum", "Nu.", "flag_btn"),
            new Currency("BZD", "Belize Dollar", "BZ$", "flag_bzd"),
            new Currency("CAD", "Canada Dollar", "$", "flag_cad"),
            new Currency("CHF", "Switzerland Franc", "CHF", "flag_chf"),
            new Currency("CLP", "Chile Peso", "$", "flag_clp"),
            new Currency("CNY", "China Yuan Renminbi", "¥", "flag_cny"),
            new Currency("COP", "Colombia Peso", "$", "flag_cop"),
            new Currency("CRC", "Costa Rica Colon", "₡", "flag_crc"),
            new Currency("DKK", "Denmark Krone", "kr", "flag_dkk"),
            new Currency("DOP", "Dominican Republic Peso", "RD$", "flag_dop"),
            new Currency("EGP", "Egypt Pound", "£", "flag_egp"),
            new Currency("ETB", "Ethiopian Birr", "Br", "flag_etb"),
            new Currency("GEL", "Georgian Lari", "₾", "flag_gel"),
            new Currency("GHS", "Ghana Cedi", "¢", "flag_ghs"),
            new Currency("GMD", "Gambian dalasi", "D", "flag_gmd"),
            new Currency("GYD", "Guyana Dollar", "$", "flag_gyd"),
            new Currency("HKD", "Hong Kong Dollar", "$", "flag_hkd"),
            new Currency("HRK", "Croatia Kuna", "kn", "flag_hrk"),
            new Currency("HUF", "Hungary Forint", "Ft", "flag_huf"),
            new Currency("IDR", "Indonesia Rupiah", "Rp", "flag_idr"),
            new Currency("ILS", "Israel Shekel", "₪", "flag_ils"),
            new Currency("INR", "Indian Rupee", "₹", "flag_inr"),
            new Currency("ISK", "Iceland Krona", "kr", "flag_isk"),
            new Currency("JMD", "Jamaica Dollar", "J$", "flag_jmd"),
            new Currency("JPY", "Japanese Yen", "¥", "flag_jpy"),
            new Currency("KES", "Kenyan Shilling", "KSh", "flag_kes"),
            new Currency("KRW", "Korea (South) Won", "₩", "flag_krw"),
            new Currency("KWD", "Kuwaiti Dinar", "د.ك", "flag_kwd"),
            new Currency("KYD", "Cayman Islands Dollar", "$", "flag_kyd"),
            new Currency("KZT", "Kazakhstan Tenge", "лв", "flag_kzt"),
            new Currency("LAK", "Laos Kip", "₭", "flag_lak"),
            new Currency("LKR", "Sri Lanka Rupee", "₨", "flag_lkr"),
            new Currency("LRD", "Liberia Dollar", "$", "flag_lrd"),
            new Currency("LTL", "Lithuanian Litas", "Lt", "flag_ltl"),
            new Currency("MAD", "Moroccan Dirham", "MAD", "flag_mad"),
            new Currency("MDL", "Moldovan Leu", "MDL", "flag_mdl"),
            new Currency("MKD", "Macedonia Denar", "ден", "flag_mkd"),
            new Currency("MNT", "Mongolia Tughrik", "₮", "flag_mnt"),
            new Currency("MUR", "Mauritius Rupee", "₨", "flag_mur"),
            new Currency("MWK", "Malawian Kwacha", "MK", "flag_mwk"),
            new Currency("MXN", "Mexico Peso", "$", "flag_mxn"),
            new Currency("MYR", "Malaysia Ringgit", "RM", "flag_myr"),
            new Currency("MZN", "Mozambique Metical", "MT", "flag_mzn"),
            new Currency("NAD", "Namibia Dollar", "$", "flag_nad"),
            new Currency("NGN", "Nigeria Naira", "₦", "flag_ngn"),
            new Currency("NIO", "Nicaragua Cordoba", "C$", "flag_nio"),
            new Currency("NOK", "Norway Krone", "kr", "flag_nok"),
            new Currency("NPR", "Nepal Rupee", "₨", "flag_npr"),
            new Currency("NZD", "New Zealand Dollar", "$", "flag_nzd"),
            new Currency("OMR", "Oman Rial", "﷼", "flag_omr"),
            new Currency("PEN", "Peru Sol", "S/.", "flag_pen"),
            new Currency("PGK", "Papua New Guinean Kina", "K", "flag_pgk"),
            new Currency("PHP", "Philippines Peso", "₱", "flag_php"),
            new Currency("PKR", "Pakistan Rupee", "₨", "flag_pkr"),
            new Currency("PLN", "Poland Zloty", "zł", "flag_pln"),
            new Currency("PYG", "Paraguay Guarani", "Gs", "flag_pyg"),
            new Currency("QAR", "Qatar Riyal", "﷼", "flag_qar"),
            new Currency("RON", "Romania Leu", "lei", "flag_ron"),
            new Currency("RSD", "Serbia Dinar", "Дин.", "flag_rsd"),
            new Currency("RUB", "Russia Ruble", "₽", "flag_rub"),
            new Currency("SAR", "Saudi Arabia Riyal", "﷼", "flag_sar"),
            new Currency("SEK", "Sweden Krona", "kr", "flag_sek"),
            new Currency("SGD", "Singapore Dollar", "$", "flag_sgd"),
            new Currency("SOS", "Somalia Shilling", "S", "flag_sos"),
            new Currency("SRD", "Suriname Dollar", "$", "flag_srd"),
            new Currency("THB", "Thailand Baht", "฿", "flag_thb"),
            new Currency("TTD", "Trinidad and Tobago Dollar", "TT$", "flag_ttd"),
            new Currency("TWD", "Taiwan New Dollar", "NT$", "flag_twd"),
            new Currency("TZS", "Tanzanian Shilling", "TSh", "flag_tzs"),
            new Currency("UAH", "Ukraine Hryvnia", "₴", "flag_uah"),
            new Currency("UGX", "Ugandan Shilling", "USh", "flag_ugx"),
            new Currency("UYU", "Uruguay Peso", "$U", "flag_uyu"),
            new Currency("VEF", "Venezuela Bolívar", "Bs", "flag_vef"),
            new Currency("VND", "Viet Nam Dong", "₫", "flag_vnd"),
            new Currency("YER", "Yemen Rial", "﷼", "flag_yer"),
            new Currency("ZAR", "South Africa Rand", "R", "flag_zar")
    };

    public static ArrayList<Currency> getCurrenciesList() {
        return new ArrayList<>(Arrays.asList(currencies));
    }

    public static Currency getCurrency(String countryCode) {
        for (Currency currency : currencies) {
            if (currency.getCountryCode().equals(countryCode)) {
                return currency;
            }
        }
        return null;
    }
}
