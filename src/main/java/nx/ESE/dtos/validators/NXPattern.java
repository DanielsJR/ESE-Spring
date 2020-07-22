package nx.ESE.dtos.validators;

public interface NXPattern {
    static final String USERNAME = "^[a-z0-9_-]{3,40}$";
    static final String PASSWORD = "((?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%]).{6,30})";
    static final String EMAIL = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
    static final String NINE_DIGITS = "\\d{9}";
    static final String DATE_FORMAT ="dd/MM/yyyy";
}
