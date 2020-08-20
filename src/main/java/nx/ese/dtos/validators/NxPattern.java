package nx.ese.dtos.validators;

public interface NxPattern {
    static final String USERNAME = "^[a-z0-9_-]{3,40}$";
    static final String PASSWORD = "((?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%]).{6,30})";
    static final String EMAIL = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
    static final String NINE_DIGITS = "\\d{9}";
    static final String DATE_FORMAT ="dd/MM/yyyy";//"EEE, dd MMM yyyy HH:mm:ss zzz";//
    static final String DATE_FORMAT_SHORT ="dd-MM-yyyy";
    static final String LOCAL_DATE_FORMAT ="yyyy-MM-dd";
    static final String LOCAL_DATE_TIME_FORMAT ="yyyy-MM-dd'T'HH:mm:ss.SSS";
}
