package nx.ESE.dtos.validators;

public interface Pattern {
    static final String NINE_DIGITS = "\\d{9}";
    static final String EMAIL = "^[_a-z0-9-]+(.[_a-z0-9-]+)*@[a-z0-9-]+(.[a-z0-9-]+)*(.[a-z]{2,4})$";
                                
}
